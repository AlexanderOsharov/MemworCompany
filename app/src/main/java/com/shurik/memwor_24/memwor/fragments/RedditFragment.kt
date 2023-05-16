package com.shurik.memwor_24.memwor.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.shurik.memwor_24.R
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.module_reddit.RedditResponse
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.Post
import com.shurik.memwor_24.memwor.content.module_reddit.ChildData
import com.shurik.memwor_24.databinding.FragmentRedditBinding
import com.shurik.memwor_24.memwor.api.module_reddit.RedditAPI
import com.shurik.memwor_24.memwor.content.artems_work.Domain
import com.shurik.memwor_24.memwor.content.artems_work.MemworViewModel
import com.shurik.memwor_24.memwor.content.artems_work.ResponseViewer
import kotlinx.coroutines.*
import java.io.IOException


class RedditFragment : Fragment() {
    private val accessToken = Constants.ACCESS_TOKEN_REDDIT
    //private var domaines = mutableListOf("mildlyinfuriating", "books", "aww")
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentRedditBinding

    var mActivity: Activity? = this.activity
    private var redditViewer = ResponseViewer()

    var redditPosts: MutableList<Post> = ArrayList()
    var redditDomains: MutableList<Domain> = ArrayList()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        MemworViewModel.redditDomainsLiveData.observe(viewLifecycleOwner){
            redditDomains = it
            Log.e("REDDIT DOMAINS SUCCESS","Success")
            redditViewer.redditConfigureRetrofit()
        }
        redditViewer.getRedditInfo()
        binding = FragmentRedditBinding.inflate(inflater, container, false)


        return binding.root
        //return inflater.inflate(R.layout.fragment_reddit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView?.layoutManager = LinearLayoutManager(mActivity)
        recyclerView?.adapter = itemAdapter
        MemworViewModel.redditPostsLiveData.observe(viewLifecycleOwner){
            redditPosts = it

            Log.e("Reddit fragment", redditPosts.toString())
//            itemAdapter.addPosts(redditPosts)

            //recyclerView?.layoutManager = LinearLayoutManager(mActivity)
            val ad = recyclerView?.adapter
            println(ad)
        }
    }

//    suspend fun getPostsBySubreddits(subredditList: List<String>): MutableList<Post> {
//        val redditAPI = RedditAPI()
//        val posts: MutableList<Post> = mutableListOf()
//        Log.e("GetPost", "true")
//        for (subreddit in subredditList) {
//            try {
//                val vkCoroutineScope = CoroutineScope(Dispatchers.IO)
//                val jsonContent = withContext(Dispatchers.IO) {
//                    redditAPI.getJsonContent(subreddit)
//                }
//
//                val gson = Gson()
//                val redditResponse: RedditResponse = gson.fromJson(jsonContent, RedditResponse::class.java)
//                Log.e("JsonReddit", gson.toString())
//                val children = redditResponse.data.children
//
//                for (child in children) {
//                    val post = Post()
//
//                    post.author = child.data.author
//                    post.category = child.data.subreddit
//                    post.images = extractImages(child.data)
//                    post.videos = extractVideos(child.data)
//                    post.text = child.data.selftext
//
//                    posts.add(post)
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        println(posts)
//        Log.e("Reddit_Posts: ", posts.toString())
//        return posts
//    }
//
//    private fun extractImages(data: ChildData): MutableList<String> {
//        val images: MutableList<String> = mutableListOf()
//
//        data.preview?.let { preview ->
//            preview.images.forEach { image ->
//                images.add(image.source.url)
//            }
//        }
//
//        return images
//    }
//
//    private fun extractVideos(data: ChildData): MutableList<String> {
//        val videos: MutableList<String> = mutableListOf()
//
//        // Здесь нам нужно будет наполнить список видео на основе данных, так как Reddit не предоставляет такую информацию,
//        // то, возможно, нам придется найти другой способ получения видео из постов.
//
//        return videos
//    }




    companion object {
        @JvmStatic
        var itemAdapter: ItemAdapter = ItemAdapter(ArrayList<Post>())
        fun newInstance() = RedditFragment()
    }
}