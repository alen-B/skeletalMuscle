package com.fjp.skeletalmuscle.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.databinding.ActivityTodaySportsDataBinding
import com.fjp.skeletalmuscle.ui.main.adapter.TodaySportsDataAdapter
import com.fjp.skeletalmuscle.ui.main.adapter.ViewPagerFragmentAdapter
import com.fjp.skeletalmuscle.ui.main.fragment.TodaySportsDumbbellFragment
import com.fjp.skeletalmuscle.ui.main.fragment.TodaySportsHighKneeFragment
import com.fjp.skeletalmuscle.ui.main.fragment.TodaySportsPlankFragment
import com.fjp.skeletalmuscle.viewmodel.state.TodaySportsDataViewModel
import com.zhpan.indicator.enums.IndicatorStyle
import me.hgj.jetpackmvvm.ext.util.dp2px
import me.hgj.jetpackmvvm.util.get

class TodaySportsActivity : BaseActivity<TodaySportsDataViewModel, ActivityTodaySportsDataBinding>() {
    companion object {
        fun start(context: Context, todayDataResult: TodayDataResult, sportsType: SportsType) {
            val intent = Intent(context, TodaySportsActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_TODAY_SPORTS_DATA, todayDataResult)
            intent.putExtra(Constants.INTENT_KEY_TODAY_SPORTS_TYPE, sportsType)
            context.startActivity(intent)
        }
    }

    lateinit var todaySportsDataAdapter: TodaySportsDataAdapter
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.today_sports_data_title))
        mDatabind.click = ProxyClick()
        //type用来记录是点击那个运动进来的详情
        val type = intent.get(Constants.INTENT_KEY_TODAY_SPORTS_TYPE, SportsType.HIGH_KNEE)
        val todaySportsData = intent.getParcelableExtra<TodayDataResult>(Constants.INTENT_KEY_TODAY_SPORTS_DATA)
        val fragments = arrayListOf<Fragment>()
        if(type == SportsType.HIGH_KNEE){
            if (todaySportsData?.sport_lift_leg != null) {
                fragments.add(TodaySportsHighKneeFragment.newInstance(todaySportsData?.sport_lift_leg))
            }
        }else if(type == SportsType.DUMBBELL){
            if (todaySportsData?.sport_dumbbell != null ) {
                fragments.add(TodaySportsDumbbellFragment.newInstance(todaySportsData?.sport_dumbbell))
            }
        }else if(type == SportsType.PLANK){
            if (todaySportsData?.sport_flat_support != null ) {
                fragments.add(TodaySportsPlankFragment.newInstance(todaySportsData?.sport_flat_support))
            }
        }
        val viewpagerAdapter = ViewPagerFragmentAdapter(supportFragmentManager, 1f, fragments)
        mDatabind.viewpager.adapter = viewpagerAdapter
        mDatabind.indicatorView.apply {
            setSliderColor(getColor(R.color.color_e9e9e9), getColor(R.color.color_blue))
            setPageSize(viewpagerAdapter.count)
            setSliderWidth(8f)
            setSliderHeight(8f)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            notifyDataChanged()
        }
        mDatabind.viewpager.pageMargin = dp2px(16)
        mDatabind.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                mDatabind.indicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                mDatabind.indicatorView.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
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
        fun clickShare() {

        }

        fun clickSportsRecord() {
            startActivity(Intent(this@TodaySportsActivity, CalendarActivity::class.java))
        }

        fun finish() {
            this@TodaySportsActivity.finish()
        }
    }
}