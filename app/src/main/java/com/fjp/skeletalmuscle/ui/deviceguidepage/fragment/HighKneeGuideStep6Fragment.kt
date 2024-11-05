package com.fjp.skeletalmuscle.ui.deviceguidepage.fragment

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.common.DeviceType
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep1Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep2Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep3Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep4Binding
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep6Binding
import com.fjp.skeletalmuscle.ui.deviceguidepage.HighKneeGuideActivity
import com.fjp.skeletalmuscle.utils.SMBleManager
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep1ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep2ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep3ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep4ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep6ViewModel

class HighKneeGuideStep6Fragment : BaseFragment<HighKneeGuideStep6ViewModel, FragmentHightKneeGuideStep6Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep6Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
    }

    override fun onResume() {
        super.onResume()
        val rightLegDevice =SMBleManager.connectedDevices.get(DeviceType.RIGHT_LEG)
        if(rightLegDevice!= null ){
            (activity as HighKneeGuideActivity).setNextButtonEnable(true)
        }else{
            (activity as HighKneeGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            mViewModel.title.set(getString(R.string.high_knee_guide_step6_title))
            SMBleManager.scanDevices(DeviceType.RIGHT_LEG.value, DeviceType.RIGHT_LEG,object: SMBleManager.DeviceStatusListener{
                override fun disConnected() {
                }

                override fun connected() {
                    showConnectedView()
                }
            })
        }
    }
    fun showConnectedView(){
        try {
            mViewModel.title.set(getString(R.string.high_knee_guide_step7_title))
            mViewModel.leftImg.set(R.drawable.title_left_default_icon)
            mDatabind.step2Tv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.high_knee_guide3_connected), null, null, null)
            mDatabind.step2Tv.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_1c1c1c))
            mDatabind.step21Tv.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_1c1c1c))
            mDatabind.step22Tv.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_1c1c1c))
            mDatabind.step23Tv.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_1c1c1c))
            (activity as HighKneeGuideActivity).setNextButtonEnable(true)
        }catch(_:Exception){

        }
    }


}