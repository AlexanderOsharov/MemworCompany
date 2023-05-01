package com.shurik.memwor_22.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.shurik.memwor_24.R
import com.shurik.memwor_24.memwor.content.artems_work.Domain
import com.shurik.memwor_24.memwor.content.artems_work.MemworViewModel
import com.shurik.memwor_24.memwor.content.artems_work.ResponseViewer
import com.shurik.memwor_24.databinding.FragmentVkBinding
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.Post

class VkFragment : Fragment() {

    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var etQuery: EditText? = null
    var btnSearch: Button? = null
    var dialog: Dialog? = null
    var mActivity: Activity? = this.activity
    private var articles = ResponseViewer()

    var vkPosts: MutableList<Post> = ArrayList()
    var vkDomains: MutableList<Domain> = ArrayList()

    lateinit var adapter: ItemAdapter

    lateinit var mContext: Context



    private val accessToken = Constants.ACCESS_TOKEN_VK
    private val domaines = mutableListOf("ul1523")
    private var contents: MutableList<Post> = ArrayList()
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
            articles.vkConfigureRetrofit()
        }

        mContext= requireContext()
        swipeRefreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        articles.getVkInfo()

        etQuery = view?.findViewById<EditText>(R.id.etQuery)
        btnSearch = view?.findViewById<Button>(R.id.btnSearch)
        dialog = mActivity?.let { Dialog(it) }

        swipeRefreshLayout?.setOnRefreshListener {
            articles.getVkInfo()
            swipeRefreshLayout?.isRefreshing = false
        }
        //Какая-нибудь заглушка
        return inflater.inflate(R.layout.fragment_vk, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        MemworViewModel.vkPostsLiveData.observe(viewLifecycleOwner) {
            vkPosts = it

            vkPosts.forEach {
                Log.e("FROM VK FRAGMENT", it.text + " " + it.author + " " + it.category + " " + it.images.toString())
            }
            adapter = ItemAdapter(vkPosts)
            recyclerView?.layoutManager = LinearLayoutManager(mActivity)
            recyclerView?.adapter = adapter
            //recyclerView?.layoutManager = LinearLayoutManager(mActivity)
            val ad = recyclerView?.adapter
            println(ad)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = VkFragment()
    }
}

