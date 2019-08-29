package com.cjdfintech.merchantlauncher.Information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cjdfintech.merchantlauncher.R
import com.cjdfintech.merchantlauncher.RemoteConfig
import com.cjdfintech.merchantlauncher.RemoteConfigInterface
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.fragment_other.view.*

class OtherFragment : Fragment(), RemoteConfigInterface {

    private lateinit var rootView: View

    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_other, container, false)
        getMessage()
        return rootView
    }

    private fun getMessage(){
        RemoteConfig(this).fetchRemoteConfig()
    }

    private fun setupUpdateView(){
        if(!remoteConfig.getString("next_update").isEmpty()) {
            rootView.nextupdate_tv.text = remoteConfig.getString("next_update")
            rootView.instruction_layout.visibility = View.VISIBLE
        }
        else{
            rootView.nextupdate_tv.text = getString(R.string.no_update_schedule)
            rootView.instruction_layout.visibility = View.GONE
        }
    }

    override fun onSuccessFetchRemoteConfig(remoteConfig: FirebaseRemoteConfig) {
        this.remoteConfig = remoteConfig
        setupUpdateView()
    }

    override fun onFailedFetchRemoteConfig() {
        setupUpdateView()
    }

    override fun onResume() {
        super.onResume()
        getMessage()
    }


}
