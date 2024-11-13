package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep6Binding
import com.fjp.skeletalmuscle.ui.deviceconnectguide.DeviceConnectGuideActivity
import com.fjp.skeletalmuscle.app.util.SMBleManager
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
        if(App.sportsType == SportsType.HIGH_KNEE.type){
            initHighKnee()
        }else if(App.sportsType == SportsType.DUMBBELL.type){
            initDumbbell()
        }else if(App.sportsType == SportsType.HAND_GRIPS.type){
            initHandGrips()
        }

    }

    private fun initDumbbell() {
        mViewModel.title.set(getString(R.string.dumbbell_connect_right_device_title))
        mDatabind.step2Tv.text = getString(R.string.dumbbell_connect_right_device_connected_title)
        mDatabind.step21Tv.text = getString(R.string.dumbbell_connect_right_device_step1)
        mDatabind.step22Tv.text = getString(R.string.dumbbell_connect_right_device_step2)
        mDatabind.step23Tv.text = getString(R.string.dumbbell_connect_right_device_step3)
        val rightLegDevice = SMBleManager.connectedDevices.get(DeviceType.RIGHT_DUMBBELL)
        if(rightLegDevice!= null ){
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        }else{
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            SMBleManager.scanDevices(DeviceType.RIGHT_DUMBBELL.value, DeviceType.RIGHT_DUMBBELL,object: SMBleManager.DeviceStatusListener{
                override fun disConnected() {
                }

                override fun connected() {
                    showConnectedView()
                }
            })
        }

    }

    private fun initHandGrips() {
        mViewModel.title.set(getString(R.string.hand_grips_connect_right_device_title))
        mDatabind.step2Tv.text = getString(R.string.hand_grips_connect_right_device_connect)
        mDatabind.step21Tv.text = getString(R.string.hand_grips_connect_right_device_step1)
        mDatabind.step22Tv.text = getString(R.string.hand_grips_connect_right_device_step2)
        mDatabind.step23Tv.text = getString(R.string.hand_grips_connect_right_device_step3)
        val rightLegDevice = SMBleManager.connectedDevices.get(DeviceType.RIGHT_HAND_GRIPS)
        if(rightLegDevice!= null ){
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        }else{
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            SMBleManager.scanDevices(DeviceType.RIGHT_HAND_GRIPS.value, DeviceType.RIGHT_HAND_GRIPS,object: SMBleManager.DeviceStatusListener{
                override fun disConnected() {
                }

                override fun connected() {
                    showConnectedView()
                }
            })
        }
    }

    private fun initHighKnee() {
        val rightLegDevice = SMBleManager.connectedDevices.get(DeviceType.RIGHT_LEG)
        if(rightLegDevice!= null ){
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        }else{
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
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
            setLayoutTitle()
            mViewModel.leftImg.set(R.drawable.title_left_default_icon)
            mDatabind.step2Tv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.high_knee_guide3_connected), null, null, null)
            mDatabind.step2Tv.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_1c1c1c))
            mDatabind.step21Tv.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_1c1c1c))
            mDatabind.step22Tv.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_1c1c1c))
            mDatabind.step23Tv.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_1c1c1c))
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        }catch(_:Exception){

        }
    }

    private fun setLayoutTitle() {
        when (App.sportsType) {
            SportsType.HIGH_KNEE.type -> {
                mViewModel.title.set(getString(R.string.high_knee_guide_step5_title))
            }
            SportsType.DUMBBELL.type -> {
                mViewModel.title.set(getString(R.string.dumbbell_connect_left_device_connect))
            }
            SportsType.HAND_GRIPS.type -> {
                mViewModel.title.set(getString(R.string.hand_grips_connect_left_device_connected_title))
            }
        }

    }


}