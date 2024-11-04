package com.fjp.skeletalmuscle.ui.deviceguidepage.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep1Binding
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep1ViewModel

class HighKneeGuideStep1Fragment : BaseFragment<HighKneeGuideStep1ViewModel,FragmentHightKneeGuideStep1Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep1Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel


    }

    override fun initData() {
        super.initData()
        mViewModel.title.set(getString(R.string.high_knee_guide_title1))
    }


}