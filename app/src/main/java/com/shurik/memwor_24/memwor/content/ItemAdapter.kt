package com.shurik.memwor_24.memwor.content

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.shurik.memwor_24.R
import com.shurik.memwor_24.memwor.content.module_adapter.MediaPagerAdapter
import kotlin.math.roundToInt

class ItemAdapter(private var posts: MutableList<Post>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val expandedTextPositions = mutableSetOf<Int>()

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        val mediaViewPager: ViewPager2 = itemView.findViewById(R.id.mediaViewPager)
        val textTextView: TextView = itemView.findViewById(R.id.textTextView)
        val cardView: CardView = itemView.findViewById(R.id.cardView)

        fun toggleCardSize() {
            val originalHeight = cardView.layoutParams.height
            val targetHeight = if (originalHeight == 100.dpToPx(itemView.context)) {
                200.dpToPx(itemView.context)
            } else {
                100.dpToPx(itemView.context)
            }

            val valueAnimator = ValueAnimator.ofInt(originalHeight, targetHeight)
            valueAnimator.addUpdateListener {
                val layoutParams = cardView.layoutParams
                layoutParams.height = it.animatedValue as Int
                cardView.layoutParams = layoutParams
            }
            valueAnimator.duration = 300
            valueAnimator.start()
        }

        private fun Int.dpToPx(context: Context): Int {
            val displayMetrics = context.resources.displayMetrics
            return (this * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
        }


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

        // Устанавливаем максимальное количество строк в соответствии с состоянием раскрытого/свернутого текста
        holder.textTextView.maxLines = if (expandedTextPositions.contains(position)) Int.MAX_VALUE else 4

        // Добавляем OnClickListener для развертывания/сворачивания текста
        holder.textTextView.setOnClickListener {
            if (expandedTextPositions.contains(position)) {
                expandedTextPositions.remove(position)
                holder.textTextView.maxLines = 4
            } else {
                expandedTextPositions.add(position)
                holder.textTextView.maxLines = Int.MAX_VALUE
            }
        }

        val mediaPagerAdapter = MediaPagerAdapter(post)
        holder.mediaViewPager.adapter = mediaPagerAdapter
        holder.mediaViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == mediaPagerAdapter.itemCount - 1) {
                    holder.mediaViewPager.post { holder.mediaViewPager.setCurrentItem(0, false) }
                }
            }
        })

        holder.itemView.setOnClickListener {
            holder.toggleCardSize()
        }

        holder.textTextView.setOnLongClickListener {
            val clipboardManager = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Copied Text", holder.textTextView.text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(it.context, "Текст скопирован", Toast.LENGTH_SHORT).show()
            true
        }

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

    @SuppressLint("NotifyDataSetChanged")
    fun addPost(newPost: Post) {
        val previousSize = itemCount
        this.posts.add(newPost)
        notifyItemRangeInserted(previousSize, 1)
    }
}
