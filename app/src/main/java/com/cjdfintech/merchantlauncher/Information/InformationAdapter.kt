package com.cjdfintech.merchantlauncher.Information

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class InformationAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        if(position == 0){
            return PromotionFragment()
        }
        else if(position == 1){
            return DeviceInfomationFragment()
        }
        else if(position == 2){
            return OtherFragment()
        }
        else{
            return PromotionFragment()
        }
    }
}