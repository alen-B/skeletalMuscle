package com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.BleScanHelper
import com.fjp.skeletalmuscle.app.util.DeviceDataParse
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.data.model.bean.BleDevice
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.FragmentHightKneeGuideStep4Binding
import com.fjp.skeletalmuscle.ui.deviceconnectguide.DeviceConnectGuideActivity
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideStep4ViewModel
import me.hgj.jetpackmvvm.base.appContext


class HighKneeGuideStep4Fragment : BaseFragment<HighKneeGuideStep4ViewModel, FragmentHightKneeGuideStep4Binding>() {

    companion object {
        fun newInstance() = HighKneeGuideStep4Fragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = Proxy()
    }

    override fun onResume() {
        super.onResume()
        if (App.sportsType == SportsType.HIGH_KNEE.type) {
            initHighKnee()
        } else if (App.sportsType == SportsType.DUMBBELL.type) {
            initDumbbell()
        } else if (App.sportsType == SportsType.HAND_GRIPS.type) {
            initHandGrips()
        }

    }

    private fun initHighKnee() {
        val leftLegDevice = SMBleManager.connectedDevices[DeviceType.LEFT_LEG]
        if (leftLegDevice != null) {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        } else {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            mViewModel.title.set(getString(R.string.high_knee_guide_step4_title))
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            SMBleManager.scanDevices(DeviceType.LEFT_LEG.value, DeviceType.LEFT_LEG, object : SMBleManager.DeviceStatusListener {
                override fun disConnected() {

                }

                override fun connected() {
                    showConnectedView()
                }

            })
        }
    }

    private val bluetoothReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
                val state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR)
                when (state) {
                    BluetoothDevice.BOND_BONDING -> Log.d("Bluetooth", "配对中...")
                    BluetoothDevice.BOND_BONDED -> Log.d("Bluetooth", "配对成功")
                    BluetoothDevice.BOND_NONE -> Log.d("Bluetooth", "配对失败")
                }
            } else if (BluetoothDevice.ACTION_FOUND == action) {
                val scanDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                println("扫描到的设备:" + scanDevice?.name)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initHandGrips() {
        mViewModel.title.set(getString(R.string.hand_grips_connect_left_device_title))
        mDatabind.step2Tv.text = getString(R.string.hand_grips_connect_left_device_connect)
        mDatabind.step21Tv.text = getString(R.string.hand_grips_connect_left_device_step1)
        mDatabind.step22Tv.text = getString(R.string.hand_grips_connect_left_device_step2)
        mDatabind.step23Tv.text = getString(R.string.hand_grips_connect_left_device_step3)
        val leftLegDevice = SMBleManager.connectedDevices[DeviceType.LEFT_HAND_GRIPS]
        if (leftLegDevice != null) {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        } else {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            mViewModel.title.set(getString(R.string.hand_grips_connect_left_device_title))
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
//            SMBleManager.scanDevices(DeviceType.LEFT_HAND_GRIPS.value, DeviceType.LEFT_HAND_GRIPS,object: SMBleManager.DeviceStatusListener{
//                override fun disConnected() {
//                    appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_connect_fail))
//                }
//
//                override fun connected() {
//                    showConnectedView()
//                }
//
//
//            })
            initBluetooth()
        }
    }

    /**
     * 初始化蓝牙
     */
    //蓝牙设配器
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    val OpenBluetooth_Request_Code = 10086

    //蓝牙扫描辅助类
    private lateinit var mBleScanHelper: BleScanHelper
    private var maxGrip = 0
    private fun initBluetooth() {
        //初始化ble设配器
        val manager = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = manager.adapter
        //判断蓝牙是否开启，如果关闭则请求打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, OpenBluetooth_Request_Code)
        }
        //创建扫描辅助类
        mBleScanHelper = BleScanHelper(activity as Context)
        mBleScanHelper.setOnScanListener(object : BleScanHelper.onScanListener {
            @SuppressLint("MissingPermission")
            override fun onNext(device: BleDevice) {
                if (device.device.name != null && device.device.name.startsWith(DeviceType.LEFT_HAND_GRIPS.value)) {
                    val rawDataStr = DeviceDataParse.bytesToHexString(device.scanRecordBytes)
                    println("====扫描到的握力数据数据是：" + rawDataStr)
                    var lengthStr = rawDataStr?.substring(46, 50)
                    //将长度转10进制
                    println("====扫描到的握力数据数据是lengthStr：" + lengthStr)
                    val grip = Integer.parseInt(lengthStr, 16)
                    println("====扫描到的握力是：" + grip / 10f + "kg")
                    if (maxGrip < grip) {
                        maxGrip = grip
                    }
                    println("====最大握力数据：" + maxGrip)
                }
            }

            override fun onFinish() {
            }
        })
        mBleScanHelper.startScanBle(1)
    }

    private fun initDumbbell() {
        mViewModel.title.set(getString(R.string.dumbbell_connect_left_device_title))
        mDatabind.step2Tv.text = getString(R.string.dumbbell_connect_left_device_connected_title)
        mDatabind.step21Tv.text = getString(R.string.dumbbell_connect_left_device_step1)
        mDatabind.step22Tv.text = getString(R.string.dumbbell_connect_left_device_step2)
        mDatabind.step23Tv.text = getString(R.string.dumbbell_connect_left_device_step3)
        val leftLegDevice = SMBleManager.connectedDevices[DeviceType.LEFT_DUMBBELL]
        if (leftLegDevice != null) {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(true)
        } else {
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
            mViewModel.title.set(getString(R.string.dumbbell_connect_left_device_title))
            (activity as DeviceConnectGuideActivity).setNextButtonEnable(false)
            connectDevice()
        }
    }

    fun connectDevice() {
        SMBleManager.scanDevices(DeviceType.LEFT_DUMBBELL.value, DeviceType.LEFT_DUMBBELL, object : SMBleManager.DeviceStatusListener {
            override fun disConnected() {
                appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_connect_fail))
                mDatabind.reconnectBtn.visibility = View.VISIBLE

            }

            override fun connected() {
                appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_connect_success))
                mDatabind.reconnectBtn.visibility = View.GONE
                showConnectedView()
            }

        })
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

    inner class Proxy {
        fun clickReconnect() {
            connectDevice()
        }
    }


}