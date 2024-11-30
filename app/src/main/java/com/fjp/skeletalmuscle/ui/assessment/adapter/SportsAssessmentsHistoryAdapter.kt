package com.fjp.skeletalmuscle.ui.assessment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fjp.skeletalmuscle.R
import me.hgj.jetpackmvvm.base.appContext

class SportsAssessmentsHistoryAdapter(fm: FragmentManager,val list:List<Fragment>) : FragmentPagerAdapter(fm) {
    private val mTitles = arrayOf(appContext.getString(R.string.month_1),
        appContext.getString(R.string.month_2),
        appContext.getString(R.string.month_3),
        appContext.getString(R.string.month_4),
        appContext.getString(R.string.month_5),
        appContext.getString(R.string.month_6),
        appContext.getString(R.string.month_7),
        appContext.getString(R.string.month_8),
        appContext.getString(R.string.month_9),
        appContext.getString(R.string.month_10),
        appContext.getString(R.string.month_11),
        appContext.getString(R.string.month_12))
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence{
        return mTitles[position]
    }
}