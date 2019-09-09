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
import kotlinx.android.synthetic.main.fragment_next_update.view.*

class NextUpdateFragment : Fragment(), RemoteConfigInterface {

    private lateinit var rootView: View

    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_next_update, container, false)
        getMessage()
        return rootView
    }

    private fun getMessage(){
        RemoteConfig(this).fetchRemoteConfig()
    }

    private fun setupUpdateView(){
        if(!remoteConfig.getString(BuildConfig.nextUpdate).isEmpty()) {
            rootView.nextupdate_tv.text = remoteConfig.getString(BuildConfig.nextUpdate)
            rootView.instruction_layout.visibility = View.VISIBLE
            rootView.check_update_link.visibility = View.GONE
        }
        else{
            rootView.nextupdate_tv.text = getString(R.string.no_update_schedule)
            rootView.instruction_layout.visibility = View.GONE
            rootView.check_update_link.visibility = View.VISIBLE
            rootView.check_update_link.setOnClickListener {
                getMessage()
            }
        }
    }

    override fun onSuccessFetchRemoteConfig(remoteConfig: FirebaseRemoteConfig) {
        this.remoteConfig = remoteConfig
        setupUpdateView()
    }

    override fun onFailedFetchRemoteConfig() {
        rootView.nextupdate_tv.text = getString(R.string.no_update_schedule)
        rootView.instruction_layout.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        getMessage()
    }


}
