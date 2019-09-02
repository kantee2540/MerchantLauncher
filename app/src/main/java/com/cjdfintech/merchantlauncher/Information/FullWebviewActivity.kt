package com.cjdfintech.merchantlauncher.Information

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cjdfintech.merchantlauncher.R
import kotlinx.android.synthetic.main.activity_full_webview.*

class FullWebviewActivity : AppCompatActivity()  {

    private lateinit var webUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_webview)

        if (intent.hasExtra("webUrl")){
            webUrl = intent.getStringExtra("webUrl")
        }else{

        }

        val webView = full_web_view
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.loadUrl(webUrl)

        full_web_view_toolbar.setNavigationOnClickListener {
            finish()
        }

        full_web_view_refresh.setOnRefreshListener {
            webView.reload()
        }


        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                full_web_view_toolbar.title = url
                full_web_view_refresh.isRefreshing = false
            }
        }
    }
}
