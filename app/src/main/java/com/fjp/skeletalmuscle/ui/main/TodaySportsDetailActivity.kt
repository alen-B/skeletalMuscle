package com.fjp.skeletalmuscle.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityTodaySportsDetailBinding
import com.fjp.skeletalmuscle.ui.main.adapter.TodaySportsDetailAdapter
import com.fjp.skeletalmuscle.ui.main.fragment.TodaySportsDetailFragment
import com.fjp.skeletalmuscle.viewmodel.state.ChartType
import com.fjp.skeletalmuscle.viewmodel.state.DateType
import com.fjp.skeletalmuscle.viewmodel.state.TodaySportsDetailViewModel
import com.flyco.tablayout.listener.OnTabSelectListener
import me.hgj.jetpackmvvm.util.get

class TodaySportsDetailActivity : BaseActivity<TodaySportsDetailViewModel, ActivityTodaySportsDetailBinding>() {


    companion object {
        fun startActivity(context: Context, sportsType: SportsType, chartType: ChartType) {
            val intent = Intent(context, TodaySportsDetailActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_SPORTS_DETAIL_TYPE, sportsType.type)
            intent.putExtra(Constants.INTENT_KEY_SPORTS_CHART_TYPE, chartType.type)
            context.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewModel.title.set("今日运动详情")
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()

        val sportsType = intent.get(Constants.INTENT_KEY_SPORTS_DETAIL_TYPE, SportsType.HIGH_KNEE.type)
        val chartType = intent.get(Constants.INTENT_KEY_SPORTS_CHART_TYPE, ChartType.BURN_CALORIES.type)
        initSecondTitle(sportsType!!, chartType!!)
        val vp = mDatabind.todayViewpager
        val fragments = arrayListOf<Fragment>(TodaySportsDetailFragment.newInstance(chartType, DateType.DAY), TodaySportsDetailFragment.newInstance(chartType, DateType.DAY), TodaySportsDetailFragment.newInstance(chartType, DateType.DAY), TodaySportsDetailFragment.newInstance(chartType, DateType.DAY))
        vp.adapter = TodaySportsDetailAdapter(supportFragmentManager, fragments)
        mDatabind.tabLayout.setViewPager(vp)
        mDatabind.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {

            }

            override fun onTabReselect(position: Int) {
            }

        })
        if (chartType == ChartType.BURN_CALORIES.type) {
            mDatabind.bottomBgView.visibility = View.VISIBLE
            mDatabind.allSportsTitleTv.visibility = View.VISIBLE
            mDatabind.allTabLayout.visibility = View.VISIBLE
            mDatabind.allSportsViewpager.visibility = View.VISIBLE

            val allSportsViewpager = mDatabind.allSportsViewpager
            val fragments = arrayListOf<Fragment>(TodaySportsDetailFragment.newInstance(ChartType.BURN_CALORIES.type, DateType.DAY), TodaySportsDetailFragment.newInstance(ChartType.BURN_CALORIES.type, DateType.MONTH), TodaySportsDetailFragment.newInstance(ChartType.BURN_CALORIES.type, DateType.YEAR), TodaySportsDetailFragment.newInstance(ChartType.BURN_CALORIES.type, DateType.All))
            allSportsViewpager.adapter = TodaySportsDetailAdapter(supportFragmentManager, fragments)
            mDatabind.allTabLayout.setViewPager(allSportsViewpager)

        }
    }

    private fun getChartType(chartType: Int): String {
        return when (chartType) {
            ChartType.BURN_CALORIES.type -> {
                return getString(R.string.today_sports_detail_calorie)
            }

            ChartType.HEART_RATE_TREND.type -> {
                return getString(R.string.high_knee_main_heart_rate_trend)
            }

            ChartType.LEG_LIFTING_ANGLE.type -> {
                return getString(R.string.today_sports_data_leg_angle)
            }

            ChartType.INTENSITY_AND_TIME.type -> {
                return getString(R.string.sports_record_sport_time)
            }

            else -> {
                return getString(R.string.today_sports_detail_calorie)
            }
        }
    }

    private fun initSecondTitle(sportsType: Int, chartType: Int) {
        val sports = getSports(sportsType)
        val chartType = getChartType(chartType)
        mDatabind.secondTitleTv.text = "$sports-$chartType"
    }

    private fun getSports(sportsType: Int): String {
        return when (sportsType) {
            SportsType.HIGH_KNEE.type -> {
                return getString(R.string.today_sports_data_type1)
            }

            SportsType.DUMBBELL.type -> {
                return getString(R.string.today_sports_data_type2)
            }

            SportsType.PLANK.type -> {
                return getString(R.string.today_sports_data_type3)
            }

            else -> {
                return getString(R.string.today_sports_data_type1)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mDatabind.blurLayout.startBlur()
    }

    override fun onPause() {
        super.onPause()
        mDatabind.blurLayout.pauseBlur()
    }

    inner class ProxyClick {
        fun clickSportsRecord() {

        }

        fun finish() {
            this@TodaySportsDetailActivity.finish()
        }
    }
}