package com.cjdfintech.merchantlauncher.Information

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cjdfintech.merchantlauncher.BuildConfig
import com.cjdfintech.merchantlauncher.R
import com.cjdfintech.merchantlauncher.RemoteConfig
import com.cjdfintech.merchantlauncher.RemoteConfigInterface
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.fragment_promotion.view.*

class PromotionFragment : Fragment(), RemoteConfigInterface {

    lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_promotion, container, false)

        updateConfig()
        getFirebaseRemoteConfig()

        return rootView


    }

    private fun getFirebaseRemoteConfig(){
        RemoteConfig(this).fetchRemoteConfig()
    }

    @SuppressLint("RestrictedApi")
    override fun onSuccessFetchRemoteConfig(remoteConfig: FirebaseRemoteConfig) {
        val webUrl = remoteConfig.getString(BuildConfig.web1)

        rootView.cannot_load_layout.visibility = View.GONE
        rootView.webView.visibility = View.VISIBLE
        rootView.full_web_view_button.visibility = View.VISIBLE


        rootView.webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                rootView.full_web_view_button.setOnClickListener {
                    val intent = Intent(rootView.context, FullWebviewActivity::class.java)
                    intent.putExtra("webUrl", url)
                    startActivity(intent)
                }
            }
        }

        val webView = rootView.webView
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.loadUrl(webUrl)
    }

    @SuppressLint("RestrictedApi")
    override fun onFailedFetchRemoteConfig() {
        rootView.cannot_load_layout.visibility = View.VISIBLE
        rootView.webView.visibility = View.GONE
        rootView.full_web_view_button.visibility = View.GONE
        rootView.try_again_btn.setOnClickListener {
            getFirebaseRemoteConfig()
        }
    }

    override fun onResume() {
        super.onResume()
        RemoteConfig(this).fetchRemoteConfig()
    }

    private fun updateConfig(){
        //Update Every 1 minutes
        val mHandler = Handler()
        val mHandlerTask = object : Runnable {
            override fun run() {
                getFirebaseRemoteConfig()
                Log.e("Update", "Updated")
                mHandler.postDelayed(this, 1000 * 60 * 1)
            }
        }

        mHandlerTask.run()
    }
}
