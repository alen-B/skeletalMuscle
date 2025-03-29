package com.fjp.skeletalmuscle.ui.setting.fragment

import android.os.Bundle
import com.clj.fastble.data.BleDevice
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.databinding.FragmentSettingDeviceBinding
import com.fjp.skeletalmuscle.viewmodel.state.DeviceViewModel

class DeviceFragment : BaseFragment<DeviceViewModel, FragmentSettingDeviceBinding>() {

    companion object {
        fun newInstance() = DeviceFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        val GTSDevice = SMBleManager.connectedDevices[DeviceType.GTS]
        val leftKneeDevice = SMBleManager.connectedDevices[DeviceType.LEFT_LEG]
        val rightKneeDevice = SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]

        mDatabind.smartWatchLayout.setValue(if (GTSDevice != null) getString(R.string.setting_device_status_connected) else getString(R.string.setting_device_status_disconnect), if (GTSDevice != null) R.drawable.blue_point else 0)
        mDatabind.leftKneeLL.setValue(if (leftKneeDevice != null) getString(R.string.setting_device_status_connected) else getString(R.string.setting_device_status_disconnect), if (leftKneeDevice != null) R.drawable.blue_point else 0)
        mDatabind.rightKneeLL.setValue(if (rightKneeDevice != null) getString(R.string.setting_device_status_connected) else getString(R.string.setting_device_status_disconnect), if (rightKneeDevice != null) R.drawable.blue_point else 0)
    }


    inner class ProxyClick {

        fun clickConnectLeftKnee() {
            SMBleManager.scanDevices(DeviceType.LEFT_LEG.value, DeviceType.LEFT_LEG, object : SMBleManager.DeviceStatusListener {
                override fun disConnected() {

                }

                override fun connected(bleDevice: BleDevice) {
                    mDatabind.leftKneeLL.setValue(getString(R.string.setting_device_status_connected), R.drawable.blue_point)

                }

            })
        }

        fun clickConnectRightKnee() {
            SMBleManager.scanDevices(DeviceType.RIGHT_LEG.value, DeviceType.RIGHT_LEG, object : SMBleManager.DeviceStatusListener {
                override fun disConnected() {

                }

                override fun connected(bleDevice: BleDevice) {
                    mDatabind.rightKneeLL.setValue(getString(R.string.setting_device_status_connected), R.drawable.blue_point)
                }

            })
        }

        fun clickConnectGTSKnee() {
            SMBleManager.scanDevices(DeviceType.GTS.value, DeviceType.GTS, object : SMBleManager.DeviceStatusListener {
                override fun disConnected() {

                }

                override fun connected(bleDevice: BleDevice) {
                    mDatabind.smartWatchLayout.setValue(getString(R.string.setting_device_status_connected), R.drawable.blue_point)
                }

            })
        }
    }
}