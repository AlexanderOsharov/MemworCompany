package com.shurik.memwor_24.memwor.content.new_platform

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View.inflate
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.BrowserActivity
import com.shurik.memwor_24.databinding.AboutUsPopUpBinding.inflate
import com.shurik.memwor_24.databinding.ActivityDopBinding.inflate
import com.shurik.memwor_24.databinding.ActivityNewPlatformBinding
import com.shurik.memwor_24.databinding.SliderLayoutBinding.inflate
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.MemworActivity
import com.shurik.memwor_24.memwor.content.Post
import com.shurik.memwor_24.memwor.content.logic.Domain
import com.shurik.memwor_24.memwor.content.logic.ResponseViewer
import com.shurik.memwor_24.memwor.content.logic.db.MemworDatabaseManager
import com.shurik.memwor_24.memwor.content.logic.quest.QuestApi
import com.shurik.memwor_24.memwor.content.new_platform.CategoryDetector.detectCategoriesWithSynonyms
import com.shurik.memwor_24.memwor.settings.Settings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.contracts.contract

class NewPlatformActivity : AppCompatActivity() {

    private val dbManager = MemworDatabaseManager()
    private lateinit var binding: ActivityNewPlatformBinding

    val vkCoroutineScope = CoroutineScope(Dispatchers.IO)
    val redditCoroutineScope = CoroutineScope(Dispatchers.IO)
    val telegramCoroutineScope = CoroutineScope(Dispatchers.IO)

    lateinit var vkQuestApi: QuestApi
    lateinit var redQuestApi: QuestApi
    lateinit var telQuestApi: QuestApi

    val httpLoggingInterceptor = HttpLoggingInterceptor()

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    val vkRetrofit = Retrofit.Builder()
        .baseUrl("https://api.vk.com/method/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val redRetrofit = Retrofit.Builder()
        .baseUrl("https://reddit.com/r/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val telegramRetrofit = Retrofit.Builder()
        .baseUrl("https://web.telegram.org/k/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val domain = Domain()

    var posts: MutableList<Post> = ArrayList()

    private lateinit var mainFab: FloatingActionButton
    private lateinit var tape: FloatingActionButton
    private lateinit var category: FloatingActionButton
    private lateinit var browser: FloatingActionButton

    private var isFabMenuOpen = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPlatformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //VK.initialize(this)
        mainFab = binding.fab
        category = binding.category
        tape = binding.tape
        browser = binding.browser

        mainFab.apply {
            alpha = 0.5f
            var isLongClick = false
            mainFab.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isLongClick = false
                        mainFab.postDelayed({
                            isLongClick = true
                            mainFab.alpha = 1f
                            mainFab.isEnabled = true
                            startUnlockAnimation()
                        }, 1000) // время удержания для разблокировки кнопки (например, 1000 мс)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (isLongClick) {
                            mainFab.x = event.rawX - mainFab.width / 2
                            mainFab.y = event.rawY - mainFab.height / 2
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        mainFab.removeCallbacks(null) // удаляем отложенное разблокирование кнопки
                        if (isLongClick) {
                            // Здесь можно сохранить положение кнопки, если хотите
                        } else {
                            mainFab.performClick()
                        }
                    }
                }
                true
            }

        }

        mainFab.setOnClickListener {
            if (isFabMenuOpen) {
                hideFabsWithAnimation()
            } else {
                showFabsWithAnimation()
            }
        }

        category.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        tape.setOnClickListener {
            val intent = Intent(this, MemworActivity::class.java)
            startActivity(intent)
        }

        browser.setOnClickListener {
            val intent = Intent(this, BrowserActivity::class.java)
            startActivity(intent)
        }

