package com.fjp.skeletalmuscle.ui.main

import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.databinding.ActivityTodaySportsDetailBinding
import com.fjp.skeletalmuscle.ui.main.adapter.TodaySportsDetailAdapter
import com.fjp.skeletalmuscle.viewmodel.state.TodaySportsDetailViewModel
import com.flyco.tablayout.listener.OnTabSelectListener

class TodaySportsDetailActivity : BaseActivity<TodaySportsDetailViewModel,ActivityTodaySportsDetailBinding>(){



    override fun initView(savedInstanceState: Bundle?) {
//        mViewModel.title.set()
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        val vp = mDatabind.vp
        vp.adapter = TodaySportsDetailAdapter(supportFragmentManager)
        mDatabind.tabLayout.setViewPager(vp)
        mDatabind.tabLayout.setOnTabSelectListener(object: OnTabSelectListener {
            override fun onTabSelect(position: Int) {

            }

            override fun onTabReselect(position: Int) {
            }

        })
    }


    inner class ProxyClick{
        fun clickSportsRecord(){

        }
    }
}