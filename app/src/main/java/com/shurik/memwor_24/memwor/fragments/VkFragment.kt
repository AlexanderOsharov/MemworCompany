package com.shurik.memwor_24.memwor.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
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
import com.shurik.memwor_24.memwor.content.logic.Domain
import com.shurik.memwor_24.memwor.content.logic.MemworViewModel
import com.shurik.memwor_24.memwor.content.logic.ResponseViewer
import com.shurik.memwor_24.databinding.FragmentVkBinding
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.Post

class VkFragment : Fragment() {

    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var etQuery: EditText? = null
    var btnSearch: Button? = null
    var dialog: Dialog? = null
    var mActivity: Activity? = this.activity

    private var vkViewer = ResponseViewer()

    var vkPosts: MutableList<Post> = ArrayList()
    var vkDomains: MutableList<Domain> = ArrayList()

    //lateinit var adapter: ItemAdapter

    lateinit var mContext: Context

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentVkBinding
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //return inflater.inflate(R.layout.fragment_vk, container, false)
        MemworViewModel.vkDomainsLiveData.observe(viewLifecycleOwner) {
            vkDomains = it
            vkDomains.forEach {
                //Log.e("DOMAINS VK", it.domain + it.platform)
            }
            Log.e("VkFragment domain", "success")
            vkViewer.vkConfigureRetrofit()
        }

        binding = FragmentVkBinding.inflate(inflater, container, false)

        mContext= requireContext()
        swipeRefreshLayout = binding.swipeRefresh

        vkViewer.getVkInfo()

        etQuery = binding.etQuery
        btnSearch = binding.btnSearch
        dialog = mActivity?.let { Dialog(it) }

        swipeRefreshLayout?.setOnRefreshListener {
//            vkViewer.getVkInfo()
            val newContentInfo = getRandomContent()
            if (newContentInfo != null) adapter.updatePosts(newContentInfo as MutableList<Post>)
            swipeRefreshLayout?.isRefreshing = false
        }
        //Какая-нибудь заглушка
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView?.layoutManager = LinearLayoutManager(mActivity)
        recyclerView?.adapter = adapter

        MemworViewModel.vkPostsLiveData.observe(viewLifecycleOwner) {
            vkPosts = it

            //vkPosts.forEach {
                //Log.e("FROM VK FRAGMENT", it.text + " " + it.author + " " + it.category + " " + it.images.toString())
           // }
            Log.e("VkFragment posts", "success")
//            adapter.addPosts(vkPosts)
            //recyclerView?.layoutManager = LinearLayoutManager(mActivity)
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
            val vkAnswerList: MutableList<Post> = ArrayList()
            val vkContentList = MemworViewModel.vkPostsLiveData.value
            val query = inputText.text.toString()
            var i: Int? = vkContentList?.size?.minus(1)
            if (i != null) {
                while (i > -1) {
                    val post = vkContentList?.get(i)
                    if (post != null && (post.text.contains(query) || post.author.contains(query) || post.category.contains(query))) {
                        vkAnswerList.add(post)
                        if (vkAnswerList.size == 1) {
                            adapter.updatePosts(vkAnswerList)
                        } else {
                            adapter.addPost(post)
                        }
                    }
                    i--
                }
            }
            if (vkAnswerList.isEmpty()) {
                Toast.makeText(activity, "По вашему запросу ничего не найдено", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getRandomContent(): List<Post>? {
        val vkContentList = MemworViewModel.vkPostsLiveData.value
        if (!vkContentList.isNullOrEmpty()) {
            val shuffledList = vkContentList.shuffled()
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
        fun newInstance() = VkFragment()
    }
}

