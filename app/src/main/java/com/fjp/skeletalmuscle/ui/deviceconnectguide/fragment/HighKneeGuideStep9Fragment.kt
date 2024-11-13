package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep9Binding
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep9ViewModel

class HighKneeGuideStep9Fragment : BaseFragment<HighKneeGuideStep9ViewModel, FragmentHightKneeGuideStep9Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep9Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.high_knee_guide_step9_title))


    }

    override fun initData() {
        super.initData()

    }


}