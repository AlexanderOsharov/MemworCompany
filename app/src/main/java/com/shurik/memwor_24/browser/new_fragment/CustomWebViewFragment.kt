package com.shurik.memwor_24.browser.new_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.shurik.memwor_24.databinding.CustomWebViewFragmentBinding

class CustomWebViewFragment : Fragment() {

    private lateinit var binding: CustomWebViewFragmentBinding

    companion object {
        private const val ARG_URL = "url"

        fun newInstance(url: String): CustomWebViewFragment {
            return CustomWebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CustomWebViewFragmentBinding.inflate(inflater, container, false)
        val url = arguments?.getString(ARG_URL)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
        url?.let { binding.webView.loadUrl(url) }
        return binding.root
    }

    fun newUrl(url: String){
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
        url?.let { binding.webView.loadUrl(url) }
    }
}
