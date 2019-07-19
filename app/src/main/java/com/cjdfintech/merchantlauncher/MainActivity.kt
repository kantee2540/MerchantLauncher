package com.cjdfintech.merchantlauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.KeyEvent
import com.cjdfintech.merchantlauncher.Information.InformationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var pm: PackageManager
    lateinit var installedApp: ArrayList<AppInfo>
    lateinit var allApp: List<ResolveInfo>

    private lateinit var resultDateTime :String
    private val formatTime = SimpleDateFormat("HH:mm")
    private val formatDay = SimpleDateFormat("EEEE")
    private val formatDate = SimpleDateFormat("d MMMM y")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addArrayList()
        intializePager()

        appList.layoutManager = GridLayoutManager(this@MainActivity, 3)
        appList.adapter = AppHomeAdapter(installedApp, this)

        allAppButton.setOnClickListener {
            val intent = Intent(this, AppDrawerActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.push_animation_enter, R.anim.push_animation_exit)
        }
        searchButton.setOnClickListener {

        }

        val timer= Timer()
        timer?.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                updateTimer()
            }
        }, 0, 1000)

    }

    override fun onResume() {
        super.onResume()
        Log.e("==>", "Resume")
        viewPager.setCurrentItem(0)
        addArrayList()
    }


    fun addArrayList(){

        pm = applicationContext.packageManager
        installedApp = ArrayList()

        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        allApp = pm.queryIntentActivities(i, 0)
        for (ri: ResolveInfo in allApp){
            if(ri.activityInfo.packageName.startsWith("com.cjdfintech.merchantapp")
                || ri.activityInfo.packageName.equals("com.android.settings")
                || ri.activityInfo.packageName.equals("com.jr.jd.th.ekyc")){
                val app = AppInfo()
                app.label = ri.loadLabel(pm)
                app.packageName = ri.activityInfo.packageName
                app.icon = ri.activityInfo.loadIcon(pm)
                installedApp.add(app)
            }

        }
    }
    private fun intializePager(){
        val informationPager = viewPager
        informationPager.adapter = InformationAdapter(supportFragmentManager)
        val tabDotPager = tabDot
        tabDotPager.setupWithViewPager(informationPager, true)
    }

    private fun updateTimer(){

        runOnUiThread {
            val date = Date()
            resultDateTime = formatTime.format(date)
            clockTv.setText(resultDateTime)
            dayTextView.setText(formatDay.format(date))
            dateTextView.setText(formatDate.format(date))
        }

    }
}
