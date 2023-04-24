package com.shurik.memwor_24.memwor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.R
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.Post
import org.json.JSONObject

class TikTokFragment : Fragment() {
    private val accessToken = Constants.ACCESS_TOKEN_TIKTOK
    private val domaines = mutableListOf("xor_journal", "kladovaya_knig",
        "prgbooks_archive", "+yVwn8nVbzkIxOTA6", "HtmlGram1")
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private var contents: MutableList<Post> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tik_tok, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        itemAdapter = ItemAdapter(contents)
        recyclerView.adapter = itemAdapter

        domaines.forEach { domain ->
            getPostsForDomain(domain)
        }
        return rootView
    }

    private fun getPostsForDomain(domain: String) {
        val url = "https://api.tiktok.com/v1/public/detailed/blob?name=$domain&access_token=$accessToken"
        val requestQueue = Volley.newRequestQueue(context)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val post = parsePostFromResponse(response, domain)
                contents.add(post)
                itemAdapter.updatePosts(contents)
            },
            { error ->
                error.printStackTrace()
            }
        )

        requestQueue.add(request)
    }

    private fun parsePostFromResponse(response: JSONObject, domain: String): Post {
        val post = Post()
        post.author = domain
        val data = response.getJSONObject("data")

        // Parse text
        post.text = data.getString("text")

        // Parse images
        val images = mutableListOf<String>()
        val imagesJsonArray = data.getJSONArray("images")
        for (i in 0 until imagesJsonArray.length()) {
            val imageUrl = imagesJsonArray.getString(i)
            images.add(imageUrl)
        }
        post.images = images

        // Parse videos
        val videos = mutableListOf<String>()
        val videosJsonArray = data.getJSONArray("videos")
        for (i in 0 until videosJsonArray.length()) {
            val videoUrl = videosJsonArray.getString(i)
            videos.add(videoUrl)
        }
        post.videos = videos

        return post
    }

    companion object {
        @JvmStatic
        fun newInstance() = TikTokFragment()
    }
}

