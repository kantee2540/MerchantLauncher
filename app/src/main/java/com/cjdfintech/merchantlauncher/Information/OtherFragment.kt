package com.cjdfintech.merchantlauncher.Information

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cjdfintech.merchantlauncher.BuildConfig
import com.cjdfintech.merchantlauncher.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.fragment_other.view.*
import kotlinx.android.synthetic.main.fragment_other.view.check_version_tv
import java.lang.Exception

class OtherFragment : Fragment() {

    private lateinit var rootView: View

    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_other, container, false)

        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .setMinimumFetchIntervalInSeconds(4200)
            .build()
        remoteConfig.setConfigSettings(configSettings)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if(task.isSuccessful){
                Log.e("FirebaseRemote", "Successful!")
                setupUpdateView()
            }
            else{
                Log.e("FirebaseRemote", "Error!")
            }
        }


        setupView()

        return rootView
    }

    private fun setupView(){
        try{
            rootView.version_layout.visibility = View.VISIBLE
            rootView.check_version_tv.text = getString(R.string.update_lastest)

            val packageManager = rootView.context.packageManager
            val pInfo = packageManager.getPackageInfo("com.cjdfintech.merchantapp.uat", 0)
            val pVersion = pInfo.versionName
            rootView.version_code_tv.text = pVersion.toString()

        }catch (e: Exception){
            rootView.version_layout.visibility = View.GONE
            rootView.check_version_tv.text = getString(R.string.not_installed)
        }
    }

    private fun setupUpdateView(){
        if(!remoteConfig.getString("next_update").isEmpty()) {
            rootView.nextupdate_tv.text = remoteConfig.getString("next_update")
            rootView.keepon_tv.visibility = View.VISIBLE
        }
        else{
            rootView.nextupdate_tv.text = getString(R.string.no_update_schedule)
            rootView.keepon_tv.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        setupView()
    }


}
