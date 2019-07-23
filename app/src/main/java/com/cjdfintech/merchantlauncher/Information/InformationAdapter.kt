package com.cjdfintech.merchantlauncher.Information

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup

class InformationAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var mFragment: ArrayList<Fragment> = ArrayList()

    fun addFragment(fragment: Fragment) {
        mFragment.add(fragment)
    }

    override fun getCount(): Int {
        return mFragment.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragment[position]
    }

}