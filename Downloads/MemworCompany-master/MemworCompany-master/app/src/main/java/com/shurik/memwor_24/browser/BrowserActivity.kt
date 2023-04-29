package com.shurik.memwor_24.browser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.shurik.memwor_24.DopActivity
import com.shurik.memwor_24.memwor.MemworActivity
import com.shurik.memwor_24.databinding.BrowserMainBinding
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.fragments_all.RedditAllFragment
import com.shurik.memwor_24.browser.fragments_all.TelegramAllFragment
import com.shurik.memwor_24.browser.fragments_all.TikTokAllFragment
import com.shurik.memwor_24.browser.fragments_all.VkAllFragment
import com.shurik.memwor_24.browser.fragments_all.adapter.ViewPagerAdapter

class BrowserActivity : DopActivity() {

    private lateinit var binding: BrowserMainBinding

    private fun Context.startActivityWithAnimation(intent: Intent) {
        startActivity(intent)
        if (this is MemworActivity || this is BrowserActivity) {
            (this as AppCompatActivity).overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
    }

    private val fragList = listOf(
        VkAllFragment.newInstance(),
        TelegramAllFragment.newInstance(),
        RedditAllFragment.newInstance(),
        TikTokAllFragment.newInstance(),
    )
    private val fragListTitles = listOf(
        "Vk",
        "Telegram",
        "Reddit",
        "TikTok",
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BrowserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //VK.initialize(this)

        binding.vp2.setPageTransformer(ZoomOutPageTransformer())
        /*toggleSwitch = binding.toggleSwitch
        toggleSwitch.setOnClickListener {
            toggleSwitch.isChecked = !toggleSwitch.isChecked
            if (toggleSwitch.isChecked) {
                val intent = Intent(this, MemworActivity::class.java)
                startActivityWithAnimation(intent)
            } else {
                // Здесь код, который будет выполнен, когда toggle будет выключен
            }
        }*/

        val adapter = ViewPagerAdapter(this, fragList) // создаем адаптер и передаем список фрагментов
        binding.vp2.adapter = adapter // устанавливаем адаптер для ViewPager2
        TabLayoutMediator(binding.ourtablayout, binding.vp2) { tab, pos ->
            tab.text = fragListTitles[pos]
        }.attach()
    }

}