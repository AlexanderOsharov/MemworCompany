package com.shurik.memwor_24.memwor.content.artems_work

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.shurik.memwor_22.fragments.VkFragment
import com.shurik.memwor_24.memwor.api.module_reddit.RedditAPI
import com.shurik.memwor_24.memwor.content.artems_work.db.MemworDatabaseManager

import com.shurik.memwor_24.memwor.content.artems_work.quest.GetVKJsonResponse
import com.shurik.memwor_24.memwor.content.artems_work.quest.QuestApi
import com.shurik.memwor_24.memwor.content.Post
import com.shurik.memwor_24.memwor.content.module_reddit.ChildData
import com.shurik.memwor_24.memwor.content.module_reddit.RedditResponse
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.artems_work.quest.GetRedditJsonResponse
import com.shurik.memwor_24.memwor.fragments.RedditFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

import kotlin.collections.ArrayList

class ResponseViewer (): ViewModel(){

    //val RetrofitClient = RetrofitModule()
    //TODO(IMPORT DOMAINS LIST FROM ACTIVITY)
    private val dbManager = MemworDatabaseManager()
    var vkResList: MutableList<Post> = ArrayList()
    var redditResList: MutableList<Post> = ArrayList()
    //var tgDomainsList: MutableList<MutableList<String>> = ArrayList()
    //var redditDomainsList: MutableList<MutableList<String>> = ArrayList()

    private lateinit var vkQuestApi: QuestApi
    private lateinit var redQuestApi: QuestApi
    //private var questApi: QuestApi

    init{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val vkRetrofit = Retrofit.Builder()
            .baseUrl("https://api.vk.com/method/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val redRetrofit = Retrofit.Builder()
            .baseUrl("https://reddit.com/r/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        vkQuestApi = vkRetrofit.create(QuestApi::class.java)
        redQuestApi = redRetrofit.create(QuestApi::class.java)

    }

    fun getVkInfo() {
        dbManager.getVkDomains()
    }
    fun getRedditInfo(){
        dbManager.getRedditDomains()
    }

    fun vkConfigureRetrofit(){
        val vkCoroutineScope = CoroutineScope(Dispatchers.IO)
        vkCoroutineScope.launch {
            MemworViewModel.vkDomainsLiveData.value?.map {
                delay(2000)
                vkCoroutineScope.async {
                    //delay(Random().nextInt(3000).toLong())
                    vkQuestApi.getVkJson(
                        domain = it.domain,
                        access_token = Constants.ACCESS_TOKEN_VK,
                        count = 100,
                        ver = Constants.API_VERSION
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ it1 ->
                            Log.e("Retrofit subscribe", "success")
                            vkFindUrls(it1, it)
                        }, {
                            println(it.stackTrace.toString())
                        })
                }
            }?.awaitAll()
            Log.e("Retrofit awaitall", "success")
            //MemworViewModel.vkPostsLiveData.setValueToPosts(vkResList)
        }
    }

    fun redditConfigureRetrofit() {

        val redditCoroutineScope = CoroutineScope(Dispatchers.IO)
        redditCoroutineScope.launch {
            MemworViewModel.redditDomainsLiveData.value?.map {
                delay(5000)
                redditCoroutineScope.async {
                    //delay(Random().nextInt(3000).toLong())
                    redQuestApi.getRedditJson(
                        domain = it.domain,
                        reddit_token = Constants.ACCESS_TOKEN_REDDIT
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ it1 ->
                            redditFindUrls(it1, it)
                        }, {
                            println(it.stackTrace.toString())
                        })
                }
            }?.awaitAll()
            Log.e("Retrofit await all","Success retrofit")
            MemworViewModel.redditPostsLiveData.setValueToPosts(redditResList)
        }
    }

