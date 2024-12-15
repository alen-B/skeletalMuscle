package com.fjp.skeletalmuscle.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 *Author:Mr'x
 *Time:2024/11/22
 *Description:
 */
class ViewPagerFragmentAdapter(fragment: FragmentManager, val pageWidth: Float, private var fragmentList: List<Fragment>) : FragmentStatePagerAdapter(fragment) {
    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageWidth(position: Int): Float {
        return pageWidth
    }
}
