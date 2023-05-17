package com.shurik.memwor_24.memwor.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.shurik.memwor_24.databinding.FragmentAllBinding
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.Post
import com.shurik.memwor_24.memwor.content.logic.Domain
import com.shurik.memwor_24.memwor.content.logic.MemworViewModel
import com.shurik.memwor_24.memwor.content.logic.ResponseViewer

class AllFragment : Fragment() {

    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var etQuery: EditText? = null
    var btnSearch: Button? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentAllBinding

    var vkPosts: MutableList<Post> = ArrayList()
    var vkDomains: MutableList<Domain> = ArrayList()
    var redditPosts: MutableList<Post> = ArrayList()
    var redditDomains: MutableList<Domain> = ArrayList()

    private var vkViewer = ResponseViewer()
    private var redditViewer = ResponseViewer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        MemworViewModel.vkDomainsLiveData.observe(viewLifecycleOwner) {
            vkDomains = it
            vkDomains.forEach {
                //Log.e("DOMAINS VK", it.domain + it.platform)
            }
            Log.e("VkFragment domain", "success")
            vkViewer.vkConfigureRetrofit()
        }
        vkViewer.getVkInfo()

        MemworViewModel.redditDomainsLiveData.observe(viewLifecycleOwner){
            redditDomains = it
            Log.e("REDDIT DOMAINS SUCCESS","Success")
            redditViewer.redditConfigureRetrofit()
        }
        redditViewer.getRedditInfo()

        binding = FragmentAllBinding.inflate(inflater, container, false)

        swipeRefreshLayout = binding.swipeRefresh

        etQuery = binding.etQuery
        btnSearch = binding.btnSearch

        swipeRefreshLayout?.setOnRefreshListener {
            val newContentInfo = getRandomContent()
            if (newContentInfo != null) adapter.updatePosts(newContentInfo as MutableList<Post>)
            swipeRefreshLayout?.isRefreshing = false
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = VkFragment.adapter

        MemworViewModel.vkPostsLiveData.observe(viewLifecycleOwner) {
            vkPosts = it
            Log.e("VkFragment posts", "success")
            val ad = recyclerView?.adapter
            println(ad)
        }

        MemworViewModel.redditPostsLiveData.observe(viewLifecycleOwner){
            redditPosts = it
            Log.e("Reddit fragment", redditPosts.toString())
            val ad = recyclerView?.adapter
            println(ad)
        }

        binding.btnSearch.setOnClickListener {
            SearchInformation()
        }
    }

    fun SearchInformation() {
        val inputText: EditText = binding.etQuery
        if (inputText.text.isNullOrEmpty()) {
            Toast.makeText(activity, "Введите запрос", Toast.LENGTH_SHORT).show()
        } else {
            val AnswerList: MutableList<Post> = ArrayList()
            val ContentList = MemworViewModel.vkPostsLiveData.value + MemworViewModel.redditPostsLiveData.value
            val query = inputText.text.toString()
            var i: Int? = ContentList?.size?.minus(1)
            if (i != null) {
                while (i > -1) {
                    val post = ContentList?.get(i)
                    if (post != null && (post.text.contains(query) || post.author.contains(query) || post.category.contains(query))) {
                        AnswerList.add(post)
                        if (AnswerList.size == 1) {
                            VkFragment.adapter.updatePosts(AnswerList)
                        } else {
                            VkFragment.adapter.addPost(post)
                        }
                    }
                    i--
                }
            }
            if (AnswerList.isEmpty()) {
                Toast.makeText(activity, "По вашему запросу ничего не найдено", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getRandomContent(): List<Post>? {
        val ContentList = MemworViewModel.vkPostsLiveData.value + MemworViewModel.redditPostsLiveData.value
        if (!ContentList.isNullOrEmpty()) {
            val shuffledList = ContentList.shuffled()
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
        var adapter: ItemAdapter = ItemAdapter(ArrayList<Post>())
        fun newInstance() = AllFragment()
    }
}

private operator fun <E> MutableList<E>?.plus(value: MutableList<E>?): MutableList<E>? {
    var i: Int? = value?.size?.minus(1)

    if (i != null) {
        while (i > -1){
            value?.get(i)?.let { this?.add(it) }
            i--
        }
    }

    return this
}
