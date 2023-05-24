package com.shurik.memwor_24.memwor.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
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
import com.shurik.memwor_24.memwor.content.logic.Domain
import com.shurik.memwor_24.memwor.content.logic.MemworViewModel
import com.shurik.memwor_24.memwor.content.logic.ResponseViewer
import com.shurik.memwor_24.databinding.FragmentVkBinding
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.ItemAdapter
import com.shurik.memwor_24.memwor.content.Post
import com.shurik.memwor_24.memwor.content.logic.search.SearchInfo
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class VkFragment : Fragment() {

    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var etQuery: EditText? = null
    var btnSearch: Button? = null
    var dialog: Dialog? = null
    var mActivity: Activity? = this.activity

    private var vkViewer = ResponseViewer()
    private val coroutineScope2 = CoroutineScope(Dispatchers.Main)

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
            //Toast.makeText(activity, "Запрос к функции", Toast.LENGTH_SHORT)
            val newContentInfo = getRandomContent()
            if ((newContentInfo != null) && newContentInfo.isNotEmpty()) coroutineScope.launch { adapter.updatePosts(newContentInfo as MutableList<Post>) }
            else {
                val toast = Toast.makeText(activity, "Сервер перегружен, \nвыберите другую категорию", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                Toast.makeText(activity, "или попробуйте позже", Toast.LENGTH_LONG).show()
            }
            //Toast.makeText(activity, "Условия пройдены", Toast.LENGTH_SHORT)
            swipeRefreshLayout?.isRefreshing = false
        }
        //Какая-нибудь заглушка
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(false)
        recyclerView?.layoutManager = LinearLayoutManager(mActivity)
        recyclerView?.adapter = adapter

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
                    if ((newContentInfo != null) && newContentInfo.isNotEmpty() && adapter.itemCount != 0) {
                        coroutineScope.launch { adapter.addPosts(newContentInfo as MutableList<Post>) }
                    } else {
                        val toast = Toast.makeText(activity, "Сервер перегружен, \nвыберите другую категорию", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                        Toast.makeText(activity, "или попробуйте позже", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

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
        startMonitoringRecyclerView(recyclerView)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun startMonitoringRecyclerView(recyclerView: RecyclerView) {
        coroutineScope2.launch {
            while (isActive) {
                delay(2000) // Проверка каждые 2 секунды
                val visibleItemCount = recyclerView.childCount
                val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0

                if (totalItemCount > 50 || memoryUsageIsHigh()) {
                    clearRecyclerView(recyclerView)
                }
            }
        }
    }

    private fun memoryUsageIsHigh(): Boolean {
        // Реализуйте проверку использования памяти здесь
        // Например, проверка процента использования памяти или доступной памяти
        return false
    }

    private fun clearRecyclerView(recyclerView: RecyclerView) {
        coroutineScope.launch { adapter.clearItemsWithPreservation() }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun SearchInformation() {
        val query: String = binding.etQuery.text.toString()
        if (query.isNullOrEmpty()) {
            Toast.makeText(activity, "Введите запрос", Toast.LENGTH_SHORT).show()
        } else {
            coroutineScope.launch { adapter.clearItems() }

            // Разбиваем строку query на список слов
            val queryList = query.split(" ")

            val vkAnswerList: MutableList<Post> = ArrayList()
            val vkContentList = MemworViewModel.vkPostsLiveData.value
            var i: Int? = vkContentList?.size?.minus(1)
            if (i != null) {
                while (i > -1) {
                    val post = vkContentList?.get(i)

                    // Проверяем совпадения для каждого слова в списке
                    val matchFound = queryList.any { word ->
                        post != null && (post.text.contains(word) || post.author.contains(word) || post.category.contains(word))
                    }

                    if (matchFound) {
                        if (post != null) {
                            vkAnswerList.add(post)
                        }
                        if (post != null) {
                            coroutineScope.launch { adapter.addPost(post) }
                        }
                    }
                    i--
                }
            }
            if (vkAnswerList.isEmpty()) {
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
                adapter.addPosts(uniqueSearch as MutableList<Post>)
            } catch (e: Exception) {
                // Обработка ошибки, например, показать сообщение пользователю
                Toast.makeText(context, "Ошибка при выполнении поиска: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val habrSearch = SearchInfo.HabrSearch(query)
                val uniqueSearch = getRandomElements(habrSearch)
                adapter.addPosts(uniqueSearch as MutableList<Post>)
            } catch (e: Exception) {
                // Обработка ошибки, например, показать сообщение пользователю
                Toast.makeText(context, "Ошибка при выполнении поиска: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val vkSearch = SearchInfo.VkSearch(query)
                val uniqueSearch = getRandomElements(vkSearch)
                adapter.addPosts(uniqueSearch as MutableList<Post>)
            } catch (e: Exception) {
                // Обработка ошибки, например, показать сообщение пользователю
                Toast.makeText(context, "Ошибка при выполнении поиска: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val wikipediaSearch = SearchInfo.WikipediaSearch(query)
                val uniqueSearch = getRandomElements(wikipediaSearch)
                adapter.addPosts(uniqueSearch as MutableList<Post>)
            } catch (e: Exception) {
                // Обработка ошибки, например, показать сообщение пользователю
                Toast.makeText(context, "Ошибка при выполнении поиска: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getRandomContent(): List<Post>? {
        val vkContentList = MemworViewModel.vkPostsLiveData.value
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
        if (categoriesList?.get("householdItems") == true) performSearch("householdItems")
        if (categoriesList?.get("chancellery") == true) performSearch("chancellery")
        if (categoriesList?.get("gardening") == true) performSearch("gardening")

        if (!vkContentList.isNullOrEmpty()) {
            // Генерируем сид для случайного перемешивания
            val seed = System.currentTimeMillis()

            // Используем сгенерированный сид для перемешивания списка
            val shuffledList = vkContentList.shuffled(Random(seed))

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

    companion object {
        @JvmStatic
        var adapter: ItemAdapter = ItemAdapter(ArrayList<Post>())
        fun newInstance() = VkFragment()
    }
}

