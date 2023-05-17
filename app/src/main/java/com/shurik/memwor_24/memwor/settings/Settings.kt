package com.shurik.memwor_24.memwor.settings

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shurik.memwor_24.R
import com.shurik.memwor_24.browser.BrowserActivity
import com.shurik.memwor_24.databinding.ActivitySettingsBinding
import com.shurik.memwor_24.memwor.MemworActivity
import com.shurik.memwor_24.memwor.content.new_platform.NewPlatformActivity

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    var dialog: Dialog? = null
    var btnSettings: Button? = null

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

        btnSettings = findViewById<Button>(R.id.close_)
        dialog = Dialog(this)
        btnSettings!!.setOnClickListener { showDialog() }

        mainFab = binding.fab
        tape = binding.tape
        database = binding.database
        browser = binding.browser

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
    }
    fun showDialog() {
        val btnClose: Button
        dialog?.setContentView(R.layout.about_us_pop_up)
        dialog!!.show()
        btnClose = dialog!!.findViewById(R.id.close_)
        btnClose.setOnClickListener { dialog!!.dismiss() }
    }

    private fun showFabsWithAnimation() {
        val tapeAnimator = createFabAnimator(tape, 0f, -200f)
        val databaseAnimator = createFabAnimator(database, 0f, -200f)
        val browserAnimator = createFabAnimator(browser, 0f, -200f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapeAnimator, databaseAnimator, browserAnimator)
        animatorSet.start()

        isFabMenuOpen = true
    }

    private fun hideFabsWithAnimation() {
        val tapeAnimator = createFabAnimator(tape, -200f, 0f)
        val databaseAnimator = createFabAnimator(database, -200f, 0f)
        val browserAnimator = createFabAnimator(browser, -200f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapeAnimator, databaseAnimator, browserAnimator)
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