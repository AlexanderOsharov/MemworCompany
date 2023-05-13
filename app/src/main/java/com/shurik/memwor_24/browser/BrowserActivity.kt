package com.shurik.memwor_24.browser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
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
import com.shurik.memwor_24.browser.new_fragment.CustomWebViewFragment
import com.shurik.memwor_24.databinding.AddUrlDialogBinding
import java.net.HttpURLConnection
import java.net.URL

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

    private val fragList = mutableListOf(
        VkAllFragment.newInstance(),
        TelegramAllFragment.newInstance(),
        RedditAllFragment.newInstance(),
        TikTokAllFragment.newInstance(),
    )
    private val fragListTitles = mutableListOf(
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

        binding.btnAddFragment.setOnClickListener { showAddFragmentDialog() }

//        binding.ourtablayout.setOnLongClickListener { v ->
//            if (v != null && v is TextView) {
//                showTabContextMenu(v)
//            }
//            true
//        }


        binding.ourtablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vp2.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                val textView = TextView(this@BrowserActivity)
                textView.text = tab.text
                showTabContextMenu(textView)
            }
        })


    }

    private fun showAddFragmentDialog() {
        val dialogBinding = AddUrlDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Добавить вкладку")
            .setView(dialogBinding.root)
            .setPositiveButton("Добавить") { _, _ ->
                val url = dialogBinding.websiteUrlInput.text.toString()
                val title = dialogBinding.websiteTitleInput.text.toString()
                if (url.isNotBlank() && title.isNotBlank()) {
                    addCustomFragment(url, title)
                } else {
                    Toast.makeText(this, "Введите URL и название", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

//    private fun addCustomFragment(url: String, title: String) {
//        val newFragment = CustomWebViewFragment.newInstance(url)
//        fragList += newFragment
//        fragListTitles += title
//        binding.vp2.adapter = ViewPagerAdapter(this, fragList)
//    }
    private fun addCustomFragment(url: String, title: String) {
        val newFragment = CustomWebViewFragment.newInstance(url)
        fragList.add(newFragment)
        fragListTitles.add(title)
        binding.vp2.adapter?.notifyDataSetChanged()
    }

    private fun showTabContextMenu(tabView: View) {
        val popup = PopupMenu(this, tabView)
        popup.menuInflater.inflate(R.menu.tab_context_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit_tab -> {
                    editTab((tabView as TextView).text.toString())
                }
                R.id.action_remove_tab -> {
                    removeTab((tabView as TextView).text.toString())
                }
            }
            true
        }

        popup.show()
    }

    private fun editTab(tabTitle: String) {
        val index = fragListTitles.indexOf(tabTitle)

        val dialogBinding = AddUrlDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Редактировать вкладку")
            .setView(dialogBinding.root)
            .setPositiveButton("Сохранить") { _, _ ->
                val url = dialogBinding.websiteUrlInput.text.toString()
                val title = dialogBinding.websiteTitleInput.text.toString()
                if (url.isNotBlank() && title.isNotBlank()) {
                    updateTab(index, url, title)
                } else {
                    Toast.makeText(this, "Введите URL и название", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        if (fragList[index] is CustomWebViewFragment) {
//            dialogBinding.websiteUrlInput.setText((fragList[index] as CustomWebViewFragment).url)
        }
        dialogBinding.websiteTitleInput.setText(tabTitle)

        dialog.show()
    }

    private fun updateTab(index: Int, url: String, title: String) {
        val updatedFragment = CustomWebViewFragment.newInstance(url)
        fragList[index] = updatedFragment
        fragListTitles[index] = title
        binding.vp2.adapter?.notifyDataSetChanged()
    }

    private fun removeTab(tabTitle: String) {
        val index = fragListTitles.indexOf(tabTitle)
        fragListTitles.removeAt(index)
        fragList.removeAt(index)
        binding.vp2.adapter?.notifyDataSetChanged()
    }

}