    private fun telegramConfigureRetrofit() {
//
//        val httpLoggingInterceptor = HttpLoggingInterceptor()
//        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(httpLoggingInterceptor)
//            .build()
//
//        //TODO(Add https for telegram api methods)
//        val retrofit = Retrofit.Builder()
//            .baseUrl("")
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//
//        questApi = retrofit.create(QuestApi::class.java)
//
//        compositeDisposable.add(
//            questApi.getTelegramJson()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    telegramFindUrls(it)
//                }, {
//                    //println(it.stackTrace.toString())
//                })
//        )
    }

    private fun vkFindUrls(res: GetVKJsonResponse, domain: Domain) {
        res.vkResponse?.items?.forEach {
            if (it.marked_as_ads == 0) {

                val post = Post()
                post.category = domain.category
                post.author = domain.name
                post.text = it.text

                val inter_list: MutableList<String> = ArrayList()
                it.attachments?.forEach {
                    it?.photo?.sizes?.last()?.url?.let { it1 -> inter_list.add(it1) }
                }

                post.images = inter_list
                if(!post.images.isNullOrEmpty()){
                    vkResList.add(post)
                    if (vkResList.size % 10 == 0) {
//                        VkFragment.adapter.addPosts(vkResList)
                        val DataPosts: MutableList<Post> = ArrayList()
                        var i: Int = vkResList.size
                        while (i > vkResList.size - 10){
                            DataPosts.add(vkResList[i - 1])
                            i--
                        }
                        VkFragment.adapter.addPosts(DataPosts)
                    }
                }

            }
        }
    }

    private fun redditFindUrls(res: GetRedditJsonResponse, domain: Domain) {
        res.data?.children?.forEach {

            val post = Post()
            post.author = domain.domain
            post.category = domain.category

            //post.videos = extractVideos(it.data)
            post.text = it.data.selftext

            val inter_list: MutableList<String> = ArrayList()
            it.data.preview?.images?.forEach {it1->
                inter_list.add(it1.source.url)
            }
            post.images = inter_list

            //if(!post.images.isNullOrEmpty()){
            redditResList.add(post)
            Log.e("Reddit source: ", post.images.toString())
            if (redditResList.size % 10 == 0) {
//                        RedditFragment.itemAdapter.addPosts(redditResList)
                val DataPosts: MutableList<Post> = ArrayList()
                var i: Int = redditResList.size
                while (i > redditResList.size - 10){
                    DataPosts.add(redditResList[i - 1])
                    i--
                }
                RedditFragment.itemAdapter.addPosts(DataPosts)
            }
            //}

        }

    }
    private fun extractImages(data: ChildData): MutableList<String> {
        val images: MutableList<String> = mutableListOf()

        data.preview?.let { preview ->
            preview.images.forEach { image ->
                images.add(image.source.url)
            }
        }

        return images
    }

    private fun extractVideos(data: ChildData): MutableList<String> {
        val videos: MutableList<String> = mutableListOf()

        // Здесь нам нужно будет наполнить список видео на основе данных, так как Reddit не предоставляет такую информацию,
        // то, возможно, нам придется найти другой способ получения видео из постов.

        return videos
    }


    //TODO(Replace string with telegram data class response)
     fun telegramFindUrls(res: String) {

        //TODO(Add to QuestApi and data.remote.quest telegram methods)
        //TODO(Add return statement)



        //        res.redditResponse?.items?.forEach {
        //            if (it.marked_as_ads == 0) {
        //                val inter_list: MutableList<String> = ArrayList()
        //
        //                // Checking for empty text
        //
        //                inter_list.add(it.text)
        //
        //
        //                // Adding existing urls to intermediate list
        //                it.attachments?.forEach {
        //                      if (it.type == "photo"){
        //                    it?.photo?.sizes?.last()?.url?.let { it1 -> inter_list.add(it1) }
        //                      }
        //
        //                }
        //
        //                // checking for empty intermediate list
        //                if (!inter_list.isNullOrEmpty()) {
        //                    res_list.add(inter_list)
        //                }
        //            }
        //        }
        //        debug output (comment if not necessary)
        //        Log.e("TAAAAG", res_list.toString())
    }
}