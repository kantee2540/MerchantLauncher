package com.cjdfintech.merchantlauncher

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var pm: PackageManager
    lateinit var apps: List<ApplicationInfo>
    lateinit var installedApp: ArrayList<AppInfo>
    lateinit var allApp: List<ResolveInfo>

    private lateinit var resultDateTime :String
    private val formatTime = SimpleDateFormat("HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pm = applicationContext.packageManager
        apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        installedApp = ArrayList()

        addArrayList()

        appList.layoutManager = GridLayoutManager(this, 3)
        appList.adapter = AppAdapter(installedApp, this)

        val timer= Timer()
        timer?.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                updateTimer()
            }
        }, 0, 1000)
    }

    fun addArrayList(){
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        allApp = pm.queryIntentActivities(i, 0)
        for (ri: ResolveInfo in allApp){
            val app = AppInfo()
            app.label = ri.loadLabel(pm)
            app.packageName = ri.activityInfo.packageName
            app.icon = ri.activityInfo.loadIcon(pm)
            installedApp.add(app)
        }
    }

    private fun updateTimer(){

        runOnUiThread {
            val date = Date()
            resultDateTime = formatTime.format(date)
            clockTv.setText(resultDateTime)
        }

    }
}
