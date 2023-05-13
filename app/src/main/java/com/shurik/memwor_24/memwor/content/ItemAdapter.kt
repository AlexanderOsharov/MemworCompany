package com.shurik.memwor_24.memwor.content

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.shurik.memwor_24.R
import com.shurik.memwor_24.memwor.content.module_adapter.MediaPagerAdapter

class ItemAdapter(private var posts: MutableList<Post>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        val mediaViewPager: ViewPager2 = itemView.findViewById(R.id.mediaViewPager)
        val textTextView: TextView = itemView.findViewById(R.id.textTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val post = posts[position]

        holder.authorTextView.text = post.author
        holder.categoryTextView.text = post.category
        holder.textTextView.text = post.text

        // Устанавливаем видимость textTextView
        holder.textTextView.visibility = if (post.text.isNotEmpty()) View.VISIBLE else View.GONE

        val mediaPagerAdapter = MediaPagerAdapter(post.images, post.videos)
        holder.mediaViewPager.adapter = mediaPagerAdapter
        holder.mediaViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == mediaPagerAdapter.itemCount - 1) {
                    holder.mediaViewPager.post { holder.mediaViewPager.setCurrentItem(0, false) }
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePosts(newPosts: MutableList<Post>) {
        this.posts = newPosts
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addPosts(newPosts: MutableList<Post>) {
        val previousSize = itemCount
        this.posts.addAll(newPosts)
        notifyItemRangeInserted(previousSize, newPosts.size)
    }
}
