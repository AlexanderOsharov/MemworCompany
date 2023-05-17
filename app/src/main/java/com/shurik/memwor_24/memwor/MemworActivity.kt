package com.shurik.memwor_24.memwor

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.shurik.memwor_24.databinding.MemworMainBinding
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.BrowserActivity
import com.shurik.memwor_24.browser.fragments_all.adapter.ViewPagerAdapter
import com.shurik.memwor_24.memwor.content.new_platform.NewPlatformActivity
import com.shurik.memwor_24.memwor.fragments.*
import com.shurik.memwor_24.memwor.settings.Settings
import kotlinx.android.synthetic.main.browser_main.*

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
        VkFragment.newInstance(),
//        TelegramFragment.newInstance(),
        RedditFragment.newInstance(),
//        InDevelopmentFragment.newInstance(),
    )
    private val fragListTitles = listOf(
        "All",
        "Vk",
//        "Telegram",
        "Reddit",
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

        database.setOnClickListener {
            val intent = Intent(this, NewPlatformActivity::class.java)
            startActivity(intent)
        }

        browser.setOnClickListener {
            val intent = Intent(this, BrowserActivity::class.java)
            startActivity(intent)
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

//        val fab = binding.fab
//        fab.setOnClickListener {
//            startActivityWithAnimation(Intent(this, BrowserActivity::class.java))
//        }
    }
    private fun showFabsWithAnimation() {
        val categoryAnimator = createFabAnimator(category, 0f, -200f)
        val databaseAnimator = createFabAnimator(database, 0f, -200f)
        val browserAnimator = createFabAnimator(browser, 0f, -200f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(categoryAnimator, databaseAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = true
    }

    private fun hideFabsWithAnimation() {
        val categoryAnimator = createFabAnimator(category, -200f, 0f)
        val databaseAnimator = createFabAnimator(database, -200f, 0f)
        val browserAnimator = createFabAnimator(browser, -200f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(categoryAnimator, databaseAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = false
    }

    private fun createFabAnimator(fab: FloatingActionButton, fromYDelta: Float, toYDelta: Float): ObjectAnimator {
        fab.visibility = View.VISIBLE

        val animator = ObjectAnimator.ofFloat(fab, "translationY", fromYDelta, toYDelta)
        animator.duration = 300

        return animator
    }
}