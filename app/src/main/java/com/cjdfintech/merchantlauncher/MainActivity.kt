package com.cjdfintech.merchantlauncher

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import com.cjdfintech.merchantlauncher.information.*
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update.*
import org.json.JSONArray
import java.lang.Exception
import kotlin.collections.ArrayList
import android.util.DisplayMetrics
import android.view.View

class MainActivity : AppCompatActivity(), RemoteConfigInterface {

    companion object{
        private const val FINPOINT_PACKAGE = "com.cjdfintech.merchantapp"
        private const val SETTINGS_PACKAGE = "com.android.settings"
        private const val DIPCHIP_PACKAGE = "com.jr.jd.th.ekyc"
        private const val DIPCHIP_NAME = "Dip Chip"
        private const val APPSTORE_PACKAGE = "woyou.market"
        private const val PLAYSTORE_PACKAGE = "com.android.vending"

        private const val REMOTE_APP_NAME = "app_name"
        private const val REMOTE_PACKAGE = "package"
        private const val REMOTE_SHOW_APP = "show"

        private const val SAVE_INSTALLED_LIST = "save_install_list"
        private const val JSON = "json"
    }

    private lateinit var pm: PackageManager
    private lateinit var installedApp: ArrayList<AppInfo>
    private lateinit var allApp: List<ResolveInfo>
    private lateinit var appRecyclerView: RecyclerView
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var dialog: Dialog

    private var firstOpen = true
    private var allAppCount = 0

    private lateinit var sharedPref: SharedPreferences
    private lateinit var mHandlerTask: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pm = applicationContext.packageManager
        installedApp = ArrayList()

        sharedPref = this.getSharedPreferences(SAVE_INSTALLED_LIST, Context.MODE_PRIVATE)

