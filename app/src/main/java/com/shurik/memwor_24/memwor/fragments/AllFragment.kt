package com.shurik.memwor_24.memwor.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
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
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.Post
import com.shurik.memwor_24.memwor.content.logic.Domain
import com.shurik.memwor_24.memwor.content.logic.MemworViewModel
import com.shurik.memwor_24.memwor.content.logic.ResponseViewer
import com.shurik.memwor_24.memwor.content.logic.search.SearchInfo
import com.shurik.memwor_24.memwor.content.module_adapter.MediaPagerAdapter.Companion.PERMISSION_REQUEST_CODE
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apache.commons.lang3.ObjectUtils.max
import org.apache.commons.lang3.ObjectUtils.min
import java.util.*
import kotlin.collections.ArrayList

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
    var telegramPosts: MutableList<Post> = ArrayList()
    var telegramDomains: MutableList<Domain> = ArrayList()

    private var vkViewer = ResponseViewer()
    private var redditViewer = ResponseViewer()
    private var telegramViewer = ResponseViewer()

    val mutex = Mutex()
    private val coroutineScope1 = CoroutineScope(Dispatchers.Main)
    private val coroutineScope2 = CoroutineScope(Dispatchers.Main)

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

        MemworViewModel.telegramDomainsLiveData.observe(viewLifecycleOwner){
            telegramDomains = it
            Log.e("TELEGRAM DOMAINS SUCCESS","Success")
            telegramViewer.telegramConfigureRetrofit()
        }
        telegramViewer.getTelegramInfo()

        binding = FragmentAllBinding.inflate(inflater, container, false)

        swipeRefreshLayout = binding.swipeRefresh

        etQuery = binding.etQuery
        btnSearch = binding.btnSearch

        swipeRefreshLayout?.setOnRefreshListener {
            val newContentInfo = getRandomContent()
            if ((newContentInfo != null) && newContentInfo.isNotEmpty()) coroutineScope1.launch { AllFragmentAdapter.updatePosts(newContentInfo as MutableList<Post>) }
            else {
                val toast = Toast.makeText(activity, "Сервер перегружен, \nвыберите другую категорию", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                Toast.makeText(activity, "или попробуйте позже", Toast.LENGTH_LONG).show()
            }
            swipeRefreshLayout?.isRefreshing = false
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(false)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = AllFragmentAdapter

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

        MemworViewModel.telegramPostsLiveData.observe(viewLifecycleOwner){
            telegramPosts = it
            Log.e("Reddit fragment", telegramPosts.toString())
            val ad = recyclerView?.adapter
            println(ad)
        }

        // Добавление OnScrollListener к RecyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Получение LayoutManager (предполагая, что используем LinearLayoutManager)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                // Получение текущее количество элементов в адаптере
                val itemCount = layoutManager.itemCount

                // Получение позицию последнего видимого элемента
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // Проверка на достижение пользователем нижнего края списка
                if (lastVisibleItemPosition >= itemCount - 1) {
                    // Здесь выполняется код для загрузки нового содержимого и обновления адаптера
                    val newContentInfo = getRandomContent()
                    if ((newContentInfo != null) && newContentInfo.isNotEmpty() && AllFragmentAdapter.itemCount != 0) {
                        coroutineScope1.launch { AllFragmentAdapter.addPosts(newContentInfo as MutableList<Post>) }
                    } else {
                        val toast = Toast.makeText(activity, "Сервер перегружен, \nвыберите другую категорию", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                        Toast.makeText(activity, "или попробуйте позже", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        binding.btnSearch.setOnClickListener {
            SearchInformation()
        }
        startMonitoringRecyclerView(recyclerView)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun startMonitoringRecyclerView(recyclerView: RecyclerView) {
        coroutineScope2.launch {
            while (isActive) {
                delay(2000) // Проверка каждые 2 секунды

                // Добавляем проверку на инициализацию RecyclerView
                if (recyclerView.layoutManager == null) continue

                val visibleItemCount = recyclerView.childCount
                val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0

                if (totalItemCount > 50 || memoryUsageIsHigh()) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    val itemsToPreserveBefore = 10
                    val itemsToPreserveAfter = 10

                    var startPreserveIndex = max(0, firstVisibleItemPosition - itemsToPreserveBefore)
                    var endPreserveIndex = min(totalItemCount - 1, lastVisibleItemPosition + itemsToPreserveAfter)

                    // Если startPreserveIndex все еще больше endPreserveIndex, меняем их местами
                    if (startPreserveIndex > endPreserveIndex) {
                        val temp = startPreserveIndex
                        startPreserveIndex = endPreserveIndex
                        endPreserveIndex = temp
                    }

                    Log.d("ClearRecyclerView", "startPreserveIndex: $startPreserveIndex, endPreserveIndex: $endPreserveIndex")

                    // Проверяем допустимые значения startPreserveIndex и endPreserveIndex
                    if (startPreserveIndex >= 0 && endPreserveIndex < AllFragmentAdapter.itemCount) {
                        try {
                            clearRecyclerView(recyclerView, startPreserveIndex, endPreserveIndex)
                        } catch (e: Exception) {
                            Log.e("RecyclerViewClearing", "Error while clearing RecyclerView", e)
                        }
                    }
                }
            }
        }
    }


    private fun memoryUsageIsHigh(): Boolean {
        val runtime = Runtime.getRuntime()
        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L // в мегабайтах
        val maxMemory = runtime.maxMemory() / 1048576L // в мегабайтах

        // Проверяем, что используемая память превышает 80% от максимально доступной памяти
        return usedMemory > (maxMemory * 0.8)
    }

    suspend fun clearRecyclerView(recyclerView: RecyclerView, startPreserveIndex: Int, endPreserveIndex: Int) {
        val adapter = recyclerView.adapter as ItemAdapter
        if (startPreserveIndex < 0 || startPreserveIndex >= adapter.posts.size) {
            throw IllegalArgumentException("startPreserveIndex($startPreserveIndex) is out of bounds")
        }
        if (endPreserveIndex < 0 || endPreserveIndex >= adapter.posts.size) {
            throw IllegalArgumentException("endPreserveIndex($endPreserveIndex) is out of bounds")
        }
        if (startPreserveIndex > endPreserveIndex) {
            throw IllegalArgumentException("startPreserveIndex($startPreserveIndex) must be <= endPreserveIndex($endPreserveIndex)")
        }

        mutex.withLock {
            val removedBefore = startPreserveIndex
            val removedAfter = adapter.posts.size - (endPreserveIndex + 1)

            // Удаляем элементы до startPreserveIndex
            for (i in 0 until removedBefore) {
                adapter.posts.removeAt(0)
            }

            // Удаляем элементы после endPreserveIndex
            for (i in 0 until removedAfter) {
                adapter.posts.removeAt(adapter.posts.size - 1)
            }

            withContext(Dispatchers.Main) {

                // Уведомляем адаптер об удалении элементов
                if (removedBefore > 0) {
                    adapter.notifyItemRangeRemoved(0, removedBefore)
                }
                if (removedAfter > 0) {
                    adapter.notifyItemRangeRemoved(endPreserveIndex + 1 - removedBefore, removedAfter)
                }
            }
        }
    }



    @OptIn(DelicateCoroutinesApi::class)
    fun SearchInformation() {
        val query: String = binding.etQuery.text.toString()
        if (query.isNullOrEmpty()) {
            Toast.makeText(activity, "Введите запрос", Toast.LENGTH_SHORT).show()
        } else {
            coroutineScope1.launch {
                withContext(Dispatchers.Main) {
                    AllFragmentAdapter.clearItems()
                }
            }

            // Разбиваем строку query на список слов
            val queryList = query.split(" ")

            val allAnswerList: MutableList<Post> = ArrayList()
            val allContentList = MemworViewModel.vkPostsLiveData.value.plus(
                MemworViewModel.redditPostsLiveData.value.plus(
                    MemworViewModel.telegramPostsLiveData.value
                )
            )
            var i: Int? = allContentList?.size?.minus(1)
            if (i != null) {
                while (i > -1) {
                    val post = allContentList?.get(i)

                    // Проверяем совпадения для каждого слова в списке
                    val matchFound = queryList.any { word ->
                        post != null && (post.text.contains(word) || post.author.contains(word) || post.category.contains(word))
                    }

                    if (matchFound) {
                        if (post != null) {
                            allAnswerList.add(post)
                            coroutineScope1.launch {
                                withContext(Dispatchers.Main) {
                                    AllFragmentAdapter.addPost(post)
                                }
                            }
                        }
                    }
                    i--
                }
            }
            if (allAnswerList.isEmpty()) {
                Toast.makeText(activity, "По вашему запросу ничего не найдено", Toast.LENGTH_SHORT).show()
            }
        }

//        lifecycleScope.launch {
//            val yandexSearch = YandexSearch()
//            if (query.isNotEmpty()) {
//                try {
//                    val posts = yandexSearch.search(query)
//                    adapter.updatePosts(posts as MutableList<Post>)
//                } catch (e: Exception) {
//                    Log.e("Error", "Search failed", e)
//                    // Обработка ошибки, например, показать пользователю сообщение об ошибке
//                }
//            }
//        }
        /*val search = PostSearch(Constants.ACCESS_TOKEN_YANDEX)
        GlobalScope.launch(Dispatchers.Main) {
            val post = withContext(Dispatchers.IO) { search.search(query) }
            post?.let { adapter.addPost(it) }
        }*/

        performSearch(query)
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun performSearch(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val googleSearch = SearchInfo.GoogleSearch(query)
                val uniqueSearch = getRandomElements(googleSearch)
                coroutineScope1.launch{ AllFragmentAdapter.addPosts(uniqueSearch as MutableList<Post>) }
            } catch (e: Exception) {
                // Обработка ошибки, например, показать сообщение пользователю
                Toast.makeText(context, "Ошибка при выполнении поиска: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val habrSearch = SearchInfo.HabrSearch(query)
                val uniqueSearch = getRandomElements(habrSearch)
                coroutineScope1.launch{ AllFragmentAdapter.addPosts(uniqueSearch as MutableList<Post>) }
            } catch (e: Exception) {
                // Обработка ошибки, например, показать сообщение пользователю
                Toast.makeText(context, "Ошибка при выполнении поиска: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val vkSearch = SearchInfo.VkSearch(query)
                val uniqueSearch = getRandomElements(vkSearch)
                coroutineScope1.launch{ AllFragmentAdapter.addPosts(uniqueSearch as MutableList<Post>) }
            } catch (e: Exception) {
                // Обработка ошибки, например, показать сообщение пользователю
                Toast.makeText(context, "Ошибка при выполнении поиска: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val wikipediaSearch = SearchInfo.WikipediaSearch(query)
                val uniqueSearch = getRandomElements(wikipediaSearch)
                coroutineScope1.launch{ AllFragmentAdapter.addPosts(uniqueSearch as MutableList<Post>) }
            } catch (e: Exception) {
                // Обработка ошибки, например, показать сообщение пользователю
                Toast.makeText(context, "Ошибка при выполнении поиска: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getRandomContent(): List<Post>? {
        val allContentList = MemworViewModel.vkPostsLiveData.value.plus(
            MemworViewModel.redditPostsLiveData.value.plus(
                MemworViewModel.telegramPostsLiveData.value
            )
        )
        val categoriesList = context?.let { Constants.getCategories(it) }

        if (categoriesList?.get("memes") == true) performSearch("memes")
        if (categoriesList?.get("news") == true) performSearch("news")
        if (categoriesList?.get("games") == true) performSearch("games")
        if (categoriesList?.get("films") == true) performSearch("films")
        if (categoriesList?.get("meal") == true) performSearch("meal")
        if (categoriesList?.get("books") == true) performSearch("books")
        if (categoriesList?.get("animals") == true) performSearch("animals")
        if (categoriesList?.get("psychology") == true) performSearch("psychology")
        if (categoriesList?.get("sciences") == true) performSearch("sciences")
        if (categoriesList?.get("cartoons") == true) performSearch("cartoons")
        if (categoriesList?.get("perfumery") == true) performSearch("perfumery")
        if (categoriesList?.get("clothes") == true) performSearch("clothes")
        if (categoriesList?.get("householdItems") == true) performSearch("household items")
        if (categoriesList?.get("chancellery") == true) performSearch("chancellery")
        if (categoriesList?.get("gardening") == true) performSearch("gardening")

        if (!allContentList.isNullOrEmpty()) {
            // Генерируем сид для случайного перемешивания
            val seed = System.currentTimeMillis()

            // Используем сгенерированный сид для перемешивания списка
            val shuffledList = allContentList.shuffled(Random(seed))

            val resList = ArrayList<Post>()
            var i: Int = shuffledList.size - 1

            while (resList.size != 10 && i > -1) {
                if (shuffledList[i].category.contains("memes") && categoriesList?.get("memes") == true ||
                    shuffledList[i].category.contains("news") && categoriesList?.get("news") == true ||
                    shuffledList[i].category.contains("games") && categoriesList?.get("games") == true ||
                    shuffledList[i].category.contains("films") && categoriesList?.get("films") == true ||
                    shuffledList[i].category.contains("meal") && categoriesList?.get("meal") == true ||
                    shuffledList[i].category.contains("books") && categoriesList?.get("books") == true ||
                    shuffledList[i].category.contains("animals") && categoriesList?.get("animals") == true ||
                    shuffledList[i].category.contains("psychology") && categoriesList?.get("psychology") == true ||
                    shuffledList[i].category.contains("sciences") && categoriesList?.get("sciences") == true ||
                    shuffledList[i].category.contains("cartoons") && categoriesList?.get("cartoons") == true ||
                    shuffledList[i].category.contains("perfumery") && categoriesList?.get("perfumery") == true ||
                    shuffledList[i].category.contains("clothes") && categoriesList?.get("clothes") == true ||
                    shuffledList[i].category.contains("household items") && categoriesList?.get("householdItems") == true ||
                    shuffledList[i].category.contains("chancellery") && categoriesList?.get("chancellery") == true ||
                    shuffledList[i].category.contains("gardening") && categoriesList?.get("gardening") == true) {
                    resList.add(shuffledList[i])
                }
                i--
            }
            return if (resList.size == 0) null else resList
        }
        return null
    }

    fun getRandomElements(list: List<Post>): List<Post> {
        return if (list.size > 20) {
            val shuffledList = list.shuffled()
            val distinctList = shuffledList.distinct()
            val randomElements = distinctList.take(20)
            randomElements.toMutableList()
        } else {
            return list
        }
    }

    fun <T> List<T>.customShuffle(): List<T> {
        val resultList = toMutableList()
        val random = Random()
        for (i in resultList.indices.reversed()) {
            val randomIndex = random.nextInt(i + 1)
            val temp = resultList[i]
            resultList[i] = resultList[randomIndex]
            resultList[randomIndex] = temp
        }
        return resultList
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Разрешение на запись в хранилище предоставлено", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Разрешение на запись в хранилище не предоставлено", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        @JvmStatic
        var AllFragmentAdapter: ItemAdapter = ItemAdapter(ArrayList<Post>())
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
