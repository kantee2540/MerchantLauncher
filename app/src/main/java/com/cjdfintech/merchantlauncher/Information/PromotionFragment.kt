package com.cjdfintech.merchantlauncher.Information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cjdfintech.merchantlauncher.BuildConfig
import com.cjdfintech.merchantlauncher.R
import com.cjdfintech.merchantlauncher.RemoteConfig
import com.cjdfintech.merchantlauncher.RemoteConfigInterface
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.fragment_promotion.*
import kotlinx.android.synthetic.main.fragment_promotion.view.*

class PromotionFragment : Fragment(), RemoteConfigInterface {

    lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_promotion, container, false)

        RemoteConfig(this).fetchRemoteConfig()

        return rootView


    }

    override fun onSuccessFetchRemoteConfig(remoteConfig: FirebaseRemoteConfig) {
        rootView.cannot_load_layout.visibility = View.GONE
        rootView.webView.visibility = View.VISIBLE

        val webView = rootView.webView
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.loadUrl(remoteConfig.getString("web1"))
    }

    override fun onFailedFetchRemoteConfig() {
        rootView.cannot_load_layout.visibility = View.VISIBLE
        rootView.webView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        RemoteConfig(this).fetchRemoteConfig()
    }
}
