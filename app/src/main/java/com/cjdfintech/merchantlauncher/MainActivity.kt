package com.cjdfintech.merchantlauncher

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cjdfintech.merchantlauncher.Information.*
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var pm: PackageManager
    lateinit var installedApp: ArrayList<AppInfo>
    lateinit var allApp: List<ResolveInfo>

    lateinit var appRecyclerView: RecyclerView

    lateinit var remoteConfig: FirebaseRemoteConfig

    lateinit var dialog: Dialog

    private var firstOpen = true
    private var allAppCount = 0

    companion object{
        private const val FINPOINT_PACKAGE = "com.cjdfintech.merchantapp"
        private const val SETTINGS_PACKAGE = "com.android.settings"
        private const val DIPCHIP_PACKAGE = "com.jr.jd.th.ekyc"
        private const val DIPCHIP_NAME = "Dip Chip"
        private const val APPSTORE_PACKAGE = "woyou.market"

        private const val SHOW_FINPOINT = "show_finpoint"
        private const val SHOW_DIPCHIP = "show_dipchip"
        private const val SHOW_SETTINGS = "show_settings"
        private const val SHOW_APPSTORE = "show_appstore"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getShowIconProperties()
        addArrayList()
        initializePager()
    }

    override fun onResume() {
        super.onResume()
        viewPager.currentItem = 0
        getShowIconProperties()
        addArrayList()
        dialogBuild()
        checkUpdateApp()
    }

    override fun onPause() {
        super.onPause()
        if(dialog.isShowing)
            dialog.dismiss()
    }


    private fun addArrayList(){

        pm = applicationContext.packageManager
        installedApp = ArrayList()

        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        allApp = pm.queryIntentActivities(i, 0)
        for (ri: ResolveInfo in allApp){
            if((ri.activityInfo.packageName.startsWith(FINPOINT_PACKAGE) && remoteConfig.getBoolean(SHOW_FINPOINT))
                || (ri.activityInfo.packageName == SETTINGS_PACKAGE && remoteConfig.getBoolean(SHOW_SETTINGS))
                || (ri.activityInfo.packageName == DIPCHIP_PACKAGE && remoteConfig.getBoolean(SHOW_DIPCHIP))
                || ri.activityInfo.packageName == APPSTORE_PACKAGE && remoteConfig.getBoolean(SHOW_APPSTORE)) {
                val app = AppInfo()
                when {
                    ri.activityInfo.packageName == DIPCHIP_PACKAGE -> {
                        app.label = DIPCHIP_NAME
                        app.listNumber = 1
                    }
                    ri.activityInfo.packageName.startsWith(FINPOINT_PACKAGE) -> {
                        app.label = ri.loadLabel(pm)
                        app.listNumber = 0
                    }
                    ri.activityInfo.packageName == SETTINGS_PACKAGE -> {
                        app.label = ri.loadLabel(pm)
                        app.listNumber = 2
                    }
                    ri.activityInfo.packageName == APPSTORE_PACKAGE -> {
                        app.label = ri.loadLabel(pm)
                        app.listNumber = 3
                    }
                }
                app.packageName = ri.activityInfo.packageName
                app.icon = ri.activityInfo.loadIcon(pm)


                installedApp.add(app)
            }

        }

        installedApp.sortBy { it.listNumber }


        if(firstOpen){
            allAppCount = installedApp.size
            firstOpen = false
        }

        if((installedApp.size > allAppCount) || (installedApp.size < allAppCount) && !firstOpen){
            runOnUiThread {
                appRecyclerView.adapter = AppHomeAdapter(installedApp, this)
                allAppCount = installedApp.size
                Log.e("App", "Apps list is changed")
            }
        }

        setupRecyclerView()
    }

    private fun getShowIconProperties(){
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .setMinimumFetchIntervalInSeconds(4200)
            .build()
        remoteConfig.setConfigSettings(configSettings)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if(task.isSuccessful){
                Log.e("FirebaseRemote", "Successful!")
            }
            else{
                Log.e("FirebaseRemote", "Error!")
            }
        }
    }

    private fun initializePager(){
        val informationAdapter = InformationAdapter(supportFragmentManager)
        informationAdapter.addFragment(PromotionFragment())
        informationAdapter.addFragment(NewsFragment())
        informationAdapter.addFragment(OtherFragment())
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
        dialog.setTitle(getString(R.string.dialog_new_update))
        dialog.setContentView(R.layout.dialog_update)
        dialog.setCanceledOnTouchOutside(false)
        dialog.update_btn.setOnClickListener {
            val intent = pm.getLaunchIntentForPackage(APPSTORE_PACKAGE)
            startActivity(intent)
        }

        dialog.cancel_btn.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun checkUpdateApp(){
        if(remoteConfig.getBoolean("new_version")) {
            dialog.show()
        }
    }
}
