package com.cjdfintech.merchantlauncher.Information

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cjdfintech.merchantlauncher.R
import kotlinx.android.synthetic.main.fragment_other.view.*
import kotlinx.android.synthetic.main.fragment_other.view.check_version_tv
import java.lang.Exception

class OtherFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_other, container, false)

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

    override fun onResume() {
        super.onResume()
        setupView()
    }


}
