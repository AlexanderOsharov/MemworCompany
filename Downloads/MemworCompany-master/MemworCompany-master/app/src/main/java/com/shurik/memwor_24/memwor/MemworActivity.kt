package com.shurik.memwor_24.memwor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import com.google.android.material.tabs.TabLayoutMediator
import com.shurik.memwor_24.BaseActivity
import com.shurik.memwor_24.databinding.MemworMainBinding
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.BrowserActivity
import com.shurik.memwor_24.memwor.fragments.InDevelopmentFragment
import com.shurik.memwor_24.memwor.fragments.RedditFragment
import com.shurik.memwor_24.memwor.fragments.TelegramFragment
import com.shurik.memwor_24.browser.fragments_all.adapter.ViewPagerAdapter
import java.util.*

class MemworActivity : BaseActivity() {
    private lateinit var binding: MemworMainBinding

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
        InDevelopmentFragment.newInstance(),
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

        binding.vp2.setPageTransformer(ZoomOutPageTransformer())
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

        val adapter = ViewPagerAdapter(this, fragList) // создаем адаптер и передаем список фрагментов
        binding.vp2.adapter = adapter // устанавливаем адаптер для ViewPager2
        TabLayoutMediator(binding.ourtablayout, binding.vp2) { tab, pos ->
            tab.text = fragListTitles[pos]
        }.attach()
    }

    override fun getLayoutId(): Int = R.layout.memwor_main
}