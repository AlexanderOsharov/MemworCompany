package com.shurik.memwor_24.memwor.content.html

import android.annotation.SuppressLint

import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView

class HtmlAdapter(private var urls: List<String>) :
    RecyclerView.Adapter<HtmlAdapter.HtmlViewHolder>() {

    class HtmlViewHolder(val webView: WebView) : RecyclerView.ViewHolder(webView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HtmlViewHolder {
        val webView = WebView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            setOnTouchListener { _, event -> event.action == MotionEvent.ACTION_MOVE }
        }
        return HtmlViewHolder(webView)
    }

    private inner class CustomWebViewClient(private val url: String) : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            if (request?.url != null) {
                view?.loadUrl(request.url.toString())
            }
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            val script = "javascript:(function() { " +
                    "var bodyWrapper = document.querySelector('.tgme_body_wrap'); " +
                    "document.documentElement.innerHTML = bodyWrapper.outerHTML; " +
                    "})()"
            view?.evaluateJavascript(script, null)
        }
    }


    override fun onBindViewHolder(holder: HtmlViewHolder, position: Int) {
        val webView = holder.webView
        webView.webViewClient = CustomWebViewClient(urls[position])
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(urls[position])
    }

    override fun getItemCount(): Int {
        return urls.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateUrls(newUrls: List<String>){
        urls = newUrls
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addUrls(newUrls: List<String>) {
        val previousSize = itemCount
        urls += newUrls
        notifyItemRangeInserted(previousSize, newUrls.size)
    }
}

