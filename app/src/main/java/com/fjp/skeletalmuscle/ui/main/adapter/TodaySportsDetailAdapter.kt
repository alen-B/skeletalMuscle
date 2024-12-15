package com.fjp.skeletalmuscle.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 *Author:Mr'x
 *Time:2024/10/24
 *Description:
 */
class TodaySportsDetailAdapter(fm: FragmentManager, val list: List<Fragment>) : FragmentStatePagerAdapter(fm) {
    private val mTitles = arrayOf("日", "周", "月", "年", "总")
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles.get(position)
    }
}