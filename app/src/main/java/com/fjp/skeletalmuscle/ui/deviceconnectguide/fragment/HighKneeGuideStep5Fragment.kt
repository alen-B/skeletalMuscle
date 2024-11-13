package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep5Binding
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep5ViewModel

class HighKneeGuideStep5Fragment : BaseFragment<HighKneeGuideStep5ViewModel, FragmentHightKneeGuideStep5Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep5Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.high_knee_guide_step5_title))


    }

    override fun initData() {
        super.initData()

    }


}