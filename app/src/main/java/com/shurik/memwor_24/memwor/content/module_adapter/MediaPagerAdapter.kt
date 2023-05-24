package com.shurik.memwor_24.memwor.content.module_adapter

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.shurik.memwor_24.R
import com.shurik.memwor_24.memwor.content.Post
import kotlinx.android.synthetic.main.media_page_item.view.*

class MediaPagerAdapter(private val post: Post, private val type: String = "fragment") :
    RecyclerView.Adapter<MediaPagerAdapter.MediaViewHolder>() {

    private var isDialogOpened = false

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

            if (type === "fragment") {
                holder.imageView.setOnClickListener {
                    showExpandedDialog(it.context, post.images, post.videos)
                }
            }
            holder.imageView.setOnLongClickListener {
                val popupMessage = PopupMessage(it.context, "Вы действительно хотите загрузить изображение?") {
                    downloadMedia(it.context, post.images[position], "изображение")
                }
                popupMessage.show()
                true
            }
//            // Добавление обработчиков событий нажатия и отпускания для imageView
//            holder.imageView.setOnTouchListener { _, event ->
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> scaleViewUp(holder.itemView)
//                    MotionEvent.ACTION_UP,
//                    MotionEvent.ACTION_CANCEL -> scaleViewDown(holder.itemView)
//                }
//                true
//            }
        } else if (position - post.images.size < post.videos.size) {
            holder.imageView.visibility = View.GONE
            holder.videoView.visibility = View.VISIBLE
            val videoPosition = position - post.images.size
            val videoUri = Uri.parse(post.videos[videoPosition])
            holder.videoView.setVideoPath(post.videos[videoPosition])
            holder.videoView.setOnPreparedListener { mp -> mp.isLooping = true }
            holder.videoView.setOnCompletionListener { holder.videoView.start() }
            holder.videoView.setOnErrorListener { _, what, extra ->
                Log.e("VideoView", "Error occurred: $what, $extra")
                Toast.makeText(holder.itemView.context, "Ошибка при загрузке видео", Toast.LENGTH_SHORT).show()
                true
            }
            holder.videoView.requestFocus()
            holder.videoView.start()

            holder.videoView.setOnClickListener {
                if (holder.videoView.isPlaying) {
                    holder.videoView.pause()
                } else {
                    holder.videoView.start()
                }
            }
            holder.videoView.setOnLongClickListener {
                if (ContextCompat.checkSelfPermission(it.context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    val popupMessage = PopupMessage(it.context, "Вы действительно хотите загрузить видео?") {
                        downloadMedia(it.context, post.videos[position - post.images.size], "видео")
                    }
                    popupMessage.show()
                } else {
                    ActivityCompat.requestPermissions(it.context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
                }
                true
            }
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

    fun showExpandedDialog(context: Context, dialogImages: MutableList<String>, dialogVideos: MutableList<String>) {
        if (isDialogOpened) {
            return
        }
        isDialogOpened = true
        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.item_detailed, null)
        dialog.setContentView(view)
        dialog.setOnDismissListener {
            isDialogOpened = false
        }

        val expandedMediaViewPager: ViewPager2 = view.findViewById<ViewPager2>(R.id.mediaViewPager)
        val mediaPagerAdapter = MediaPagerAdapter(post, "dialog")
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
        author.text = post.author

        // Создаем SpannableStringBuilder с текстом поста
        val spannableStringBuilder = SpannableStringBuilder(post.text)

        // Получаем все URL-ссылки из текста
        val urlPattern = Patterns.WEB_URL
        val matcher = urlPattern.matcher(post.text)

        // Добавляем CustomClickableSpan для каждой найденной ссылки
        while (matcher.find()) {
            val urlStart = matcher.start()
            val urlEnd = matcher.end()
            val url = post.text.substring(urlStart, urlEnd)

            spannableStringBuilder.setSpan(
                CustomClickableSpan(url, context),
                urlStart,
                urlEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Устанавливаем текст для textTextView с кликабельными ссылками
        desc.text = spannableStringBuilder

        // Устанавливаем скролинг textTextView
        desc.movementMethod = ScrollingMovementMethod()

        // Устанавливаем OnLongClickListener для копирования текста
        desc.setOnLongClickListener {
            val clipboardManager = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Copied Text", desc.text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(it.context, "Текст скопирован", Toast.LENGTH_SHORT).show()
            true
        }

        // Устанавливаем MovementMethod для textTextView, чтобы обрабатывать нажатия на ссылки
        desc.movementMethod = LinkMovementMethod.getInstance()

        // Установка определенных размеров или использование WRAP_CONTENT
        dialog.window?.setLayout( WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT )

        val close = view.findViewById<ImageButton>(R.id.btnClose)
        close.setOnClickListener {
            dialog.dismiss()
        }

        // Выравнивание диалогового окна по центру экрана
        dialog.window?.setGravity(Gravity.CENTER)

        dialog.show()

    // Установка отступов для диалогового окна
        val window = dialog.window
        val layoutParams = window?.attributes
        val displayMetrics = context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

    /*// Установка размеров диалогового окна с учетом отступов
        layoutParams?.width = (width * 0.9).toInt() // 90% от ширины экрана
        layoutParams?.height = (height * 0.9).toInt() // 90% от высоты экрана
        window?.attributes = layoutParams*/
    }

    private fun downloadMedia(context: Context, url: String, type: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Загрузка $type")
            .setDescription("Загрузка $type")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf("/") + 1))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
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

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }
}
