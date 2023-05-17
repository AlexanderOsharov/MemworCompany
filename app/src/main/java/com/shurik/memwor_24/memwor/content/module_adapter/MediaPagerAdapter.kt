package com.shurik.memwor_24.memwor.content.module_adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Matrix
import android.net.Uri
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.shurik.memwor_24.R
import com.shurik.memwor_24.memwor.content.Post
import kotlinx.android.synthetic.main.media_page_item.view.*

class MediaPagerAdapter(private val post: Post) :
    RecyclerView.Adapter<MediaPagerAdapter.MediaViewHolder>() {

    inner class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val videoView: VideoView = itemView.findViewById(R.id.videoView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.media_page_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        if (position < post.images.size) {
            holder.imageView.visibility = View.VISIBLE
            holder.videoView.visibility = View.GONE
            Glide.with(holder.itemView.context)
                .load(post.images[position])
                .transform(RoundedCorners(20))
                .into(holder.imageView)

//            // Добавление обработчиков событий нажатия и отпускания для imageView
//            holder.imageView.setOnTouchListener { _, event ->
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> scaleViewUp(holder.itemView)
//                    MotionEvent.ACTION_UP,
//                    MotionEvent.ACTION_CANCEL -> scaleViewDown(holder.itemView)
//                }
//                true
//            }
            /*holder.imageView.setOnClickListener {
                showExpandedDialog(it.context, post.images, post.videos)
            }*/
        } else if (position - post.images.size < post.videos.size) {
            holder.imageView.visibility = View.GONE
            holder.videoView.visibility = View.VISIBLE
            val videoPosition = position - post.images.size
            val videoUri = Uri.parse(post.videos[videoPosition])
            holder.videoView.setVideoURI(videoUri)
            holder.videoView.setOnPreparedListener { mp -> mp.isLooping = true }
            holder.videoView.setOnCompletionListener { holder.videoView.start() }
            holder.videoView.requestFocus()
            holder.videoView.start()

//            // Добавление обработчиков событий нажатия и отпускания для videoView
//            holder.videoView.setOnTouchListener { _, event ->
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> scaleViewUp(holder.videoView)
//                    MotionEvent.ACTION_UP,
//                    MotionEvent.ACTION_CANCEL -> scaleViewDown(holder.videoView)
//                }
//                true
//            }
        }
    }

    fun showExpandedDialog(context: Context,  dialogImages: MutableList<String>, dialogVideos: MutableList<String>) {
        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.item_detailed, null)
        dialog.setContentView(view)


        val expandedMediaViewPager: ViewPager2 = view.findViewById<ViewPager2>(R.id.viewPager2)
        val mediaPagerAdapter = MediaPagerAdapter(post)
        expandedMediaViewPager.adapter = mediaPagerAdapter
        expandedMediaViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == mediaPagerAdapter.itemCount - 1) {
                    expandedMediaViewPager.post { expandedMediaViewPager.setCurrentItem(0, false) }
                }
            }
        })
        val category: TextView = view.findViewById<TextView>(R.id.category)
        val author: TextView = view.findViewById(R.id.author)
        val desc: TextView = view.findViewById(R.id.desc)

        category.text = post.category
        author.text = post.text
        desc.text = post.text

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.show()
    }

    private fun scaleViewUp(view: View) {
        if (view is ImageView) {
            val matrix = Matrix(view.imageMatrix)
            matrix.setScale(1.5f, 1.5f, view.width / 2f, view.height / 2f)
            view.imageMatrix = matrix
            view.invalidate()
        }
        else view.animate().scaleX(1.5f).scaleY(1.5f).setDuration(200).start()
    }

    private fun scaleViewDown(view: View) {
        if (view is ImageView) {
            val matrix = Matrix(view.imageMatrix)
            matrix.setScale(1f, 1f, view.width / 2f, view.height / 2f)
            view.imageMatrix = matrix
            view.invalidate()
        }
        else view.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
    }


    override fun getItemCount(): Int {
        return post.images.size + post.videos.size
    }
}
