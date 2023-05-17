package com.shurik.memwor_24.memwor.content.new_platform

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.os.Bundle
import android.view.View.inflate
import android.widget.Button
import android.widget.EditText
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.BrowserActivity
import com.shurik.memwor_24.databinding.AboutUsPopUpBinding.inflate
import com.shurik.memwor_24.databinding.ActivityDopBinding.inflate
import com.shurik.memwor_24.databinding.ActivityNewPlatformBinding
import com.shurik.memwor_24.databinding.SliderLayoutBinding.inflate
import com.shurik.memwor_24.memwor.MemworActivity
import com.shurik.memwor_24.memwor.content.logic.db.MemworDatabaseManager
import com.shurik.memwor_24.memwor.settings.Settings
import kotlinx.android.synthetic.main.item.*

class NewPlatformActivity : AppCompatActivity() {

    private val dbManager = MemworDatabaseManager()
    private lateinit var binding: ActivityNewPlatformBinding

    private lateinit var mainFab: FloatingActionButton
    private lateinit var tape: FloatingActionButton
    private lateinit var category: FloatingActionButton
    private lateinit var browser: FloatingActionButton

    private var isFabMenuOpen = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPlatformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //VK.initialize(this)
        mainFab = binding.fab
        category = binding.category
        tape = binding.tape
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

        tape.setOnClickListener {
            val intent = Intent(this, MemworActivity::class.java)
            startActivity(intent)
        }

        browser.setOnClickListener {
            val intent = Intent(this, BrowserActivity::class.java)
            startActivity(intent)
        }

        val addButton: Button = findViewById(R.id.addBtn)
        addButton.setOnClickListener {
            addDBCommunity()
        }

    }

    fun addDBCommunity(){
        val platformText: EditText = findViewById(R.id.editPlatformText)
        val domainText: EditText = findViewById(R.id.editDomainText)
        val nameText: EditText = findViewById(R.id.editNameText)
        val categoryText: EditText = findViewById(R.id.editCategoryText)

        dbManager.addNewCommunity(platformText.text.toString(), domainText.text.toString(), nameText.text.toString(), categoryText.text.toString())

    }

    private fun showFabsWithAnimation() {
        val tapeAnimator = createFabAnimator(tape, 0f, -200f)
        val categoryAnimator = createFabAnimator(category, 0f, -200f)
        val browserAnimator = createFabAnimator(browser, 0f, -200f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapeAnimator, categoryAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = true
    }

    private fun hideFabsWithAnimation() {
        val tapeAnimator = createFabAnimator(tape, -200f, 0f)
        val categoryAnimator = createFabAnimator(category, -200f, 0f)
        val browserAnimator = createFabAnimator(browser, -200f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapeAnimator, categoryAnimator, browserAnimator)
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