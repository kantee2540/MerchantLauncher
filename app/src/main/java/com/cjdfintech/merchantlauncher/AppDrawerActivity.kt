package com.cjdfintech.merchantlauncher

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_app_drawer.*

class AppDrawerActivity : AppCompatActivity() {

    lateinit var pm: PackageManager
    lateinit var installedApp: ArrayList<AppInfo>
    lateinit var allApp: List<ResolveInfo>

    var pauseApp :Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_drawer)

        pm = applicationContext.packageManager
        installedApp = ArrayList()

        addArrayList()

        appDrawer.layoutManager = GridLayoutManager(this, 4)
        appDrawer.adapter = AppAdapter(installedApp, this)
        toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.pop_animation_enter, R.anim.pop_animation_exit)
        }
    }

    override fun onResume() {
        super.onResume()
        if (pauseApp){
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        pauseApp = true
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
            Log.e("LABEL", ri.activityInfo.packageName.toString())
            installedApp.add(app)

        }

        installedApp.sortBy { it.label.toString() }
    }
}