        val addButton: Button = findViewById(R.id.addBtn)
        addButton.setOnClickListener {
            addDBCommunity()
        }

    }

    fun addDBCommunity() {
        val platformText: EditText = binding.editPlatformText
        val domainText: EditText = binding.editDomainText
        val nameText: EditText = binding.editNameText
        var categoryText: String = ""

        CoroutineScope(Dispatchers.IO).launch {
            val checkPassed = try {
                ((platformText.text.toString() == "vk" && CheckPlatform.checkVkGroup(domainText.text.toString()))
                        || (platformText.text.toString() == "reddit" && CheckPlatform.checkRedditGroup(domainText.text.toString()))
                        || (platformText.text.toString() == "telegram" && CheckPlatform.checkTelegramGroup(nameText.text.toString()))
                        || (platformText.text.toString() == "telegram" && CheckPlatform.checkTelegramGroup(domainText.text.toString()))
                        || (platformText.text.toString() == "telegram" && CheckPlatform.checkTelegramGroup(nameText.text.toString(), domainText.text.toString()))
                        || (platformText.text.toString() == "tiktok" && CheckPlatform.checkTikTokGroup(nameText.text.toString()))
                        || (platformText.text.toString() == "tiktok" && CheckPlatform.checkTikTokGroup(domainText.text.toString()))
                        || (platformText.text.toString() == "tiktok" && CheckPlatform.checkTikTokGroup(nameText.text.toString(), domainText.text.toString())))
            } catch (e: Exception) {
                Log.e("ErrorCheck", "Ошибка при проверке: $e")
                false
            }

            withContext(Dispatchers.Main) {
                if (checkPassed) {
                    val userSelectedCategories = mutableListOf<String>()

                    if (nameText.text.isNullOrEmpty()) nameText.text = domainText.text
                    if (domainText.text.isNullOrEmpty() || platformText.text.toString() == "telegram" && CheckPlatform.checkTelegramGroup(nameText.text.toString())) domainText.text = nameText.text

                    if (binding.memes.isChecked) {
                        categoryText += "memes"
                        userSelectedCategories.add("memes")
                    }
                    if (binding.news.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "news"
                        else categoryText += ", news"
                        userSelectedCategories.add("news")
                    }
                    if (binding.games.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "games"
                        else categoryText += ", games"
                        userSelectedCategories.add("games")
                    }
                    if (binding.films.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "films"
                        else categoryText += ", films"
                        userSelectedCategories.add("films")
                    }
                    if (binding.meal.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "news"
                        else categoryText += ", meal"
                        userSelectedCategories.add("meal")
                    }
                    if (binding.books.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "books"
                        else categoryText += ", books"
                        userSelectedCategories.add("books")
                    }
                    if (binding.animals.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "animals"
                        else categoryText += ", animals"
                        userSelectedCategories.add("animals")
                    }
                    if (binding.psychology.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "psychology"
                        else categoryText += ", psychology"
                        userSelectedCategories.add("psychology")
                    }
                    if (binding.sciences.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "sciences"
                        else categoryText += ", sciences"
                        userSelectedCategories.add("sciences")
                    }
                    if (binding.cartoons.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "cartoons"
                        else categoryText += ", cartoons"
                        userSelectedCategories.add("cartoons")
                    }
                    if (binding.perfumery.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "perfumery"
                        else categoryText += ", perfumery"
                        userSelectedCategories.add("perfumery")
                    }
                    if (binding.clothes.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "clothes"
                        else categoryText += ", clothes"
                        userSelectedCategories.add("clothes")
                    }
                    if (binding.householdItems.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "household items"
                        else categoryText += ", household items"
                        userSelectedCategories.add("household items")
                    }
                    if (binding.chancellery.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "chancellery"
                        else categoryText += ", chancellery"
                        userSelectedCategories.add("chancellery")
                    }
                    if (binding.gardening.isChecked) {
                        if (categoryText.isNullOrEmpty() || categoryText == "") categoryText += "gardening"
                        else categoryText += ", gardening"
                        userSelectedCategories.add("gardening")
                    }

                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    vkQuestApi = vkRetrofit.create(QuestApi::class.java)
                    redQuestApi = redRetrofit.create(QuestApi::class.java)
                    telQuestApi = telegramRetrofit.create(QuestApi::class.java)

                    domain.platform = platformText.text.toString()
                    domain.domain = domainText.text.toString()
                    domain.name = nameText.text.toString()

                    if (domain.platform == "vk") {
                        vkCoroutineScope.async {
                            //delay(Random().nextInt(3000).toLong())
                            vkQuestApi.getVkJson(
                                domain = domainText.text.toString(),
                                access_token = Constants.ACCESS_TOKEN_VK,
                                count = 100,
                                ver = Constants.API_VERSION
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ it ->
                                    Log.e("Retrofit subscribe", "success")
                                    Log.e("Json", it.toString())

                                    posts = ResponseViewer.vkFindUrls(it, domain, "category_detector")
                                }, {
                                    println(it.stackTrace.toString())
                                })
                        }
                    }
                    if (domain.platform == "reddit") {
                        redditCoroutineScope.async {
                            redQuestApi.getRedditJson(
                                domain = domain.domain,
                                reddit_token = Constants.ACCESS_TOKEN_REDDIT
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ it ->
                                    posts = ResponseViewer.redditFindUrls(it, domain, "category_detector")
                                }, {
                                    println(it.stackTrace.toString())
                                })
                        }
                    }
                    if (domain.platform == "telegram") {
                        telegramCoroutineScope.async {
                            posts = ResponseViewer.getPostsFromTelegramGroup(domain.domain, "category_detector") as MutableList<Post>
                        }
                    }

                    if (binding.memes.isChecked) userSelectedCategories.add("memes")
                    // ... добавьте условия для других категорий

                    var content = ""
                    for (post in posts) content += " $post.text"

                    var detectedCategories = CategoryDetector.detectCategories(content, userSelectedCategories)

//                    runBlocking {
//                        detectedCategories = detectCategoriesWithSynonyms(content, userSelectedCategories)
//                    }

                    if (detectedCategories.isNullOrEmpty()) {
                        dbManager.addNewCommunity(
                            platformText.text.toString(),
                            domainText.text.toString(),
                            nameText.text.toString(),
                            detectedCategories.joinToString(", ")
                        )
                        Toast.makeText(
                            this@NewPlatformActivity,
                            "Спасибо за Ваше активное участие",
                            Toast.LENGTH_LONG
                        ).show()
                        Toast.makeText(
                            this@NewPlatformActivity,
                            "в разработке приложения Memwor",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else {
                        Toast.makeText(this@NewPlatformActivity, "Вы неверно указали платформу/домейн/название", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@NewPlatformActivity, "Вы неверно указали платформу/домейн/название", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /*suspend fun getVkPosts(): List<Post> = coroutineScope {
        val vkPostsDeferred = async(Dispatchers.IO) {
            vkQuestApi.getVkJson(
                domain = domain.domain,
                access_token = Constants.ACCESS_TOKEN_VK,
                count = 100,
                ver = Constants.API_VERSION
            )
        }

        val vkResponse = vkPostsDeferred.await()
        Log.e("Retrofit", "success")
        Log.e("Json", vkResponse.toString())

        return@coroutineScope ResponseViewer.vkFindUrls(vkResponse, domain, "category_detector")
    }

    suspend fun getRedditPosts(domain: String): List<Post> = coroutineScope {
        val redditPostsDeferred = async(Dispatchers.IO) {
            redQuestApi.getRedditJson(
                domain = domain,
                reddit_token = Constants.ACCESS_TOKEN_REDDIT
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    posts = ResponseViewer.redditFindUrls(it, domain, "category_detector")
                }, {
                    println(it.stackTrace.toString())
                })
        }
    }

        suspend fun getTelegramPosts(domain: String): List<Post> = coroutineScope {
            // Ваш код для получения постов из Telegram

    }

    suspend fun updateDetectedCategories() = coroutineScope {
        // Логика обновления detectedCategories
    }

    fun setupRetrofit() {
        // Логика создания и настройки Retrofit
    }*/

    private fun showFabsWithAnimation() {
        val tapeAnimator = createFabAnimator(tape, 0f, -200f,0f, 1f)
        val categoryAnimator = createFabAnimator(category, 0f, -200f,0f, 1f)
        val browserAnimator = createFabAnimator(browser, 0f, -200f,0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapeAnimator, categoryAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = true
        category.isEnabled = true
        browser.isEnabled = true
        tape.isEnabled = true
    }

    private fun hideFabsWithAnimation() {
        val tapeAnimator = createFabAnimator(tape, -200f, 0f, 1f, 0f)
        val categoryAnimator = createFabAnimator(category, -200f, 0f, 1f, 0f)
        val browserAnimator = createFabAnimator(browser, -200f, 0f, 1f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapeAnimator, categoryAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = false
        category.isEnabled = false
        browser.isEnabled = false
        tape.isEnabled = false
    }

    private fun startUnlockAnimation() {
        val animator = ObjectAnimator.ofFloat(mainFab, "rotation", 0f, 360f)
        animator.duration = 500
        animator.start()
    }

    private fun startLockAnimation() {
        val animator = ObjectAnimator.ofFloat(mainFab, "rotation", 360f, 0f)
        animator.duration = 500
        animator.start()
    }

    private fun createFabAnimator(fab: FloatingActionButton, fromYDelta: Float, toYDelta: Float, fromScale: Float, toScale: Float): AnimatorSet {
        fab.visibility = View.VISIBLE

        val translateYAnimator = ObjectAnimator.ofFloat(fab, "translationY", fromYDelta, toYDelta)
        val scaleXAnimator = ObjectAnimator.ofFloat(fab, "scaleX", fromScale, toScale)
        val scaleYAnimator = ObjectAnimator.ofFloat(fab, "scaleY", fromScale, toScale)
        val rotateAnimator = ObjectAnimator.ofFloat(fab, "rotation", 0f, 360f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translateYAnimator, scaleXAnimator, scaleYAnimator, rotateAnimator)
        animatorSet.duration = 300

        return animatorSet
    }
}