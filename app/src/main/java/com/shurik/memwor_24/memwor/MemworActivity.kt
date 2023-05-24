package com.shurik.memwor_24.memwor

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.shurik.memwor_24.databinding.MemworMainBinding
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.BrowserActivity
import com.shurik.memwor_24.browser.fragments_all.adapter.ViewPagerAdapter
import com.shurik.memwor_24.memwor.content.new_platform.NewPlatformActivity
import com.shurik.memwor_24.memwor.fragments.*
import com.shurik.memwor_24.memwor.settings.Settings

class MemworActivity : AppCompatActivity() {
    private lateinit var binding: MemworMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var mainFab: FloatingActionButton
    private lateinit var category: FloatingActionButton
    private lateinit var database: FloatingActionButton
    private lateinit var browser: FloatingActionButton

    private var isFabMenuOpen = false

    private fun Context.startActivityWithAnimation(intent: Intent) {
        startActivity(intent)
        if (this is MemworActivity || this is BrowserActivity) {
            (this as AppCompatActivity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    private val fragList = listOf(
        AllFragment.newInstance(),
//        VkFragment.newInstance(),
//        TelegramFragment.newInstance(),
//        RedditFragment.newInstance(),
//        InDevelopmentFragment.newInstance(),
    )
    private val fragListTitles = listOf(
        "All",
//        "Vk",
//        "Telegram",
//        "Reddit",
//        "TikTok",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MemworMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //VK.initialize(this)
        mainFab = binding.fab
        category = binding.category
        database = binding.database
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
            overridePendingTransition(R.anim.browser_out_left, R.anim.browser_in_right)
        }

        database.setOnClickListener {
            val intent = Intent(this, NewPlatformActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.browser_out_left, R.anim.browser_in_right)
        }

        browser.setOnClickListener {
            val intent = Intent(this, BrowserActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.browser_out_left, R.anim.browser_in_right)
        }

        binding.vp23.setPageTransformer(ZoomOutPageTransformer())
        /*toggleSwitch = binding.toggleSwitch
//        toggleSwitch.setOnClickListener {
//            toggleSwitch.isChecked = !toggleSwitch.isChecked
//            if (toggleSwitch.isChecked) {
//                val intent = Intent(this, SecondActivity::class.java)
//                startActivityWithAnimation(intent)
//            } else {
//                // Здесь код, который будет выполнен, когда toggle будет выключен
//            }
//        }
        toggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val intent = Intent(this, BrowserActivity::class.java)
                startActivityWithAnimation(intent)
            } else {
                // Действия при выключении тогла
            }
        }*/

        val drawerLayout = binding.drawerLayout
        /*val navigationView = binding.navigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.firstActivity -> {
                    startActivityWithAnimation(Intent(this, BrowserActivity::class.java))
                }
                R.id.secondActivity -> {
                    startActivityWithAnimation(Intent(this, BrowserActivity::class.java))
                }
                // Add more menu items handling...
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Add the toggle action for the drawer
        setSupportActionBar(binding.toolBar2)
        toggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolBar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()*/

        val adapter = ViewPagerAdapter(this, fragList) // создаем адаптер и передаем список фрагментов
        binding.vp23.adapter = adapter // устанавливаем адаптер для ViewPager2
        TabLayoutMediator(binding.ourtablayout2, binding.vp23) { tab, pos ->
            tab.text = fragListTitles[pos]
        }.attach()

    }

    private fun showFabsWithAnimation() {
        val categoryAnimator = createFabAnimator(category, 0f, -200f,0f, 1f)
        val databaseAnimator = createFabAnimator(database, 0f, -200f,0f, 1f)
        val browserAnimator = createFabAnimator(browser, 0f, -200f,0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(categoryAnimator, databaseAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = true
        category.isEnabled = true
        browser.isEnabled = true
        database.isEnabled = true
    }

    private fun hideFabsWithAnimation() {
        val categoryAnimator = createFabAnimator(category, -200f, 0f, 1f, 0f)
        val databaseAnimator = createFabAnimator(database, -200f, 0f, 1f, 0f)
        val browserAnimator = createFabAnimator(browser, -200f, 0f, 1f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(categoryAnimator, databaseAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = false
        category.isEnabled = false
        browser.isEnabled = false
        database.isEnabled = false
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