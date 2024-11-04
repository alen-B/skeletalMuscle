package com.fjp.skeletalmuscle.utils

import android.bluetooth.BluetoothGatt
import android.util.Log
import androidx.compose.ui.unit.Constraints
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.callback.BleWriteCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.common.DeviceType
import com.lxj.xpopup.BuildConfig
import me.hgj.jetpackmvvm.base.appContext
import java.math.BigInteger

/**
 *Author:Mr'x
 *Time:2024/11/4
 *Description:
 */
object SMBleManager {
    val foundDevices: MutableList<BleDevice> = mutableListOf()
    val connectedDevices: MutableMap<DeviceType, BleDevice> = mutableMapOf()
    private val deviceListeners: MutableList<DeviceListener> = mutableListOf()


    interface DeviceStatusListener{
        fun disConnected()
        fun connected()
    }
    interface DeviceListener {
        fun GTSDisConnected()
        fun leftLegDisConnected()
        fun rightLegDisConnected()

        fun onLeftLegData(data: ByteArray)
        fun onRightLegData(data: ByteArray)
        fun onGTSData(data: ByteArray)

    }

    fun addDeviceResultDataListener(listener: DeviceListener) {
        deviceListeners.add(listener)
    }

    fun delDeviceResultDataListener(listener: DeviceListener) {
        deviceListeners.remove(listener)
    }

    init {
        BleManager.getInstance().init(App.instance)
        BleManager.getInstance().enableLog(BuildConfig.DEBUG).setReConnectCount(1, 5000).setSplitWriteNum(20).setConnectOverTime(10000).operateTimeout = 5000
    }

    fun scanDevices(devicePrefix: String, deviceType: DeviceType,listener:DeviceStatusListener) {
        BleManager.getInstance().scan(object : BleScanCallback() {

            override fun onScanStarted(success: Boolean) {
                Log.d("BLE", "Scan started: $success")
                appContext.showToast(appContext.getString(R.string.bluetooth_scaning))
                foundDevices.clear()
            }

            override fun onLeScan(bleDevice: BleDevice) {
                super.onLeScan(bleDevice)
                Log.d("BLE", "Found device: " + bleDevice.name)
                if (bleDevice.name != null && bleDevice.name.startsWith(devicePrefix)) {
                    foundDevices.add(bleDevice)
                    connectToDevice(bleDevice.mac,deviceType,listener)
                    BleManager.getInstance().cancelScan() // 停止扫描
                }
            }

            override fun onScanning(bleDevice: BleDevice) {
                // 在这里可以根据需要更新扫描进度的 UI
                Log.d("BLE", "Found device: " + bleDevice.name)
            }

            override fun onScanFinished(scanResultList: MutableList<BleDevice>?) {
                if (foundDevices.isEmpty()) {
                    Log.d("BLE", "No devices found with prefix: $devicePrefix")
                    appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_not_find))
                }
            }

        })
    }

    private fun connectToDevice(mac: String, deviceType: DeviceType,listener:DeviceStatusListener) {
        BleManager.getInstance().connect(mac, object : BleGattCallback() {
            override fun onStartConnect() {
                // 连接开始，弹出Toast消息
                appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_start_connect))
            }

            override fun onConnectFail(bleDevice: BleDevice, exception: BleException) {
                listener.disConnected()
                appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_connect_fail) + exception?.description)
            }

            override fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt, status: Int) {
                appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_connect_success) + bleDevice.name)
                connectedDevices[deviceType] = bleDevice
                println("connectedDevices[deviceType]:${connectedDevices[deviceType]}   name:"+bleDevice.name)
                if(deviceType === DeviceType.GTS){
                    subscribeToNotifications(bleDevice,Constants.GTS_UUID_SERVICE,Constants.GTS_UUID_NOTIFY_CHAR)
                }
                listener.connected()
            }

            override fun onDisConnected(isActiveDisConnected: Boolean, device: BleDevice, gatt: BluetoothGatt, status: Int) {
                listener.disConnected()
                val msg = if (isActiveDisConnected) "断开连接：" else "连接丢失："
                appContext.showToast(msg + device.name)
                // 根据断开连接的设备名称,将对应的按钮设置为半透明状态
                val deviceName = device.name
                if (deviceName != null) {
                    deviceListeners.forEach {
                        if (deviceType == DeviceType.LEFT_LEG) {
                            it.leftLegDisConnected()
                        } else if (deviceType == DeviceType.RIGHT_LEG) {
                            it.rightLegDisConnected()
                        } else if (deviceType == DeviceType.GTS) {
                            it.GTSDisConnected()
                        }

                    }
                }

                // 从已连接设备列表中移除断开连接的设备
                connectedDevices.remove(deviceType)
            }
        })
    }
    fun subscribeToNotifications(bleDevice: BleDevice, uuidService: String, uuidNotify: String) {
        val deviceName: String = bleDevice.name
        BleManager.getInstance().notify(bleDevice, uuidService, uuidNotify, object : BleNotifyCallback() {
            override fun onNotifySuccess() {
                // 订阅成功，可以在这里发送获取数据的指令
                Log.d("subscribeToNotifications", "deviceName:"+deviceName)
                if(deviceName.startsWith("GTS")){
                    writeDataToBleDevice(bleDevice)
                }
            }

            override fun onNotifyFailure(exception: BleException) {
                // 订阅失败，处理异常
                Log.e("subscribeToNotifications", "Notify failed: " + exception.description)
            }

            override fun onCharacteristicChanged(data: ByteArray) {
                // 设备返回的数据将在这里接收
//                DeviceDataParse.handleNotifyData(data)
                deviceListeners.forEach {
                    if (deviceName.startsWith("GTS")) {
                        it.onGTSData(data)
                    } else if (deviceName.startsWith("WT901BLE67L")) {
                        it.onRightLegData(data)

                    } else if (deviceName.startsWith("WT901BLE67R")) {
                        it.onLeftLegData(data)
                    }

                }
            }
        })
    }

    private fun writeDataToBleDevice(bleDevice: BleDevice) {

        // 将十六进制字符串转换为字节数组
        val hexCommand = "DA420200630183BD"
        var command = BigInteger(hexCommand, 16).toByteArray()
        // 如果BigInteger解释为正数它会在开头添加额外的0x00，所以需要移除
        if (command[0].toInt() == 0x00) {
            val tmp = ByteArray(command.size - 1)
            System.arraycopy(command, 1, tmp, 0, tmp.size)
            command = tmp
        }
        BleManager.getInstance().write(bleDevice, Constants.GTS_UUID_SERVICE,  // 你的设备服务UUID
            Constants.GTS_UUID_CHARACTERISTIC_WRITE,  // 你的设备特征UUID
            command, object : BleWriteCallback() {
                override fun onWriteSuccess(current: Int, total: Int, justWrite: ByteArray) {
                    // 写入数据成功，根据需要更新UI或其他操作
                    Log.d("writeDataToBleDevice", "Write successful")
                }

                override fun onWriteFailure(exception: BleException) {
                    // 写入数据失败，处理异常
                    Log.d("writeDataToBleDevice", "Write failed: " + exception.description)
                }
            })
    }


}