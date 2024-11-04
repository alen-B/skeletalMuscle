package com.fjp.skeletalmuscle.ui.deviceguidepage.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep1Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep2Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep3Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep5Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep7Binding
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep1ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep2ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep3ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep5ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep7ViewModel

class HighKneeGuideStep7Fragment : BaseFragment<HighKneeGuideStep7ViewModel, FragmentHightKneeGuideStep7Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep7Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.high_knee_guide_step7_title))


    }

    override fun initData() {
        super.initData()

    }


}