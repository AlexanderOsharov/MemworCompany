package com.shurik.memwor_24.memwor.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.Post
import com.shurik.memwor_24.databinding.FragmentRedditBinding
import com.shurik.memwor_24.memwor.content.logic.Domain
import com.shurik.memwor_24.memwor.content.logic.MemworViewModel
import com.shurik.memwor_24.memwor.content.logic.ResponseViewer
import kotlinx.coroutines.*


class RedditFragment : Fragment() {
    private val accessToken = Constants.ACCESS_TOKEN_REDDIT
    //private var domaines = mutableListOf("mildlyinfuriating", "books", "aww")
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentRedditBinding

    var mActivity: Activity? = this.activity
    private var redditViewer = ResponseViewer()

    var redditPosts: MutableList<Post> = ArrayList()
    var redditDomains: MutableList<Domain> = ArrayList()

    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var etQuery: EditText? = null
    var btnSearch: Button? = null
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
        swipeRefreshLayout = binding.swipeRefresh

        etQuery = binding.etQuery
        btnSearch = binding.btnSearch

        swipeRefreshLayout?.setOnRefreshListener {
            val newContentInfo = getRandomContent()
            if (newContentInfo != null) itemAdapter.updatePosts(newContentInfo as MutableList<Post>)
            swipeRefreshLayout?.isRefreshing = false
        }

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
        binding.btnSearch.setOnClickListener {
            SearchInformation()
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


    fun SearchInformation() {
        val inputText: EditText = binding.etQuery
        if (inputText.text.isNullOrEmpty()) {
            Toast.makeText(activity, "Введите запрос", Toast.LENGTH_SHORT).show()
        } else {
            val redditAnswerList: MutableList<Post> = ArrayList()
            val redditContentList = MemworViewModel.redditPostsLiveData.value
            val query = inputText.text.toString()
            var i: Int? = redditContentList?.size?.minus(1)
            if (i != null) {
                while (i > -1) {
                    val post = redditContentList?.get(i)
                    if (post != null && (post.text.contains(query) || post.author.contains(query) || post.category.contains(query))) {
                        redditAnswerList.add(post)
                        if (redditAnswerList.size == 1) {
                            itemAdapter.updatePosts(redditAnswerList)
                        } else {
                            itemAdapter.addPost(post)
                        }
                    }
                    i--
                }
            }
            if (redditAnswerList.isEmpty()) {
                Toast.makeText(activity, "По вашему запросу ничего не найдено", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getRandomContent(): List<Post>? {
        val redditContentList = MemworViewModel.redditPostsLiveData.value
        if (!redditContentList.isNullOrEmpty()) {
            val shuffledList = redditContentList.shuffled()
            if (shuffledList.size >= 10) {
                return shuffledList.subList(0, 10)
            } else {
                return shuffledList.subList(0, shuffledList.size)
            }
        }
        return null
    }

    companion object {
        @JvmStatic
        var itemAdapter: ItemAdapter = ItemAdapter(ArrayList<Post>())
        fun newInstance() = RedditFragment()
    }
}