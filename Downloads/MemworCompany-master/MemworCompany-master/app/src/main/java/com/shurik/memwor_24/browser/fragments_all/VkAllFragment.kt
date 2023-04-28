package com.shurik.memwor_24.browser.fragments_all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.shurik.memwor_24.R

class VkAllFragment : Fragment() {

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vk_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById(R.id.vk_web_view)
        webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null && url.startsWith("https://vk.com/login")) {
                        webView.loadUrl(url)
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
            loadUrl("https://vk.com/login")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = VkAllFragment()
    }
}
