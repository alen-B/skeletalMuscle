package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep11Binding
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep11ViewModel

class HighKneeGuideStep11Fragment : BaseFragment<HighKneeGuideStep11ViewModel, FragmentHightKneeGuideStep11Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep11Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.high_knee_guide_step11_title))
//        (activity as HighKneeGuideActivity).setNextButtonEnable(false)


    }

    override fun onResume() {
        super.onResume()
       if(App.sportsType == SportsType.DUMBBELL.type){
            initDumbbell()
        }else if(App.sportsType == SportsType.HAND_GRIPS.type){
            initHandGrips()
        }
    }

    private fun initDumbbell() {

        mDatabind.step2Tv.text = getString(R.string.high_knee_guide_step11_right_dumebbell_checked)
        mDatabind.step21Tv.text = getString(R.string.dumbbell_connect_right_device_step1)
        mDatabind.step22Tv.text = getString(R.string.dumbbell_connect_right_device_step2)
        mDatabind.step23Tv.text = getString(R.string.dumbbell_connect_right_device_step3)
    }
    private fun initHandGrips() {
        mDatabind.step2Tv.text = getString(R.string.high_knee_guide_step11_right_hand_grips_checked)
        mDatabind.step21Tv.text = getString(R.string.hand_grips_connect_right_device_step1)
        mDatabind.step22Tv.text = getString(R.string.hand_grips_connect_right_device_step2)
        mDatabind.step23Tv.text = getString(R.string.hand_grips_connect_right_device_step3)

    }

}