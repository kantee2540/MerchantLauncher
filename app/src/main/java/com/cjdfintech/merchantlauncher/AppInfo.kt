package com.cjdfintech.merchantlauncher

import android.graphics.drawable.Drawable

class AppInfo {
    lateinit var label: CharSequence
    lateinit var packageName: CharSequence
    lateinit var icon: Drawable
    var listNumber: Int = 0
}

class RemoteConfigPackage{
    lateinit var appName: String
    lateinit var packageName: String
    var show: Boolean = false
}