package com.cjdfintech.merchantlauncher.information

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
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