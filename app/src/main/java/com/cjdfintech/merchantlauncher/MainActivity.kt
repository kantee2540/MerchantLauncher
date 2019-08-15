package com.cjdfintech.merchantlauncher

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.cjdfintech.merchantlauncher.Information.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var pm: PackageManager
    lateinit var installedApp: ArrayList<AppInfo>
    lateinit var allApp: List<ResolveInfo>

    lateinit var appRecyclerView: RecyclerView
    lateinit var saveInstalledApp: SharedPreferences

    private lateinit var resultDateTime :String
    private var firstOpen = true
    private var allAppCount = 0
    private val formatTime = SimpleDateFormat("HH:mm")
    private val formatDay = SimpleDateFormat("EEEE")
    private val formatDate = SimpleDateFormat("d MMMM y")

    companion object{
        private const val FINPOINT_PACKAGE = "com.cjdfintech.merchantapp"
        private const val SETTINGS_PACKAGE = "com.android.settings"
        private const val DIPCHIP_PACKAGE = "com.jr.jd.th.ekyc"
        private const val DIPCHIP_NAME = "Dip Chip"
        private const val APPSTORE_PACKAGE = "woyou.market"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveInstalledApp = getSharedPreferences("saveArrayList", MODE_PRIVATE)

        addArrayList()
        updateTimer()
        intializePager()

        allAppButton.setOnClickListener {
            val intent = Intent(this, AppDrawerActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.push_animation_enter, R.anim.push_animation_exit)
        }

    }

    override fun onResume() {
        super.onResume()
        viewPager.currentItem = 0
        addArrayList()
    }


    fun addArrayList(){

        pm = applicationContext.packageManager
        installedApp = ArrayList()

        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        allApp = pm.queryIntentActivities(i, 0)
        for (ri: ResolveInfo in allApp){
            if(ri.activityInfo.packageName.startsWith(FINPOINT_PACKAGE)
                || ri.activityInfo.packageName == SETTINGS_PACKAGE
                || ri.activityInfo.packageName == DIPCHIP_PACKAGE
                || ri.activityInfo.packageName == APPSTORE_PACKAGE) {
                val app = AppInfo()
                if (ri.activityInfo.packageName == DIPCHIP_PACKAGE){
                    app.label = DIPCHIP_NAME
                    app.listNumber = 1
                }
                else if(ri.activityInfo.packageName.startsWith(FINPOINT_PACKAGE)){
                    app.label = ri.loadLabel(pm)
                    app.listNumber = 0
                }
                else if(ri.activityInfo.packageName == SETTINGS_PACKAGE){
                    app.label = ri.loadLabel(pm)
                    app.listNumber = 2
                }
                else if(ri.activityInfo.packageName == APPSTORE_PACKAGE){
                    app.label = ri.loadLabel(pm)
                    app.listNumber = 3
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

    private fun checkAppListIsChanged(){

    }

    private fun intializePager(){
        val informationAdapter = InformationAdapter(supportFragmentManager)
        informationAdapter.addFragment(PromotionFragment())
        informationAdapter.addFragment(CallCenterFragment())
        informationAdapter.addFragment(OtherFragment())

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

    private fun updateTimer(){
        val timer= Timer()
        timer?.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                //addArrayList()
                runOnUiThread {
                    val date = Date()
                    resultDateTime = formatTime.format(date)
                    clockTv.setText(resultDateTime)
                    dayTextView.setText(formatDay.format(date))
                    dateTextView.setText(formatDate.format(date))
                }
            }
        }, 0, 1000)

    }
}
