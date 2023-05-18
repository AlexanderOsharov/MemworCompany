package com.shurik.memwor_24.memwor.settings

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.BrowserActivity
import com.shurik.memwor_24.databinding.ActivitySettingsBinding
import com.shurik.memwor_24.memwor.MemworActivity
import com.shurik.memwor_24.memwor.content.new_platform.NewPlatformActivity

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    var dialog: Dialog? = null
    var btnSettings: Button? = null

    private lateinit var mainFab: FloatingActionButton
    private lateinit var tape: FloatingActionButton
    private lateinit var database: FloatingActionButton
    private lateinit var browser: FloatingActionButton

    private var isFabMenuOpen = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        btnSettings = findViewById<Button>(R.id.close_)
//        dialog = Dialog(this)
//        btnSettings!!.setOnClickListener { showDialog() }

        mainFab = binding.fab
        tape = binding.tape
        database = binding.database
        browser = binding.browser

        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Загрузка сохраненных значений чекбоксов
        binding.memes.isChecked = sharedPreferences.getBoolean("memes", true)
        binding.news.isChecked = sharedPreferences.getBoolean("news", true)
        binding.games.isChecked = sharedPreferences.getBoolean("games", true)
        binding.films.isChecked = sharedPreferences.getBoolean("films", true)
        binding.meal.isChecked = sharedPreferences.getBoolean("meal", true)
        binding.books.isChecked = sharedPreferences.getBoolean("books", true)
        binding.animals.isChecked = sharedPreferences.getBoolean("animals", true)
        binding.gardening.isChecked = sharedPreferences.getBoolean("gardening", true)
        binding.psychology.isChecked = sharedPreferences.getBoolean("psychology", true)
        binding.sciences.isChecked = sharedPreferences.getBoolean("sciences", true)
        binding.cartoons.isChecked = sharedPreferences.getBoolean("cartoons", true)
        binding.perfumery.isChecked = sharedPreferences.getBoolean("perfumery", true)
        binding.clothes.isChecked = sharedPreferences.getBoolean("clothes", true)
        binding.householdItems.isChecked = sharedPreferences.getBoolean("householdItems", true)
        binding.chancellery.isChecked = sharedPreferences.getBoolean("chancellery", true)

        // Установка слушателей изменений состояния чекбоксов
        binding.memes.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("memes", isChecked).apply()
        }
        binding.news.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("news", isChecked).apply()
        }
        binding.games.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("games", isChecked).apply()
        }
        binding.films.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("films", isChecked).apply()
        }
        binding.meal.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("meal", isChecked).apply()
        }
        binding.books.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("books", isChecked).apply()
        }
        binding.animals.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("animals", isChecked).apply()
        }
        binding.gardening.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("gardening", isChecked).apply()
        }
        binding.psychology.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("psychology", isChecked).apply()
        }
        binding.sciences.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("sciences", isChecked).apply()
        }
        binding.cartoons.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("cartoons", isChecked).apply()
        }
        binding.perfumery.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("perfumery", isChecked).apply()
        }
        binding.clothes.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("clothes", isChecked).apply()
        }
        binding.householdItems.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("householdItems", isChecked).apply()
        }
        binding.chancellery.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("chancellery", isChecked).apply()
        }

        mainFab.setOnClickListener {
            if (isFabMenuOpen) {
                hideFabsWithAnimation()
            } else {
                showFabsWithAnimation()
            }
        }

        tape.setOnClickListener {
            val intent = Intent(this, MemworActivity::class.java)
            startActivity(intent)
        }

        database.setOnClickListener {
            val intent = Intent(this, NewPlatformActivity::class.java)
            startActivity(intent)
        }

        browser.setOnClickListener {
            val intent = Intent(this, BrowserActivity::class.java)
            startActivity(intent)
        }
    }
//    fun showDialog() {
//        val btnClose: Button
//        dialog?.setContentView(R.layout.about_us_pop_up)
//        dialog!!.show()
//        btnClose = dialog!!.findViewById(R.id.close_)
//        btnClose.setOnClickListener { dialog!!.dismiss() }
//    }

    private fun showFabsWithAnimation() {
        val tapeAnimator = createFabAnimator(tape, 0f, -200f)
        val databaseAnimator = createFabAnimator(database, 0f, -200f)
        val browserAnimator = createFabAnimator(browser, 0f, -200f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapeAnimator, databaseAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = true
    }

    private fun hideFabsWithAnimation() {
        val tapeAnimator = createFabAnimator(tape, -200f, 0f)
        val databaseAnimator = createFabAnimator(database, -200f, 0f)
        val browserAnimator = createFabAnimator(browser, -200f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapeAnimator, databaseAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = false
    }

    private fun createFabAnimator(fab: FloatingActionButton, fromYDelta: Float, toYDelta: Float): ObjectAnimator {
        fab.visibility = View.VISIBLE

        val animator = ObjectAnimator.ofFloat(fab, "translationY", fromYDelta, toYDelta)
        animator.duration = 300

        return animator
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        memes = binding.memes.isChecked
//        news = binding.news.isChecked
//        games = binding.games.isChecked
//        films = binding.films.isChecked
//        meal = binding.meal.isChecked
//        books = binding.books.isChecked
//        animals = binding.animals.isChecked
//        psychology = binding.psychology.isChecked
//        sciences = binding.sciences.isChecked
//        cartoons = binding.cartoons.isChecked
//        perfumery = binding.perfumery.isChecked
//        clothes = binding.clothes.isChecked
//        household_items = binding.householdItems.isChecked
//        chancellery = binding.chancellery.isChecked
//        gardening = binding.gardening.isChecked
//    }
//
//    companion object{
//        var memes: Boolean = true
//        var news: Boolean = true
//        var games: Boolean = true
//        var films: Boolean = true
//        var meal: Boolean = true
//        var books: Boolean = true
//        var animals: Boolean = true
//        var psychology: Boolean = true
//        var sciences: Boolean = true
//        var cartoons: Boolean = true
//        var perfumery: Boolean = true
//        var clothes: Boolean = true
//        var household_items: Boolean = true
//        var chancellery: Boolean = true
//        var gardening: Boolean = true
//
//    }
}