package com.cjdfintech.merchantlauncher.Information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cjdfintech.merchantlauncher.BuildConfig
import com.cjdfintech.merchantlauncher.R
import kotlinx.android.synthetic.main.fragment_promotion.view.*

class PromotionFragment2 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_promotion2, container, false)

        val webView = rootView.webView
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.loadUrl(BuildConfig.URL2)

        return rootView


    }
}
