package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep6Binding
import com.fjp.skeletalmuscle.ui.deviceconnectguide.DeviceConnectGuideActivity
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep6ViewModel
import me.hgj.jetpackmvvm.base.appContext

class HighKneeGuideStep6Fragment : BaseFragment<HighKneeGuideStep6ViewModel, FragmentHightKneeGuideStep6Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep6Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = Proxy()
        if (App.sportsType == SportsType.HIGH_KNEE) {
            initHighKneeView()
        } else if (App.sportsType == SportsType.DUMBBELL) {
            initDumbbellView()
        } else if (App.sportsType == SportsType.HAND_GRIPS) {
            initHandGripsView()
        } else if (App.sportsType == SportsType.ASSESSMENT) {
            mDatabind.step2Tv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.guide1_step2_2), null, null, null)
            initHighKnee()
        }
    }

    override fun onResume() {
        super.onResume()
        if (App.sportsType == SportsType.HIGH_KNEE) {
            initHighKnee()
        } else if (App.sportsType == SportsType.DUMBBELL) {
            initDumbbell()
        } else if (App.sportsType == SportsType.HAND_GRIPS) {
            initHandGrips()
        } else if (App.sportsType == SportsType.ASSESSMENT) {
            mDatabind.step2Tv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.guide1_step2_2), null, null, null)
            initHighKnee()
        }

    }

    var listener: SMBleManager.DeviceStatusListener? = object : SMBleManager.DeviceStatusListener {
        override fun disConnected() {
            appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_connect_fail))
            if (context != null) {
                mDatabind.reconnectBtn.visibility = View.VISIBLE
            }
        }

        override fun connected() {
            context?.let {
                mDatabind.reconnectBtn.visibility = View.GONE
                appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_connect_success))
                showConnectedView()
            }
        }

    }

    private fun initDumbbellView() {
        mViewModel.title.set(getString(R.string.dumbbell_connect_right_device_title))
        mDatabind.step2Tv.text = getString(R.string.dumbbell_connect_right_device_connected_title)
        mDatabind.step21Tv.text = getString(R.string.dumbbell_connect_right_device_step1)
        mDatabind.step22Tv.text = getString(R.string.dumbbell_connect_right_device_step2)
        mDatabind.step23Tv.text = getString(R.string.dumbbell_connect_right_device_step3)
        mDatabind.iconIv.setBackgroundResource(R.drawable.dumbbell)
        val rightLegDevice = SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]
        if (rightLegDevice != null) {
            showConnectedView()
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        } else {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
        }
    }

    private fun initDumbbell() {
        mViewModel.title.set(getString(R.string.dumbbell_connect_right_device_title))
        mDatabind.step2Tv.text = getString(R.string.dumbbell_connect_right_device_connected_title)
        mDatabind.step21Tv.text = getString(R.string.dumbbell_connect_right_device_step1)
        mDatabind.step22Tv.text = getString(R.string.dumbbell_connect_right_device_step2)
        mDatabind.step23Tv.text = getString(R.string.dumbbell_connect_right_device_step3)
        mDatabind.iconIv.setBackgroundResource(R.drawable.dumbbell)
        val rightLegDevice = SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]
        if (rightLegDevice != null) {
            showConnectedView()
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        } else {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            SMBleManager.scanDevices(DeviceType.RIGHT_LEG.value, DeviceType.RIGHT_LEG, listener)
        }
    }

    private fun initHandGripsView() {
        mViewModel.title.set(getString(R.string.hand_grips_connect_right_device_title))
        mDatabind.step2Tv.text = getString(R.string.hand_grips_connect_right_device_connect)
        mDatabind.step21Tv.text = getString(R.string.hand_grips_connect_right_device_step1)
        mDatabind.step22Tv.text = getString(R.string.hand_grips_connect_right_device_step2)
        mDatabind.step23Tv.text = getString(R.string.hand_grips_connect_right_device_step3)
        val rightLegDevice = SMBleManager.connectedDevices[DeviceType.RIGHT_HAND_GRIPS]
        if (rightLegDevice != null) {
            showConnectedView()
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        } else {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
        }
    }

    private fun initHandGrips() {
        mViewModel.title.set(getString(R.string.hand_grips_connect_right_device_title))
        mDatabind.step2Tv.text = getString(R.string.hand_grips_connect_right_device_connect)
        mDatabind.step21Tv.text = getString(R.string.hand_grips_connect_right_device_step1)
        mDatabind.step22Tv.text = getString(R.string.hand_grips_connect_right_device_step2)
        mDatabind.step23Tv.text = getString(R.string.hand_grips_connect_right_device_step3)
        val rightLegDevice = SMBleManager.connectedDevices[DeviceType.RIGHT_HAND_GRIPS]
        if (rightLegDevice != null) {
            showConnectedView()
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        } else {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            SMBleManager.scanDevices(DeviceType.RIGHT_HAND_GRIPS.value, DeviceType.RIGHT_HAND_GRIPS, object : SMBleManager.DeviceStatusListener {
                override fun disConnected() {
                    mDatabind.reconnectBtn.visibility = View.VISIBLE
                }

                override fun connected() {
                    showConnectedView()
                }
            })
        }

    }

    private fun initHighKneeView() {
        val rightLegDevice = SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]
        mDatabind.iconIv.setBackgroundResource(R.drawable.high_knee_guide5_icon)
        if (rightLegDevice != null) {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
            showConnectedView()
            mDatabind.step2Tv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.high_knee_guide3_connected), null, null, null)
        } else {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            mViewModel.title.set(getString(R.string.high_knee_guide_step6_title))
        }
    }

    private fun initHighKnee() {
        val rightLegDevice = SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]
        mDatabind.iconIv.setBackgroundResource(R.drawable.high_knee_guide5_icon)
        if (rightLegDevice != null) {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
            showConnectedView()
            mDatabind.step2Tv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.high_knee_guide3_connected), null, null, null)
        } else {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            mViewModel.title.set(getString(R.string.high_knee_guide_step6_title))
            connectDevice()
        }

    }

    fun showConnectedView() {
        try {
            setLayoutTitle()
            mViewModel.leftImg.set(R.drawable.title_left_default_icon)
            mDatabind.step2Tv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.high_knee_guide3_connected), null, null, null)
            mDatabind.step2Tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_1c1c1c))
            mDatabind.step21Tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_1c1c1c))
            mDatabind.step22Tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_1c1c1c))
            mDatabind.step23Tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_1c1c1c))
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        } catch (_: Exception) {

        }
    }

    private fun setLayoutTitle() {
        when (App.sportsType) {
            SportsType.HIGH_KNEE -> {
                mViewModel.title.set(getString(R.string.high_knee_guide_step7_title))
            }

            SportsType.DUMBBELL -> {
                mViewModel.title.set(getString(R.string.dumbbell_connect_right_device_connect))
            }

            SportsType.HAND_GRIPS -> {
                mViewModel.title.set(getString(R.string.hand_grips_connect_right_device_connected_title))
            }

            SportsType.ASSESSMENT -> {
                mViewModel.title.set(getString(R.string.high_knee_guide_step7_title))
            }

            else -> {}
        }

    }

    fun connectDevice() {
        SMBleManager.scanDevices(DeviceType.RIGHT_LEG.value, DeviceType.RIGHT_LEG, listener)
    }

    inner class Proxy {
        fun clickReconnect() {
            connectDevice()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }
}