        updateConfig()
        dialogBuild()
        getFirebaseRemoteConfigProperties()
        initializePager()
    }

    override fun onResume() {
        super.onResume()

        viewPager.currentItem = 0
        getFirebaseRemoteConfigProperties()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        getFirebaseRemoteConfigProperties()
    }

    override fun onSuccessFetchRemoteConfig(remoteConfig: FirebaseRemoteConfig) {
        this.remoteConfig = remoteConfig
        addArrayList()
        checkUpdateApp()
    }

    override fun onFailedFetchRemoteConfig() {
        if(installedApp.size != 0){
            no_item_layout.visibility = View.GONE
        }
        else{
            no_item_layout.visibility = View.VISIBLE
        }
        if(sharedPref.getString(JSON, "") != "")
            addArrayList()
    }


    private fun addArrayList(){
        installedApp = ArrayList()

        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        try {
            addToSharedPreference(remoteConfig.getString(BuildConfig.packageShowApp))
        }catch (e:Exception){
            e.printStackTrace()
        }

        val checkPackage: ArrayList<RemoteConfigPackage> = ArrayList()
        val jsonArray = JSONArray(sharedPref.getString(JSON, ""))

        for(x in 0 until jsonArray.length()){
            val remotePackage = RemoteConfigPackage()
            val obj = jsonArray.getJSONObject(x)
            remotePackage.appName = obj.getString(REMOTE_APP_NAME)
            remotePackage.packageName = obj.getString(REMOTE_PACKAGE)
            remotePackage.show = obj.getBoolean(REMOTE_SHOW_APP)

            checkPackage.add(remotePackage)
        }

        allApp = pm.queryIntentActivities(i, 0)
        for (ri: ResolveInfo in allApp){

            for(j in 0 until checkPackage.size){
                if(ri.activityInfo.packageName.startsWith(checkPackage[j].packageName)
                    && checkPackage[j].show){
                    val res = pm.getResourcesForApplication(ri.activityInfo.applicationInfo)
                    val app = AppInfo()
                    if(ri.activityInfo.packageName == DIPCHIP_PACKAGE){
                        app.label = DIPCHIP_NAME
                        app.icon = ri.activityInfo.loadIcon(pm)
                    }
                    else {
                        app.label = ri.loadLabel(pm)
                        app.icon = res.getDrawableForDensity(
                            ri.activityInfo.applicationInfo.icon, DisplayMetrics.DENSITY_XXXHIGH)!!

                    }
                    app.packageName = ri.activityInfo.packageName
                    //app.icon = ri.activityInfo.loadIcon(pm)

                    app.listNumber = j

                    installedApp.add(app)
                }
            }

        }

        installedApp.sortBy { it.listNumber }

        if (installedApp.size != 0){
            no_item_layout.visibility = View.GONE
        }
        else{
            no_item_layout.visibility = View.VISIBLE
        }


        if(firstOpen){
            allAppCount = installedApp.size
            firstOpen = false
        }

        if((installedApp.size > allAppCount) || (installedApp.size < allAppCount) && !firstOpen){
            runOnUiThread {
                appRecyclerView.adapter = AppHomeAdapter(installedApp, this)
                allAppCount = installedApp.size
            }
        }

        setupRecyclerView()
    }

    @SuppressLint("CommitPrefEdits")
    private fun addToSharedPreference(json: String){
        sharedPref.edit().apply {
            this.putString(JSON, json)
        }.apply()
    }

    private fun getFirebaseRemoteConfigProperties(){
        RemoteConfig(this).fetchRemoteConfig()

    }

    private fun initializePager(){
        val informationAdapter = InformationAdapter(supportFragmentManager)
        informationAdapter.addFragment(WebFragment())
        informationAdapter.addFragment(MessageFragment())
        informationAdapter.addFragment(NextUpdateFragment())
        informationAdapter.addFragment(CallCenterFragment())

        val informationPager = viewPager
        informationPager.adapter = informationAdapter
        informationPager.offscreenPageLimit = 3
        val tabDotPager = tabDot
        tabDotPager.setupWithViewPager(informationPager, true)

    }
    private fun setupRecyclerView(){
        appRecyclerView = appList
        appRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@MainActivity, 3)
        appRecyclerView.adapter = AppHomeAdapter(installedApp, this)
    }

    private fun dialogBuild(){
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_update)
        dialog.setCanceledOnTouchOutside(false)

        dialog.update_btn.setOnClickListener {
            try {
                val intent = pm.getLaunchIntentForPackage(APPSTORE_PACKAGE)
                startActivity(intent)
            }
            catch (e:Exception){
                val intent = pm.getLaunchIntentForPackage(PLAYSTORE_PACKAGE)
                startActivity(intent)
            }
        }
        dialog.cancel_btn.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun checkUpdateApp(){
        var finpointVersionCode = 0
        try{
            val pInfo = pm.getPackageInfo(BuildConfig.finpointPackageName, 0)
            finpointVersionCode = pInfo.versionCode
        }catch (e:Exception){
            e.printStackTrace()
        }


        if(finpointVersionCode < remoteConfig.getLong(BuildConfig.check_version_finpoint)
            && finpointVersionCode != 0
            || remoteConfig.getBoolean(BuildConfig.openAppStore)) {
            dialog.title_update_tv.text = getString(R.string.dialog_new_update)
            dialog.description_update_tv.text = getString(R.string.dialog_new_update_description)
            dialog.show()
        }
        else if(finpointVersionCode == 0){
            dialog.title_update_tv.text = getString(R.string.dialog_not_install)
            dialog.description_update_tv.text = getString(R.string.dialog_not_install_description)
            dialog.show()
        }
        else{

        }
    }

    private fun updateConfig(){
        val mHandler = Handler()
        mHandlerTask = object : Runnable {
            override fun run() {
                getFirebaseRemoteConfigProperties()
                mHandler.postDelayed(this, 1000 * 60 * 2)
            }
        }

        mHandlerTask.run()
    }

}
