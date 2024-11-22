package com.fjp.skeletalmuscle.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentMainSportsHighKneeBinding
import com.fjp.skeletalmuscle.ui.main.TodaySportsActivity
import com.fjp.skeletalmuscle.viewmodel.state.MainSportsHighKneeViewModel

class MainSportsHighKneeFragment :BaseFragment<MainSportsHighKneeViewModel,FragmentMainSportsHighKneeBinding>() {

    companion object {
        fun newInstance() = MainSportsHighKneeFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.exerciseIntensityLayout.setValue(1.1f,2.2f,3.3f,4.4f)
    }


    inner class ProxyClick{
        fun clickTodaySports(){
            startActivity(Intent(activity, TodaySportsActivity::class.java))
        }
    }
}