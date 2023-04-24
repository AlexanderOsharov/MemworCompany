package com.shurik.memwor_24.browser.fragments_all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.shurik.memwor_24.R

class TelegramAllFragment : Fragment() {

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_telegram_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById(R.id.vk_web_view)
        webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null && url.startsWith("https://web.telegram.org/k/")) {
                        webView.loadUrl(url)
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
            loadUrl("https://web.telegram.org/k/")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TelegramAllFragment()
    }
}