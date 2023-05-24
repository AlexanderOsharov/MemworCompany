package com.shurik.memwor_24.memwor.content.module_adapter

import android.content.Context
import android.content.Intent
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import com.shurik.memwor_24.browser.BrowserActivity

class CustomClickableSpan(private val url: String, private val context: Context) : ClickableSpan() {

    override fun onClick(widget: View) {
        val intent = Intent(context, BrowserActivity::class.java)
        intent.putExtra("url", url)
        Toast.makeText(context, "Нажата ссылка: $url", Toast.LENGTH_SHORT).show()

        // Запускаем BrowserActivity
        context.startActivity(intent)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}
