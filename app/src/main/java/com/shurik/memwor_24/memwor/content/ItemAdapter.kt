package com.shurik.memwor_24.memwor.content

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil.isValidUrl
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.shurik.memwor_24.R
import com.shurik.memwor_24.memwor.content.module_adapter.CustomClickableSpan
import com.shurik.memwor_24.memwor.content.module_adapter.MediaPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt

class ItemAdapter(var posts: MutableList<Post>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val expandedTextPositions = mutableSetOf<Int>()
    val mutex = Mutex()

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        val mediaViewPager: ViewPager2 = itemView.findViewById(R.id.mediaViewPager)
        val textTextView: TextView = itemView.findViewById(R.id.textTextView)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val gradientImageView: ImageView = itemView.findViewById(R.id.gradientImageView)
        val mediaFrameLayout: FrameLayout = itemView.findViewById(R.id.mediaFrameLayout)

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
        if (post.videos.size != 0) post.text += post.videos[0]
        holder.authorTextView.text = post.author
        holder.categoryTextView.text = post.category

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
                CustomClickableSpan(url, holder.itemView.context),
                urlStart,
                urlEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Устанавливаем текст для textTextView с кликабельными ссылками
        holder.textTextView.text = spannableStringBuilder

        // Устанавливаем скролинг textTextView
        holder.textTextView.movementMethod = ScrollingMovementMethod()

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

        // Устанавливаем OnLongClickListener для копирования текста
        holder.textTextView.setOnLongClickListener {
            val clipboardManager = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Copied Text", holder.textTextView.text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(it.context, "Текст скопирован", Toast.LENGTH_SHORT).show()
            true
        }

        // Устанавливаем MovementMethod для textTextView, чтобы обрабатывать нажатия на ссылки
        holder.textTextView.movementMethod = LinkMovementMethod.getInstance()

        // Запуск корутины для асинхронной проверки изображений и видео
        CoroutineScope(Dispatchers.Main).launch {
            // Обработка изображений
            val validImages = mutableListOf<String>()
            for (image in post.images) {
                if (isValidUrl(image)) {
                    validImages.add(image)
                }
            }
            post.images = validImages

            // Обработка видео
            val validVideos = mutableListOf<String>()
            for (video in post.videos) {
                if (isValidUrl(video)) {
                    validVideos.add(video)
                }
            }
            post.videos = validVideos
        }

        val mediaPagerAdapter = MediaPagerAdapter(post)
        // Проверка на наличие изображений или видео
        val hasMedia = post.images.isNotEmpty() || post.videos.isNotEmpty()

        // Установка видимости элементов в зависимости от наличия изображений или видео
        if (hasMedia) {
            holder.mediaViewPager.visibility = View.VISIBLE
            holder.gradientImageView.visibility = View.VISIBLE
            holder.mediaViewPager.adapter = mediaPagerAdapter
            holder.mediaViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        if (position == mediaPagerAdapter.itemCount - 1) {
                            holder.mediaViewPager.post { holder.mediaViewPager.setCurrentItem(0, false) }
                        }
                    }
                })

            holder.mediaFrameLayout.layoutParams.height = dpToPx(200) // Устанавливаем высоту FrameLayout обратно на 200dp
            } else {

            holder.mediaViewPager.visibility = View.GONE
            holder.gradientImageView.visibility = View.GONE

            holder.mediaFrameLayout.layoutParams.height = dpToPx(0) // Устанавливаем высоту FrameLayout на 0dp
            }
    }

    // Вспомогательная функция для проверки доступности и валидности ссылки
    suspend fun isValidUrl(url: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "HEAD"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                val responseCode = connection.responseCode
                connection.disconnect()
                responseCode == HttpURLConnection.HTTP_OK
            } catch (e: Exception) {
                false
            }
        }
    }

    fun dpToPx(dp: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).toInt()
    }

    suspend fun removeInvalidMediaLinks(post: Post) {
        // Удалить недействительные ссылки на изображения
        post.images = post.images.filter { isValidImageUrl(it) }.toMutableList()

        // Удалить недействительные ссылки на видео
        post.videos = post.videos.filter { isValidVideoUrl(it) }.toMutableList()
    }

    suspend fun isValidImageUrl(url: String): Boolean {
        // Здесь проверьте, является ли URL допустимым и доступным
        return true
    }

    suspend fun isValidVideoUrl(url: String): Boolean {
        // Здесь проверьте, является ли URL допустимым и доступным
        return true
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun updatePosts(newPosts: MutableList<Post>) {
        mutex.withLock {
            this.posts = newPosts
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun addPosts(newPosts: MutableList<Post>) {
        mutex.withLock {
            val previousSize = itemCount
            this.posts.addAll(newPosts)
            notifyItemRangeInserted(previousSize, newPosts.size)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun addPost(newPost: Post) {
        mutex.withLock {
            val previousSize = itemCount
            this.posts.add(newPost)
            notifyItemRangeInserted(previousSize, 1)
        }
    }

    suspend fun removeItem(item: Post) {
        mutex.withLock {
            // Удаление элемента из списка
            posts.remove(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun clearItems() {
        mutex.withLock {
            this.posts.clear()
            notifyDataSetChanged()
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    suspend fun clearItemsWithPreservation() {
        mutex.withLock {
            val itemsToSave = 20 // количество элементов, которые нужно сохранить
            val totalItems = posts.size

            if (totalItems <= itemsToSave * 2) {
                // если элементов меньше, чем нужно сохранить, просто очищаем список
                this.posts.clear()
            } else {
                // сохраняем первые и последние элементы, а затем удаляем все остальные
                val postsToSave = arrayListOf<Post>()
                postsToSave.addAll(posts.subList(0, itemsToSave))
                postsToSave.addAll(posts.subList(totalItems - itemsToSave, totalItems))
                posts.clear()
                posts.addAll(postsToSave)
            }
            notifyDataSetChanged()
        }
    }

    suspend fun removeItemsExceptCurrentAndAdjacent(position: Int) {
        mutex.withLock {
            val start = Math.max(0, position - 10)
            val end = Math.min(itemCount - 1, position + 10)
            for (i in 0..start) {
                if (i != position) {
                    posts.removeAt(i)
                    notifyItemRemoved(i)
                }
            }
            for (i in end..itemCount) {
                if (i != position) {
                    posts.removeAt(i)
                    notifyItemRemoved(i)
                }
            }
        }
    }

    suspend fun removeItemsExceptRange(centerPosition: Int) {
        mutex.withLock {
            val range = 10
            val startIndex = (centerPosition - range).coerceAtLeast(0)
            val endIndex = (centerPosition + range).coerceAtMost(posts.size - 1)

            // Удаление элементов перед и после заданного диапазона
            posts.subList(endIndex + 1, posts.size).clear()
            posts.subList(0, startIndex).clear()

            // Сообщаем адаптеру об удалении элементов
            notifyItemRangeRemoved(endIndex + 1, posts.size)
            notifyItemRangeRemoved(0, startIndex)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun clearItemsWithPreservation(startPreserveIndex: Int, endPreserveIndex: Int) {
        mutex.withLock {
            val itemsToPreserve = posts.subList(startPreserveIndex, endPreserveIndex + 1)

            posts.clear()
            posts.addAll(itemsToPreserve)

            notifyDataSetChanged()
        }
    }

}
