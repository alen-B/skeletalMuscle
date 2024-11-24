package com.fjp.skeletalmuscle.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.databinding.ActivityTodaySportsDataBinding
import com.fjp.skeletalmuscle.ui.main.adapter.TodaySportsDataAdapter
import com.fjp.skeletalmuscle.ui.main.adapter.ViewPagerFragmentAdapter
import com.fjp.skeletalmuscle.ui.main.fragment.TodaySportsDumbbellFragment
import com.fjp.skeletalmuscle.ui.main.fragment.TodaySportsHighKneeFragment
import com.fjp.skeletalmuscle.ui.main.fragment.TodaySportsPlankFragment
import com.fjp.skeletalmuscle.viewmodel.state.TodaySportsDataViewModel
import com.zhpan.indicator.enums.IndicatorStyle
import me.hgj.jetpackmvvm.ext.util.dp2px

class TodaySportsActivity :BaseActivity<TodaySportsDataViewModel,ActivityTodaySportsDataBinding>() {
    lateinit var todaySportsDataAdapter:TodaySportsDataAdapter
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.today_sports_data_title))
        mDatabind.click = ProxyClick()
        val fragments =arrayListOf<Fragment>(TodaySportsHighKneeFragment.newInstance(), TodaySportsDumbbellFragment.newInstance(),TodaySportsPlankFragment.newInstance())
        val viewpagerAdapter = ViewPagerFragmentAdapter(supportFragmentManager,1f,fragments)
        mDatabind.viewpager.adapter = viewpagerAdapter
        mDatabind.indicatorView.apply {
            setSliderColor(getColor(R.color.color_e9e9e9),getColor(R.color.color_blue))
            setPageSize(viewpagerAdapter.count)
            setSliderWidth(8f)
            setSliderHeight(8f)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            notifyDataChanged()
        }
        mDatabind.viewpager.pageMargin=dp2px(16)
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
    inner class ProxyClick{
        fun clickShare(){

        }
        fun clickSportsRecord(){

        }
        fun finish(){
            this@TodaySportsActivity.finish()
        }
    }
}