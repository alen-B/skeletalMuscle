package com.fjp.skeletalmuscle.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentMainSportsPlankBinding
import com.fjp.skeletalmuscle.ui.main.TodaySportsActivity
import com.fjp.skeletalmuscle.viewmodel.state.MainSportsPlankViewModel

class MainSportsPlankFragment : BaseFragment<MainSportsPlankViewModel, FragmentMainSportsPlankBinding>() {

    companion object {
        fun newInstance() = MainSportsPlankFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
    }


    inner class ProxyClick {
        fun clickTodaySports() {
            startActivity(Intent(activity, TodaySportsActivity::class.java))
        }
    }
}