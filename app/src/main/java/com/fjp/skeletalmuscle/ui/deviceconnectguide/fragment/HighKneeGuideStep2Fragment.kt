package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep2Binding
import com.fjp.skeletalmuscle.ui.deviceconnectguide.DeviceConnectGuideActivity
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep2ViewModel
import me.hgj.jetpackmvvm.base.appContext

class HighKneeGuideStep2Fragment : BaseFragment<HighKneeGuideStep2ViewModel, FragmentHightKneeGuideStep2Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep2Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.high_knee_guide_step2_title))
        mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
        (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)

    }

    override fun onResume() {
        super.onResume()
        val GTSDevice = SMBleManager.connectedDevices.get(DeviceType.GTS)
        if (GTSDevice == null) {
            SMBleManager.scanDevices(DeviceType.GTS.value, DeviceType.GTS, object : SMBleManager.DeviceStatusListener {
                override fun disConnected() {
                    appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_connect_fail))
                }

                override fun connected() {
                    appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_connect_success))
                    showConnectedView()
                }

            })
        } else {
            showConnectedView()
        }
    }

    fun showConnectedView() {
        try {
            mViewModel.leftImg.set(R.drawable.title_left_default_icon)
            mViewModel.title.set(getString(R.string.high_knee_guide_step3_title))
            mDatabind.step2Tv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(), R.drawable.high_knee_guide3_connected), null, null, null)
            mDatabind.step2Tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_1c1c1c))
            mDatabind.step21Tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_1c1c1c))
            mDatabind.step22Tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_1c1c1c))
            mDatabind.step23Tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_1c1c1c))
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)

        } catch (_: Exception) {

        }

    }

    override fun initData() {
        super.initData()

    }


}