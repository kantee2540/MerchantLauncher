package com.cjdfintech.merchantlauncher.Information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cjdfintech.merchantlauncher.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class NewsFragment : Fragment(){

    private lateinit var rootView: View

    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_news, container, false)

        return rootView
    }
}