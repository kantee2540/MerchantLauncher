package com.cjdfintech.merchantlauncher.Information

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cjdfintech.merchantlauncher.BuildConfig
import com.cjdfintech.merchantlauncher.R
import com.cjdfintech.merchantlauncher.RemoteConfig
import com.cjdfintech.merchantlauncher.RemoteConfigInterface
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.fragment_message.view.*
import java.nio.charset.StandardCharsets

class MessageFragment : Fragment(), RemoteConfigInterface{

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_message, container, false)

        updateConfig()
        getMessage()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        getMessage()
    }

    private fun getMessage(){
        RemoteConfig(this).fetchRemoteConfig()
    }

    override fun onSuccessFetchRemoteConfig(remoteConfig: FirebaseRemoteConfig) {
        val mess = remoteConfig.getString(BuildConfig.message).toByteArray(StandardCharsets.ISO_8859_1)
        val newValue = String(mess, StandardCharsets.UTF_8)
        Log.e("MESSAGE",newValue)
        rootView.message_tv.text = newValue
    }

    override fun onFailedFetchRemoteConfig() {
        rootView.message_tv.text = "เกิดข้อผิดพลาด"
    }

    private fun updateConfig(){
        //Update Every 1 minutes
        val mHandler = Handler()
        val mHandlerTask = object : Runnable {
            override fun run() {
                getMessage()
                Log.e("Update", "Updated")
                mHandler.postDelayed(this, 1000 * 60 * 2)
            }
        }

        mHandlerTask.run()
    }
}