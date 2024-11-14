package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep1Binding
import com.fjp.skeletalmuscle.ui.deviceconnectguide.DeviceConnectGuideActivity
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep1ViewModel

class HighKneeGuideStep1Fragment : BaseFragment<HighKneeGuideStep1ViewModel,FragmentHightKneeGuideStep1Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep1Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        if( App.sportsType == SportsType.HIGH_KNEE.type){
        //目前默认数据是高抬腿的不需要做处理
        }else if(App.sportsType == SportsType.DUMBBELL.type){
            initDumbbellView()

        }else if(App.sportsType == SportsType.HAND_GRIPS.type){
            initHandGripsView()
        }

    }

    private fun initHandGripsView() {
        mDatabind.iconIv.setBackgroundResource(R.drawable.hand_grips_guide_1_icon)
        mDatabind.step12Tv.text = getString(R.string.hand_grips_left_device)
        mDatabind.step13Tv.text = getString(R.string.hand_grips_right_device)
    }

    private fun initDumbbellView() {
        mDatabind.iconIv.setBackgroundResource(R.drawable.dumbbell_guide_1_icon)
        mDatabind.step12Tv.text = getString(R.string.dumbbell_left)
        mDatabind.step13Tv.text = getString(R.string.dumbbell_right)
    }

    override fun initData() {
        super.initData()
        mViewModel.title.set(getString(R.string.high_knee_guide_title1))
    }


}