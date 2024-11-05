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
import com.fjp.skeletalmuscle.ui.deviceguidepage.HighKneeGuideActivity
import com.fjp.skeletalmuscle.utils.SMBleManager
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep1ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep2ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep3ViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep4ViewModel

class HighKneeGuideStep4Fragment : BaseFragment<HighKneeGuideStep4ViewModel, FragmentHightKneeGuideStep4Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep4Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel

    }

    override fun onResume() {
        super.onResume()
        val leftLegDevice = SMBleManager.connectedDevices[DeviceType.LEFT_LEG]
        if(leftLegDevice!= null ){
            (activity as HighKneeGuideActivity).setNextButtonEnable(true)
        }else{
            (activity as HighKneeGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            mViewModel.title.set(getString(R.string.high_knee_guide_step4_title))
            (activity as HighKneeGuideActivity).setNextButtonEnable(false)
            SMBleManager.scanDevices(DeviceType.LEFT_LEG.value, DeviceType.LEFT_LEG,object: SMBleManager.DeviceStatusListener{
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
            mViewModel.title.set(getString(R.string.high_knee_guide_step5_title))
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