package com.cjdfintech.merchantlauncher

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class RemoteConfig(callback: RemoteConfigInterface) {

    init {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()
        remoteConfig.setConfigSettings(configSettings)
        remoteConfig.fetch(0)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if(task.isSuccessful){
                callback.onSuccessFetchRemoteConfig(remoteConfig)
                Log.e("FirebaseRemote", "Successful!")
            }
            else{
                callback.onFailedFetchRemoteConfig()
                Log.e("FirebaseRemote", "Error!")
            }
        }
    }

}