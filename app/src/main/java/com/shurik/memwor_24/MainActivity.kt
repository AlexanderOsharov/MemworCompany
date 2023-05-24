package com.shurik.memwor_24

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.shurik.memwor_24.browser.BrowserActivity
import com.shurik.memwor_24.memwor.MemworActivity

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        openSelectedActivity()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "start_activity_preference") {
            openSelectedActivity()
        }
    }

    private fun openSelectedActivity() {
        val selectedActivity = sharedPreferences.getString(
            "start_activity_preference",
            getString(R.string.default_start_activity)
        )

        val activityClass = when (selectedActivity) {
            "memwor_activity" -> MemworActivity::class.java
            "browser_activity" -> BrowserActivity::class.java
            else -> MemworActivity::class.java
        }

        startActivity(Intent(this, activityClass))
        finish()
    }
}