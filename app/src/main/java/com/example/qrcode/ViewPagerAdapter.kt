package com.example.qrcode

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.qrcode.fragments.TabCreateFragment
import com.example.qrcode.fragments.TabFavouriteFragment
import com.example.qrcode.fragments.TabScanFragment

class ViewPagerAdapter(supportFragmentManager: FragmentActivity) :
    FragmentStateAdapter(supportFragmentManager) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> TabScanFragment()
            1-> TabCreateFragment()
            2-> TabFavouriteFragment()
            else -> TabScanFragment()
        }
    }

}