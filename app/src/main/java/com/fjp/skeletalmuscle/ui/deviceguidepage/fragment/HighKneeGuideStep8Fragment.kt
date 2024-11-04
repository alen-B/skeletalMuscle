package com.fjp.skeletalmuscle.ui.deviceguidepage.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep1Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep8Binding
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep1ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep8ViewModel

class HighKneeGuideStep8Fragment : BaseFragment<HighKneeGuideStep8ViewModel, FragmentHightKneeGuideStep8Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep8Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.high_knee_guide_step8_title))

    }

    override fun initData() {
        super.initData()

    }


}