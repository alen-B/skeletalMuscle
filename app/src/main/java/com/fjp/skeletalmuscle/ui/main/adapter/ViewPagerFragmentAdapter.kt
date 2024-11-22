package com.fjp.skeletalmuscle.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle

/**
 *Author:Mr'x
 *Time:2024/11/22
 *Description:
 */
class ViewPagerFragmentAdapter(fragment: FragmentManager, var lifecycle: Lifecycle, private var fragmentList: List<Fragment>) : FragmentPagerAdapter(fragment) {
    override fun getCount(): Int {
       return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageWidth(position: Int): Float {
        return 0.93f
    }
}
