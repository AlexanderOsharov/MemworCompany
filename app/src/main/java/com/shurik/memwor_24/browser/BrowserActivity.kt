package com.shurik.memwor_24.browser

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shurik.memwor_24.DopActivity
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.fragments_all.adapter.ViewPagerAdapter
import com.shurik.memwor_24.browser.new_fragment.CustomWebViewFragment
import com.shurik.memwor_24.browser.new_fragment.FragmentInfo
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

    private val fragList: MutableList<Fragment> = mutableListOf()
    private val fragListTitles: MutableList<String> = mutableListOf()
    private val fragmentInfoList: MutableList<FragmentInfo> = mutableListOf()

    var isLongClick = false
    private var lockMainFabRunnable: Runnable? = null
    private fun scheduleLockMainFab() {
        lockMainFabRunnable = Runnable {
            startLockAnimation()
            mainFab.alpha = 0.5f
            isLongClick = false
        }
        mainFab.postDelayed(lockMainFabRunnable, 5000) // время бездействия перед блокировкой (например, 5000 мс)
    }

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

        loadFragmentsFromSharedPreferences()

        // проверяем, первый ли раз пользователь запускает приложение
        val isFirstLaunch = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            .getBoolean("isFirstLaunch", true)
        if (isFirstLaunch || fragList.isEmpty()) {
            // добавляем фрагменты в список
            addCustomFragment("https://vk.com/login", "Vk")
            addCustomFragment("https://web.telegram.org/k/", "Telegram")
            addCustomFragment("https://www.reddit.com/", "Reddit")
            addCustomFragment("https://www.tiktok.com/", "TikTok")

            // устанавливаем флаг первого запуска в false, чтобы не добавлять фрагменты в дальнейшем
            val editor = getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
            editor.putBoolean("isFirstLaunch", false)
            editor.apply()
        }

        val url = intent.getStringExtra("url")
        if (url != null) {
            addCustomFragment(url, url)
        }

        // создаем адаптер и передаем список фрагментов
        val adapter = ViewPagerAdapter(this, fragList)
        binding.vp2.adapter = adapter
        binding.vp2.isUserInputEnabled = false

        TabLayoutMediator(binding.ourtablayout, binding.vp2) { tab, pos ->
            tab.text = fragListTitles[pos]
        }.attach()


        mainFab = binding.fab
        tape = binding.tape
        category = binding.category
        database = binding.database
        btnAddFragment = binding.btnAddFragment

        mainFab.apply {
            alpha = 0.5f
            isLongClick = false
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
                        scheduleLockMainFab()
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
            overridePendingTransition(R.anim.browser_in_right, R.anim.browser_out_left)
        }

        database.setOnClickListener {
            val intent = Intent(this, NewPlatformActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.browser_in_right, R.anim.browser_out_left)
        }

        tape.setOnClickListener {
            val intent = Intent(this, MemworActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.browser_in_right, R.anim.browser_out_left)
        }

        btnAddFragment.setOnClickListener { showAddFragmentDialog() }

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

        // Включаем поддержку ActionBar и устанавливаем кнопку "назад"
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        requestPermissions()
        checkAndRequestPermissions()
    }

    private val REQUEST_PERMISSIONS = 1

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissions, 1)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Разрешения предоставлены, можно выполнять нужные операции
                } else {
                    // Разрешения не предоставлены, уведомляем об этом сообщением пользователю или обработываем отказ
                    Toast.makeText(this, "Вы не предоставили некоторые разрешения", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Учтите, что из-за этого Вам могут быть недоступны особые функции", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val microphonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (microphonePermission != PackageManager.PERMISSION_GRANTED || storagePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
        } else {
            // Разрешения уже предоставлены, можно выполнять нужные операции
        }
    }

    private fun saveFragmentsToSharedPreferences() {
        val sharedPreferences = getSharedPreferences("fragments", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(fragmentInfoList)
        editor.putString("fragmentInfoList", json)
        editor.apply()
    }

    private fun loadFragmentsFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("fragments", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("fragmentInfoList", null)
        if (json != null) {
            val type = object : TypeToken<List<FragmentInfo>>() {}.type
            val loadedFragmentInfoList: List<FragmentInfo> = gson.fromJson(json, type)
            for (info in loadedFragmentInfoList) {
                addCustomFragment(info.url, info.title)
            }
        }
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

    private fun addCustomFragment(url: String, title: String) {
        val newFragment = CustomWebViewFragment.newInstance(url)
        fragList.add(newFragment)
        fragListTitles.add(title)
        fragmentInfoList.add(FragmentInfo(url, title))
        binding.vp2.adapter?.notifyDataSetChanged()

        saveFragmentsToSharedPreferences()
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

        saveFragmentsToSharedPreferences()
    }

    private fun removeTab(tabTitle: String) {
        val index = fragListTitles.indexOf(tabTitle)
        fragmentInfoList.removeAt(index)
        fragListTitles.removeAt(index)
        fragList.removeAt(index)

        val adapter = ViewPagerAdapter(this, fragList)
        binding.vp2.adapter = adapter
        saveFragmentsToSharedPreferences()
    }

    private fun showFabsWithAnimation() {
        val btnAddFragmentAnimator = createFabAnimator(btnAddFragment, 0f, -200f, 0f, 1f)
        val categoryAnimator = createFabAnimator(category, 0f, -400f, 0f, 1f)
        val databaseAnimator = createFabAnimator(database, 0f, -600f, 0f, 1f)
        val tapeAnimator = createFabAnimator(tape, 0f, -800f, 0f, 1f)

        val rotationAnimator = ObjectAnimator.ofFloat(mainFab, "rotation", 0f, 45f)
        rotationAnimator.duration = 300

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(btnAddFragmentAnimator, tapeAnimator, categoryAnimator, databaseAnimator, rotationAnimator)
        animatorSet.start()

        isFabMenuOpen = true
        btnAddFragment.isEnabled = true
        category.isEnabled = true
        database.isEnabled = true
        tape.isEnabled = true
    }

    private fun hideFabsWithAnimation() {
        val btnAddFragmentAnimator = createFabAnimator(btnAddFragment, -200f, 0f, 1f, 0f)
        val categoryAnimator = createFabAnimator(category, -400f, 0f, 1f, 0f)
        val databaseAnimator = createFabAnimator(database, -600f, 0f, 1f, 0f)
        val tapeAnimator = createFabAnimator(tape, -800f, 0f, 1f, 0f)

        val rotationAnimator = ObjectAnimator.ofFloat(mainFab, "rotation", 45f, 0f)
        rotationAnimator.duration = 300

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(btnAddFragmentAnimator, tapeAnimator, categoryAnimator, databaseAnimator, rotationAnimator)
        animatorSet.start()

        isFabMenuOpen = false
        btnAddFragment.isEnabled = false
        category.isEnabled = false
        database.isEnabled = false
        tape.isEnabled = false
    }

    private fun isPointInCircle(px: Float, py: Float, cx: Float, cy: Float, radius: Int): Boolean {
        val dx = px - cx
        val dy = py - cy
        return dx * dx + dy * dy <= radius * radius
    }

    private fun startUnlockAnimation() {
        val animator = ObjectAnimator.ofFloat(mainFab, "rotation", 0f, 360f)
        animator.duration = 500
        animator.start()

        mainFab.removeCallbacks(lockMainFabRunnable)
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