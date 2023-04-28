package com.shurik.memwor_22.fragments
/*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.Post
import com.shurik.memwor_22.databinding.FragmentVkBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VkFragment : Fragment() {
    private val accessToken = Constants.ACCESS_TOKEN_VK
    private val domaines = mutableListOf("ul1523")
    private var contents: MutableList<Post> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentVkBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentVkBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()

        loadPosts()

        val adapter = ItemAdapter(contents)
        recyclerView.adapter = adapter

        return binding.root
    }

    private fun loadPosts() {
        for (domain in domaines) {
            getVkPosts(domain) { posts ->
                posts?.let {
                    contents.addAll(it)
                    (recyclerView.adapter as? ItemAdapter)?.addPosts(it)
                }
            }
        }
    }

    private fun getVkPosts(domain: String, onComplete: (MutableList<Post>?) -> Unit) {
        val request = object : VKRequest<List<Post>>("wall.get") {
            init {
                addParam("domain", domain)
                addParam("access_token", accessToken)
            }

            override fun parse(r: JSONObject): List<Post> {
                val result = mutableListOf<Post>()
                val items = r.getJSONObject("response").getJSONArray("items")
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val post = Post()
                    post.author = domain
                    post.text = item.getString("text")
                    post.images = getImages(item)
                    post.videos = getVideos(item)
                    result.add(post)
                }
                return result
            }
        }

        VK.execute(request, object : VKApiCallback<List<Post>> {
            override fun success(result: List<Post>) {
                onComplete(result.toMutableList())
            }

            override fun fail(error: VKApiExecutionException) {
                onComplete(null)
            }
        })
    }

    private fun getImages(item: JSONObject): MutableList<String> {
        val images = mutableListOf<String>()
        if (item.has("attachments")) {
            val attachments = item.getJSONArray("attachments")
            for (i in 0 until attachments.length()) {
                val attachment = attachments.getJSONObject(i)
                if (attachment.getString("type") == "photo") {
                    val sizes = attachment.getJSONObject("photo").getJSONArray("sizes")
                    for (j in 0 until sizes.length()) {
                        val size = sizes.getJSONObject(j)
                        images.add(size.getString("url"))
                    }
                }
            }
        }
        return images
    }

    private fun getVideos(item: JSONObject): MutableList<String> {
        val videos = mutableListOf<String>()

        val videosArray = item.getJSONArray("videos")
        for (i in 0 until videosArray.length()) {
            val video = videosArray.getJSONObject(i)
            val url = video.getString("url")
            videos.add(url)
        }

        return videos
    }

    companion object {
        @JvmStatic
        fun newInstance() = VkFragment()
    }
}

*/