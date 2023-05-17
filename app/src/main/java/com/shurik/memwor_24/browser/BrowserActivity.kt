package com.shurik.memwor_24.browser

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shurik.memwor_24.DopActivity
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.fragments_all.RedditAllFragment
import com.shurik.memwor_24.browser.fragments_all.TelegramAllFragment
import com.shurik.memwor_24.browser.fragments_all.TikTokAllFragment
import com.shurik.memwor_24.browser.fragments_all.VkAllFragment
import com.shurik.memwor_24.browser.fragments_all.adapter.ViewPagerAdapter
import com.shurik.memwor_24.browser.new_fragment.CustomWebViewFragment
import com.shurik.memwor_24.databinding.AddNameDialogBinding
import com.shurik.memwor_24.databinding.AddUrlDialogBinding
import com.shurik.memwor_24.databinding.BrowserMainBinding
import com.shurik.memwor_24.memwor.MemworActivity
import com.shurik.memwor_24.memwor.content.new_platform.NewPlatformActivity
import com.shurik.memwor_24.memwor.settings.Settings
import kotlinx.android.synthetic.main.browser_main.*


class BrowserActivity : DopActivity() {

    private lateinit var binding: BrowserMainBinding
    private lateinit var toolbar: Toolbar

    private lateinit var mainFab: FloatingActionButton
    private lateinit var category: FloatingActionButton
    private lateinit var database: FloatingActionButton
    private lateinit var tape: FloatingActionButton
    private lateinit var btnAddFragment: FloatingActionButton

    private var isFabMenuOpen = false

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

        toolbar = binding.toolbar
        setActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        // Обработка клика на стрелку назад
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        //VK.initialize(this)

//        binding.vp2.setPageTransformer(ZoomOutPageTransformer())
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
//        val sharedPreferences = getSharedPreferences("tabs", MODE_PRIVATE)
//        val tabCount = sharedPreferences.getInt("tab_count", 0)
//
//        if (tabCount == 0) {
            val adapter =
                ViewPagerAdapter(this, fragList) // создаем адаптер и передаем список фрагментов
            binding.vp2.adapter = adapter // устанавливаем адаптер для ViewPager2
            binding.vp2.isUserInputEnabled = false
            TabLayoutMediator(binding.ourtablayout, binding.vp2) { tab, pos ->
                tab.text = fragListTitles[pos]
            }.attach()
//        }
//        else {
//            fragList.clear()
//            fragListTitles.clear()
//            val adapter =
//                ViewPagerAdapter(this, fragList) // создаем адаптер и передаем список фрагментов
//            binding.vp2.adapter = adapter // устанавливаем адаптер для ViewPager2
//            binding.vp2.isUserInputEnabled = false
//            restoreTabs()
//        }


        mainFab = binding.fab
        tape = binding.tape
        category = binding.category
        database = binding.database
        btnAddFragment = binding.btnAddFragment

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

        tape.setOnClickListener {
            val intent = Intent(this, MemworActivity::class.java)
            startActivity(intent)
        }
        btnAddFragment.setOnClickListener { showAddFragmentDialog() }

        // Включаем поддержку ActionBar и устанавливаем кнопку "назад"
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

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

    override fun onDestroy() {
        super.onDestroy()
//        saveTabs()
    }

    override fun onStop() {
        super.onStop()
//        saveTabs()
    }

    // Обработка нажатия на кнопку "назад"
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

        val dialogBinding = AddNameDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Редактировать вкладку")
            .setView(dialogBinding.root)
            .setPositiveButton("Сохранить") { _, _ ->
//                val url = dialogBinding.websiteUrlInput.text.toString()
                val title = dialogBinding.websiteTitleInput.text.toString()
                if (title.isNotBlank()) {
                    updateTab(index, title)
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

    private fun updateTab(index: Int, title: String) {
//        val updatedFragment = CustomWebViewFragment.newInstance(url)
//        fragList[index] = updatedFragment
        fragListTitles[index] = title
        binding.vp2.adapter?.notifyDataSetChanged()
    }

    private fun removeTab(tabTitle: String) {
        val index = fragListTitles.indexOf(tabTitle)
        fragListTitles.removeAt(index)
        fragList.removeAt(index)
        binding.vp2.adapter?.notifyDataSetChanged()
    }

//    private var isTabsRestored = false
//
//    private fun saveTabs() {
//        val sharedPreferences = getSharedPreferences("tabs", MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.clear()
//        editor.putInt("tab_count", binding.ourtablayout.tabCount)
//        for (i in 0 until binding.ourtablayout.tabCount) {
//            editor.putString("tab_$i", binding.ourtablayout.getTabAt(i)?.text.toString())
//            // сохранить информацию о фрагменте
//        }
//        editor.apply()
//    }
//
//    private fun restoreTabs() {
//        if (isTabsRestored) return
//
//        val sharedPreferences = getSharedPreferences("tabs", MODE_PRIVATE)
//        val tabCount = sharedPreferences.getInt("tab_count", 0)
//
//        for (i in 0 until tabCount) {
//            val tabTitle = sharedPreferences.getString("tab_$i", "")
//            binding.ourtablayout.addTab(
//                binding.ourtablayout.newTab().setText(tabTitle)
//            )
//            // восстановить информацию о фрагменте и добавить его в список fragList
//        }
//        val adapter = ViewPagerAdapter(this, fragList) // создаем адаптер и передаем список фрагментов
//        binding.vp2.adapter = adapter // устанавливаем адаптер для ViewPager2
//
//        isTabsRestored = true
//    }
private fun showFabsWithAnimation() {
    val btnAddFragmentAnimator = createFabAnimator(btnAddFragment, 0f, -200f)
    val categoryAnimator = createFabAnimator(category, 0f, -400f)
    val databaseAnimator = createFabAnimator(database, 0f, -600f)
    val tapeAnimator = createFabAnimator(tape, 0f, -800f)

    val rotationAnimator = ObjectAnimator.ofFloat(mainFab, "rotation", 0f, 45f)
    rotationAnimator.duration = 300

    val animatorSet = AnimatorSet()
    animatorSet.playTogether(btnAddFragmentAnimator, tapeAnimator, categoryAnimator, databaseAnimator, rotationAnimator)
    animatorSet.start()

    isFabMenuOpen = true
}

    private fun hideFabsWithAnimation() {
        val btnAddFragmentAnimator = createFabAnimator(btnAddFragment, -200f, 0f)
        val categoryAnimator = createFabAnimator(category, -400f, 0f)
        val databaseAnimator = createFabAnimator(database, -600f, 0f)
        val tapeAnimator = createFabAnimator(tape, -800f, 0f)

        val rotationAnimator = ObjectAnimator.ofFloat(mainFab, "rotation", 45f, 0f)
        rotationAnimator.duration = 300

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(btnAddFragmentAnimator, tapeAnimator, categoryAnimator, databaseAnimator, rotationAnimator)
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