package com.fjp.skeletalmuscle.ui.deviceguidepage.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep11Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep1Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep2Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep3Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep5Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep7Binding
import com.fjp.skeletalmuscle.ui.deviceguidepage.HighKneeGuideActivity
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep11ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep1ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep2ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep3ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep5ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep7ViewModel

class HighKneeGuideStep11Fragment : BaseFragment<HighKneeGuideStep11ViewModel, FragmentHightKneeGuideStep11Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep11Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.high_knee_guide_step11_title))
//        (activity as HighKneeGuideActivity).setNextButtonEnable(false)


    }

    override fun initData() {
        super.initData()

    }


}