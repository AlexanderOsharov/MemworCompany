package com.shurik.memwor_24.memwor.fragments

import android.annotation.SuppressLint
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
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.module_reddit.RedditResponse
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.Post
import com.shurik.memwor_24.memwor.content.module_reddit.ChildData
import com.shurik.memwor_24.databinding.FragmentRedditBinding
import com.shurik.memwor_24.memwor.api.module_reddit.RedditAPI
import kotlinx.coroutines.*
import java.io.IOException


class RedditFragment : Fragment() {
    private val accessToken = Constants.ACCESS_TOKEN_REDDIT
    private var domaines = mutableListOf("gaming","Funny", "Gaming",
        "WorldNews", "Science", "Technology")
    private var contents: MutableList<Post> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var binding: FragmentRedditBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRedditBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

//        GlobalScope.launch {
//            try {
//                val subreddit = "gaming"
//                val redditAPI = RedditAPI()
//                val jsonContent = redditAPI.getJsonContent(subreddit)
//                println(jsonContent)
//                Log.e("ReddditJson: ", jsonContent)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            contents = getPostsBySubreddits(domaines)

            // Обратить внимание на использование Dispatchers.Main для работы с UI-элементами
            withContext(Dispatchers.Main) {
                // Теперь contents заполнен постами и затем можно обновить RecyclerView здесь
                itemAdapter = ItemAdapter(contents)
                recyclerView.adapter = itemAdapter
            }
        }
    }

    suspend fun getPostsBySubreddits(subredditList: List<String>): MutableList<Post> {
        val redditAPI = RedditAPI()
        val posts: MutableList<Post> = mutableListOf()

        for (subreddit in subredditList) {
            try {
                val jsonContent = withContext(Dispatchers.IO) { redditAPI.getJsonContent(subreddit) }

                val gson = Gson()
                val redditResponse: RedditResponse = gson.fromJson(jsonContent, RedditResponse::class.java)
                Log.e("Json", gson.toString())
                val children = redditResponse.data.children

                for (child in children) {
                    val post = Post()

                    post.author = child.data.author
                    post.category = child.data.subreddit
                    post.images = extractImages(child.data)
                    post.videos = extractVideos(child.data)
                    post.text = child.data.selftext

                    posts.add(post)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        println(posts)
        Log.e("Reddit_Posts: ", posts.toString())
        return posts
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




    companion object {
        @JvmStatic
        fun newInstance() = RedditFragment()
    }
}