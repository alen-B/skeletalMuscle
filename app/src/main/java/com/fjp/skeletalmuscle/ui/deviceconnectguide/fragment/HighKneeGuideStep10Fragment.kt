package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep10Binding
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep10ViewModel

class HighKneeGuideStep10Fragment : BaseFragment<HighKneeGuideStep10ViewModel, FragmentHightKneeGuideStep10Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep10Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.high_knee_guide_step10_title))


    }

    override fun initData() {
        super.initData()

    }


}