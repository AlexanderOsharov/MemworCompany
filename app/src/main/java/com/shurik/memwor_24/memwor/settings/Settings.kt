package com.shurik.memwor_24.memwor.settings

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.preference.PreferenceFragmentCompat
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

    var startAnimButton: FloatingActionButton? = null
    private lateinit var gear1: ImageView
    private lateinit var gear2: ImageView
    private lateinit var gear1Rotation: ObjectAnimator
    private lateinit var gear2Rotation: ObjectAnimator
    private lateinit var translateYAnimator: ObjectAnimator
    private lateinit var walking2ManAnimation: AnimationDrawable
    var walking2Man: ImageView? = null
    private lateinit var walkingManAnimation: AnimationDrawable
    var walkingMan: ImageView? = null
    private var isAnimationRunning = false

    private lateinit var memworRadioButton: RadioButton
    private lateinit var browserRadioButton: RadioButton

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

//        memworRadioButton = binding.memworRadioButton
//        browserRadioButton = binding.browserRadioButton
//
//        // Проверяем, какая активность была выбрана ранее
//        val selectedActivity = getSelectedActivity()
//
//        // Устанавливаем radiobutton, соответствующий выбранной активности, в состояние checked
//        if (selectedActivity == "memwor_activity") {
//            memworRadioButton.isChecked = true
//        } else {
//            browserRadioButton.isChecked = true
//        }

        // Анимация выдвижения
        val cardView = binding.cardView
        translateYAnimator = ObjectAnimator.ofFloat(cardView, "translationY", -200f, 0f)
        translateYAnimator.duration = 500 // продолжительность анимации в миллисекундах
        translateYAnimator.start()

        walkingMan = binding.walkingMan
        walking2Man = binding.walking2Man
        gear1 = binding.gear1
        gear2 = binding.gear2
        isAnimationRunning = loadAnimationState()

        walkingManAnimation = walkingMan!!.drawable as AnimationDrawable
        walking2ManAnimation = walking2Man!!.drawable as AnimationDrawable
        gear1Rotation = ObjectAnimator.ofFloat(gear1, "rotation", 0f, 360f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }

        gear2Rotation = ObjectAnimator.ofFloat(gear2, "rotation", 360f, 0f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }

        toggleAnimation()

        startAnimButton = binding.anim
        startAnimButton!!.setOnClickListener {
            toggleAnimation()
        }

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
        supportFragmentManager
            .beginTransaction()
            .replace(binding.settingsContainer.id, SettingsFragment())
            .commit()
    }

    // Функция для запуска и остановки анимации
    private fun toggleAnimation() {
        if (isAnimationRunning) {
            // Остановить анимацию
            startAnimButton?.setImageResource(R.drawable.off)
            walkingMan!!.isVisible = false
            walking2Man!!.isVisible = false

            gear1.isVisible = false
            gear2.isVisible = false

            // Остановка анимаций
            gear1Rotation.cancel()
            gear2Rotation.cancel()

            walkingManAnimation.stop()
            walking2ManAnimation.stop()
        } else {
            // Запустить анимацию
            startAnimButton?.setImageResource(R.drawable.on)

            walkingManAnimation.start()
            walkingMan!!.isVisible = true

            val viewTreeObserver = binding.root.viewTreeObserver
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val maxX = binding.root.width - walkingMan!!.width + 0f
                    val maxY = binding.root.height - walkingMan!!.height + 0f

                    startAnimation("walkingMan", maxX, maxY)
                }
            })

            walking2ManAnimation.start()
            walking2Man!!.isVisible = true
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val maxX = binding.root.width - walking2Man!!.width + 0f
                    val maxY = binding.root.height - walking2Man!!.height + 0f
                    startAnimation("walking2Man", maxX, maxY)
                }
            })

            gear1.isVisible = true
            gear2.isVisible = true

            gear1Rotation.start()
            gear2Rotation.start()
        }
        saveAnimationState(isAnimationRunning)
        isAnimationRunning = !isAnimationRunning
    }

    // Сохранение состояния анимации в SharedPreferences
    private fun saveAnimationState(state: Boolean) {
        val sharedPreferences = getSharedPreferences("animation_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isAnimationRunning", state)
        editor.apply()
    }

    // Загрузка состояния анимации из SharedPreferences
    private fun loadAnimationState(): Boolean {
        val sharedPreferences = getSharedPreferences("animation_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isAnimationRunning", false)
    }

    fun startAnimation(anim: String, maxX: Float, maxY: Float) {
        if (anim == "walkingMan") {
            val (randomX, randomY) = generateRandomCoordinates(maxX, maxY)

            // Здесь надо проверить пересечение с другими объектами и, если необходимо, измените координаты

            val xAnimator = walkingMan?.x?.let {
                ObjectAnimator.ofFloat(walkingMan, "x", it, randomX).apply {
                    duration = 2000
                    interpolator = LinearInterpolator()
                }
            }
            val yAnimator = walkingMan?.y?.let {
                ObjectAnimator.ofFloat(walkingMan, "y", it, randomY).apply {
                    duration = 2000
                    interpolator = LinearInterpolator()
                }
            }

            val animatorSet = AnimatorSet().apply {
                playSequentially(xAnimator, yAnimator)
            }
            animatorSet.doOnEnd {
                // Здесь можно проверить пересечение с другими объектами и изменить путь анимации при необходимости
                startAnimation("walkingMan", maxX, maxY)
            }
            animatorSet.start()
        }
        else if (anim == "walking2Man"){
            val (randomX, randomY) = generateRandomCoordinates(maxX, maxY)

            // Здесь надо проверить пересечение с другими объектами и, если необходимо, измените координаты

            val xAnimator = walking2Man?.x?.let {
                ObjectAnimator.ofFloat(walking2Man, "x", it, randomX).apply {
                    duration = 2000
                    interpolator = LinearInterpolator()
                }
            }
            val yAnimator = walking2Man?.y?.let {
                ObjectAnimator.ofFloat(walking2Man, "y", it, randomY).apply {
                    duration = 2000
                    interpolator = LinearInterpolator()
                }
            }

            val animatorSet = AnimatorSet().apply {
                playSequentially(xAnimator, yAnimator)
            }
            animatorSet.doOnEnd {
                // Здесь можно проверить пересечение с другими объектами и изменить путь анимации при необходимости
                startAnimation("walking2Man", maxX, maxY)
            }
            animatorSet.start()
        }
    }

    fun generateRandomCoordinates(maxX: Float, maxY: Float): Pair<Float, Float> {
        val randomX = (0..maxX.toInt()).random().toFloat()
        val randomY = (0..maxY.toInt()).random().toFloat()
        return Pair(randomX, randomY)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }

    fun onOkButtonClick(view: View) {
        // Сохраняем выбранную активность
        val selectedActivity = if (memworRadioButton.isChecked) 0 else 1
        saveSelectedActivity(selectedActivity)
        finish()
    }

    private fun saveSelectedActivity(selectedItem: Int) {
        val activityValues = arrayOf("memwor_activity", "browser_activity")

        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("start_activity_preference", activityValues[selectedItem])
            apply()
        }
    }

    private fun getSelectedActivity(): String {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("start_activity_preference", "memwor_activity") ?: "memwor_activity"
    }

//    fun showDialog() {
//        val btnClose: Button
//        dialog?.setContentView(R.layout.about_us_pop_up)
//        dialog!!.show()
//        btnClose = dialog!!.findViewById(R.id.close_)
//        btnClose.setOnClickListener { dialog!!.dismiss() }
//    }

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

    private fun showFabsWithAnimation() {
        val tapeAnimator = createFabAnimator(tape, 0f, -200f,0f, 1f)
        val databaseAnimator = createFabAnimator(database, 0f, -200f,0f, 1f)
        val browserAnimator = createFabAnimator(browser, 0f, -200f,0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapeAnimator, databaseAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = true
        database.isEnabled = true
        browser.isEnabled = true
        tape.isEnabled = true
    }

    private fun hideFabsWithAnimation() {
        val tapeAnimator = createFabAnimator(tape, -200f, 0f, 1f, 0f)
        val databaseAnimator = createFabAnimator(database, -200f, 0f, 1f, 0f)
        val browserAnimator = createFabAnimator(browser, -200f, 0f, 1f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapeAnimator, databaseAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = false
        database.isEnabled = false
        browser.isEnabled = false
        tape.isEnabled = false
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