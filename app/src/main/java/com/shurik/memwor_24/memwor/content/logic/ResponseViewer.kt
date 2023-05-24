package com.shurik.memwor_24.memwor.content.logic

import android.media.tv.CommandResponse
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.shurik.memwor_24.memwor.fragments.VkFragment
import com.shurik.memwor_24.memwor.content.logic.db.MemworDatabaseManager

import com.shurik.memwor_24.memwor.content.logic.quest.GetVKJsonResponse
import com.shurik.memwor_24.memwor.content.logic.quest.QuestApi
import com.shurik.memwor_24.memwor.content.Post
import com.shurik.memwor_24.memwor.content.module_reddit.ChildData
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.logic.quest.GetRedditJsonResponse
import com.shurik.memwor_24.memwor.fragments.AllFragment
import com.shurik.memwor_24.memwor.fragments.RedditFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

import kotlin.collections.ArrayList

class ResponseViewer (): ViewModel(){

    //val RetrofitClient = RetrofitModule()
    //TODO(IMPORT DOMAINS LIST FROM ACTIVITY)
    private val dbManager = MemworDatabaseManager()
    var vkResList: MutableList<Post> = ArrayList()
    var vkCategoryList: MutableList<Post> = ArrayList()
    var redditResList: MutableList<Post> = ArrayList()
    var telegramResList: MutableList<Post> = ArrayList()
    //var tgDomainsList: MutableList<MutableList<String>> = ArrayList()
    //var redditDomainsList: MutableList<MutableList<String>> = ArrayList()

    private lateinit var vkQuestApi: QuestApi
    private lateinit var redQuestApi: QuestApi
    private lateinit var telQuestApi: QuestApi
    //private var questApi: QuestApi

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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

        val telegramRetrofit = Retrofit.Builder()
            .baseUrl("https://web.telegram.org/k/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        vkQuestApi = vkRetrofit.create(QuestApi::class.java)
        redQuestApi = redRetrofit.create(QuestApi::class.java)
        telQuestApi = telegramRetrofit.create(QuestApi::class.java)
    }

    fun getVkInfo() {
        dbManager.getVkDomains()
    }
    fun getRedditInfo(){
        dbManager.getRedditDomains()
    }
    fun getTelegramInfo(){
        dbManager.getTelegramDomains()
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
                            Log.e("Json", it1.toString())

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
            Log.e("Retrofit await all", "Success retrofit")
        }
    }

    fun telegramConfigureRetrofit(){
        val telegramCoroutineScope = CoroutineScope(Dispatchers.IO)
        telegramCoroutineScope.launch {
            MemworViewModel.telegramDomainsLiveData.value?.map {
                delay(2000)
                telegramCoroutineScope.async {
                    getPostsFromTelegramGroup(it.domain)
                }
            }?.awaitAll()
            Log.e("Retrofit awaitall", "success")
            //MemworViewModel.vkPostsLiveData.setValueToPosts(vkResList)
        }
    }

