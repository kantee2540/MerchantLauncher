package com.cjdfintech.merchantlauncher

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

interface RemoteConfigInterface {

    fun onSuccessFetchRemoteConfig(remoteConfig: FirebaseRemoteConfig)
    fun onFailedFetchRemoteConfig()
}