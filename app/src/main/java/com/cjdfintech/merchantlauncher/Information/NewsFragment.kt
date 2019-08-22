package com.cjdfintech.merchantlauncher.Information

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cjdfintech.merchantlauncher.BuildConfig
import com.cjdfintech.merchantlauncher.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.fragment_news.view.*

class NewsFragment : Fragment(){

    private lateinit var rootView: View

    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_news, container, false)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("helloWorld")

        getMessage()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        getMessage()
    }

    private fun getMessage(){
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .setMinimumFetchIntervalInSeconds(4200)
            .build()
        remoteConfig.setConfigSettings(configSettings)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if(task.isSuccessful){
                rootView.message_tv.text = remoteConfig.getString("message")
                Log.e("FirebaseRemote", "Successful!")
            }
            else{
                Log.e("FirebaseRemote", "Error!")
            }
        }
    }
}