//    private fun telegramConfigureRetrofit() {
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
//    }

    companion object{
        var vkResList: MutableList<Post> = ArrayList()
        var redditResList: MutableList<Post> = ArrayList()
        var telegramResList: MutableList<Post> = ArrayList()

        private val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun vkFindUrls(res: GetVKJsonResponse, domain: Domain, commandResponse: String = "parsing"): MutableList<Post> {
        val resList: MutableList<Post> = ArrayList()
        res.vkResponse?.items?.forEach {
            if (it.marked_as_ads == 0) {

                val post = Post()
                post.category = domain.category
                post.author = domain.name
                post.text = it.text

                val inter_list: MutableList<String> = ArrayList()
                it.attachments?.forEach {
                    it?.photo?.sizes?.last()?.url?.let { it1 ->
                        inter_list.add(it1)
                    }
                }

                post.images = inter_list
                if(!post.images.isNullOrEmpty()){
                    vkResList.add(post)
//                    val categoriesList = context?.let { Constants.getCategories(it) }
//                    if (post.category.contains("memes") && categoriesList?.get("memes") == true ||
//                        post.category.contains("news") && categoriesList?.get("news") == true ||
//                        post.category.contains("games") && categoriesList?.get("games") == true ||
//                        post.category.contains("films") && categoriesList?.get("films") == true ||
//                        post.category.contains("books") && categoriesList?.get("books") == true ||
//                        post.category.contains("animals") && categoriesList?.get("animals") == true ||
//                        post.category.contains("psychology") && categoriesList?.get("psychology") == true ||
//                        post.category.contains("sciences") && categoriesList?.get("sciences") == true ||
//                        post.category.contains("meal") && categoriesList?.get("meal") == true ||
//                        post.category.contains("cartoons") && categoriesList?.get("cartoons") == true ||
//                        post.category.contains("perfumery") && categoriesList?.get("perfumery") == true ||
//                        post.category.contains("clothes") && categoriesList?.get("clothes") == true ||
//                        post.category.contains("household items") && categoriesList?.get("householdItems") == true ||
//                        post.category.contains("chancellery") && categoriesList?.get("chancellery") == true ||
//                        post.category.contains("gardening") && categoriesList?.get("gardening") == true)
                    if (commandResponse == "parsing") MemworViewModel.vkPostsLiveData.addPost(post)
//                    if (vkResList.size % 10 == 0) {
////                        VkFragment.adapter.addPosts(vkResList)
//                        val DataPosts: MutableList<Post> = ArrayList()
//                        var i: Int = vkResList.size
//                        while (i > vkResList.size - 10){
//                            DataPosts.add(vkResList[i - 1])
//                            i--
//                        }
//                        VkFragment.adapter?.addPosts(DataPosts)
//                    }
                    if (vkResList.size < 11 && commandResponse == "parsing") {
                        coroutineScope.launch {
                            VkFragment.adapter?.addPost(post)
                        }
                        coroutineScope.launch {
                            AllFragment.AllFragmentAdapter.addPost(post)
                        }
                    }
                    if (commandResponse == "category_detector") resList.add(post)
                }

            }
        }
        return resList
    }

    fun redditFindUrls(res: GetRedditJsonResponse, domain: Domain, commandResponse: String = "парсинг"): MutableList<Post> {
        val resList: MutableList<Post> = ArrayList()
        res.data?.children?.forEach {

            val post = Post()
            post.author = domain.domain
            post.category = domain.category

            //post.videos = extractVideos(it.data)
            post.text = it.data.selftext

            val inter_list: MutableList<String> = ArrayList()
            it.data.preview?.images?.forEach { it1 ->
                val rawUrl = it1.source.url
                val decodedUrl = rawUrl.replace("&amp;", "&") // Заменяем экранированные символы на их исходные значения
                inter_list.add(decodedUrl)
            }
            post.images = inter_list
//            post.images = getDirectImageUrl(domain.domain, Constants.ACCESS_TOKEN_VK)

            //if(!post.images.isNullOrEmpty()){
            redditResList.add(post)
            if (commandResponse == "парсинг") MemworViewModel.redditPostsLiveData.addPost(post)
            Log.e("Reddit source: ", post.images.toString())
            if (redditResList.size < 11 && commandResponse == "parsing") {
                coroutineScope.launch {
                    RedditFragment.itemAdapter.addPost(post)
                    AllFragment.AllFragmentAdapter.addPost(post)
                }
            }
            //}
            Log.e("RedditPost", post.toString())
            if (commandResponse == "category_detector") resList.add(post)
        }
        return resList
    }

    fun getPostsFromTelegramGroup(domain: String, commandResponse: String = "parsing"): List<Post> {
        val url = "https://t.me/$domain"
        val document: Document = Jsoup.connect(url).get()
        val posts: MutableList<Post> = mutableListOf()

        val postElements: List<Element> = document.select("div.tgme_widget_message_bubble")
        for (postElement in postElements) {
            val post = Post()
            post.text = postElement.select("div.tgme_widget_message_text").text()
            post.category = postElement.select("div.tgme_widget_message_info > a.tgme_widget_message_group").text()
            post.author = postElement.select("div.tgme_widget_message_info > a.tgme_widget_message_owner").text()

            val mediaElements: List<Element> = postElement.select("div.tgme_widget_message_media_group > div.tgme_widget_message_photo_wrap > a > img.tgme_widget_message_photo")
            for (mediaElement in mediaElements) {
                post.images.add(mediaElement.attr("src"))
            }

            val videoElements: List<Element> = postElement.select("div.tgme_widget_message_media_group > div.tgme_widget_message_video_wrap > a")
            for (videoElement in videoElements) {
                post.videos.add(videoElement.attr("href"))
            }

            if (commandResponse == "parsing") MemworViewModel.telegramPostsLiveData.addPost(post)
            telegramResList.add(post)
            posts.add(post)
            if (telegramResList.size < 11) coroutineScope.launch {AllFragment.AllFragmentAdapter.addPost(post)}
        }

        return posts
    }
    }
    private fun getDirectImageUrl(domain: String, token: String): MutableList<String> {
        val url = "https://oauth.reddit.com/${domain}.json?raw_json=1"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.setRequestProperty("Authorization", "Bearer $token")
        connection.connect()

        val data = BufferedReader(connection.inputStream.reader()).readText()
        val json = JSONObject(data)
        val dataObj = json.getJSONObject("data")
        val children = dataObj.getJSONArray("children")
        val postJson = children.getJSONObject(0).getJSONObject("data")
        val preview = postJson.getJSONObject("preview")
        val images = preview.getJSONArray("images")
        val source = images.getJSONObject(0).getJSONObject("source")
        val urls = mutableListOf<String>()
        urls.add(source.getString("url"))
        return urls
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