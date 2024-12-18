package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep8Binding
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep8ViewModel

class HighKneeGuideStep8Fragment : BaseFragment<HighKneeGuideStep8ViewModel, FragmentHightKneeGuideStep8Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep8Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.high_knee_guide_step8_title))
        if (App.sportsType == SportsType.HIGH_KNEE) {
            //目前默认数据是高抬腿的不需要做处理
        } else if (App.sportsType == SportsType.DUMBBELL) {
            initDumbbellView()

        } else if (App.sportsType == SportsType.HAND_GRIPS) {
            initHandGripsView()
        }

    }

    private fun initDumbbellView() {
        mDatabind.iconIv.setBackgroundResource(R.drawable.dumbbell_guide_1_icon)
        mDatabind.step12Tv.text = getString(R.string.dumbbell_left)
        mDatabind.step13Tv.text = getString(R.string.dumbbell_right)
        mDatabind.step22Tv.text = getString(R.string.dumbbell_left)
        mDatabind.step23Tv.text = getString(R.string.dumbbell_right)
    }

    private fun initHandGripsView() {
        mDatabind.iconIv.setBackgroundResource(R.drawable.hand_grips_guide_1_icon)
        mDatabind.step12Tv.text = getString(R.string.hand_grips_left_device)
        mDatabind.step13Tv.text = getString(R.string.hand_grips_right_device)
        mDatabind.step22Tv.text = getString(R.string.hand_grips_left_device)
        mDatabind.step23Tv.text = getString(R.string.hand_grips_right_device)
    }
}