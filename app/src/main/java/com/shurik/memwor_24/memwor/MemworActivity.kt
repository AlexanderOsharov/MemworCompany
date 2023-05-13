package com.shurik.memwor_24.memwor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.shurik.memwor_22.fragments.VkFragment
import com.shurik.memwor_24.DopActivity
import com.shurik.memwor_24.databinding.MemworMainBinding
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.BrowserActivity
import com.shurik.memwor_24.memwor.fragments.InDevelopmentFragment
import com.shurik.memwor_24.memwor.fragments.RedditFragment
import com.shurik.memwor_24.memwor.fragments.TelegramFragment
import com.shurik.memwor_24.browser.fragments_all.adapter.ViewPagerAdapter

class MemworActivity : AppCompatActivity() {
    private lateinit var binding: MemworMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

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
        VkFragment.newInstance(),
        TelegramFragment.newInstance(),
        RedditFragment.newInstance(),
        InDevelopmentFragment.newInstance(),
    )
    private val fragListTitles = listOf(
        "Vk",
        "Telegram",
        "Reddit",
        "TikTok",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MemworMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //VK.initialize(this)

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
        val navigationView = binding.navigationView
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
        toggle.syncState()

        val adapter = ViewPagerAdapter(this, fragList) // создаем адаптер и передаем список фрагментов
        binding.vp23.adapter = adapter // устанавливаем адаптер для ViewPager2
        TabLayoutMediator(binding.ourtablayout2, binding.vp23) { tab, pos ->
            tab.text = fragListTitles[pos]
        }.attach()
    }
}