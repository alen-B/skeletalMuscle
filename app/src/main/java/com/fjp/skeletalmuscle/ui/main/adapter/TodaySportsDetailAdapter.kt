package com.fjp.skeletalmuscle.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fjp.skeletalmuscle.ui.main.TodaySportsDetailFragment

/**
 *Author:Mr'x
 *Time:2024/10/24
 *Description:
 */
class TodaySportsDetailAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val mTitles = arrayOf("日", "周", "月", "年", "总")
    override fun getCount(): Int {
        return mTitles.size
    }

    override fun getItem(position: Int): Fragment {
        return TodaySportsDetailFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles.get(position)
    }
}