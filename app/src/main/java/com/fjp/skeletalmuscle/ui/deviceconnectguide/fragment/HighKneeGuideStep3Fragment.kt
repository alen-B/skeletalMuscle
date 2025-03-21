package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep3Binding
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep3ViewModel

class HighKneeGuideStep3Fragment : BaseFragment<HighKneeGuideStep3ViewModel, FragmentHightKneeGuideStep3Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep3Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.high_knee_guide_step3_title))


    }

    override fun initData() {
        super.initData()

    }


}