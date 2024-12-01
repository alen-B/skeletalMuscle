package com.fjp.skeletalmuscle.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentMainSportsDumbbellBinding
import com.fjp.skeletalmuscle.ui.main.TodaySportsActivity
import com.fjp.skeletalmuscle.viewmodel.state.MainSportsDumbbellViewModel

class MainSportsDumbbellFragment : BaseFragment<MainSportsDumbbellViewModel, FragmentMainSportsDumbbellBinding>() {

    companion object {
        fun newInstance() = MainSportsDumbbellFragment()
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