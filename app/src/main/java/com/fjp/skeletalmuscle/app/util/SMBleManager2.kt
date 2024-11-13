package com.fjp.skeletalmuscle.app.util

import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import cn.com.heaton.blelibrary.ble.BleLog
import cn.com.heaton.blelibrary.ble.callback.BleConnectCallback
import cn.com.heaton.blelibrary.ble.callback.BleNotifyCallback
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback
import cn.com.heaton.blelibrary.ble.callback.BleWriteCallback
import cn.com.heaton.blelibrary.ble.model.BleDevice
import cn.com.heaton.blelibrary.ble.utils.ByteUtils
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.ext.showToast
import me.hgj.jetpackmvvm.base.appContext
import java.math.BigInteger
import java.util.UUID


/**
 *Author:Mr'x
 *Time:2024/11/10
 *Description:
 */
object SMBleManager2 {
    val TAG = "SMBleManager"
    interface ConnectListener{
        fun connected()
        fun connectFailed()
    }
    val foundDevices: MutableList<BleDevice> = mutableListOf()
    interface DeviceDataListener{
        fun onLeftLegData(data: ByteArray)
        fun onRightLegData(data: ByteArray)
        fun onGTSData(data: ByteArray)
        fun onLeftHandGripsData(data: ByteArray)
        fun onRightHandGripsData(data: ByteArray)
    }
    private val deviceDataListeners: MutableList<DeviceDataListener> = mutableListOf()
  fun starScan(devicePrefix: String, deviceType: DeviceType,listener: ConnectListener){
      App.mBle.startScan(object : BleScanCallback<BleDevice?>() {
          override fun onLeScan(bleDevice: BleDevice?, rssi: Int, scanRecord: ByteArray?) {
              //Scanned devices
              bleDevice?.let {
                  Log.d("BLE", "Found device: " + it.bleName)
                  if (it.bleName != null && it.bleName.startsWith(devicePrefix)) {
                      App.mBle.stopScan()
                      foundDevices.add(it)
                      connectToDevice(it, deviceType, listener)

                  }
              }
          }

          override fun onStart() {
              super.onStart()
              appContext.showToast(appContext.getString(R.string.bluetooth_scaning))
              SMBleManager.foundDevices.clear()
          }

          override fun onStop() {
              super.onStop()
              if (foundDevices.isEmpty()) {
                  Log.d("BLE", "No devices found with prefix: $devicePrefix")
                  appContext.showToast(appContext.getString(R.string.bluetooth_scaning_device_not_find))
              }
          }

          override fun onScanFailed(errorCode: Int) {
              super.onScanFailed(errorCode)
              Log.e(TAG, "ScanFailed:errorCode$errorCode")
              listener.connectFailed()

          }
      })
  }

    private fun connectToDevice(bleDevice: BleDevice, deviceType: DeviceType, listener: ConnectListener) {
        App.mBle.connect(bleDevice,object: BleConnectCallback<BleDevice>() {
            override fun onConnectionChanged(device: BleDevice?) {

            }

            override fun onConnectCancel(device: BleDevice?) {
                super.onConnectCancel(device)
            }

            override fun onConnectFailed(device: BleDevice?, errorCode: Int) {
                super.onConnectFailed(device, errorCode)
                listener.connectFailed()

            }

            override fun onReady(device: BleDevice?) {
                super.onReady(device)
                listener.connected()
                device?.let {
                    if(deviceType === DeviceType.GTS){
                        subscribeToNotifications(bleDevice, Constants.GTS_UUID_SERVICE, Constants.GTS_UUID_NOTIFY_CHAR)
                    }
                }
            }
        })
    }

    private fun subscribeToNotifications(bleDevice: BleDevice, gtsUuidService: String, gtsUuidNotifyChar: String) {
        App.mBle.enableNotifyByUuid(bleDevice, true, UUID.fromString(gtsUuidService),UUID.fromString(gtsUuidNotifyChar), object : BleNotifyCallback<BleDevice?>() {
            override fun onChanged(device: BleDevice?, characteristic: BluetoothGattCharacteristic) {
                val uuid = characteristic.uuid
                BleLog.e(TAG, "onChanged==uuid:$uuid")
                BleLog.e(TAG, "onChanged==data:" + ByteUtils.toHexString(characteristic.value))
                val deviceName = bleDevice.bleName
                deviceDataListeners.forEach {
                    if (deviceName.startsWith(DeviceType.GTS.value)) {
                        it.onGTSData(characteristic.value)
                    } else if (deviceName.startsWith(DeviceType.LEFT_LEG.value)) {
                        it.onLeftLegData(characteristic.value)

                    } else if (deviceName.startsWith(DeviceType.RIGHT_LEG.value)) {
                        it.onRightLegData(characteristic.value)
                    } else if (deviceName.startsWith(DeviceType.LEFT_HAND_GRIPS.value)){
                        it.onLeftHandGripsData(characteristic.value)
                    }else if (deviceName.startsWith(DeviceType.RIGHT_HAND_GRIPS.value)){
                        it.onRightLegData(characteristic.value)
                    }

                }
            }

            fun onNotifySuccess(device: BleDevice) {
                super.onNotifySuccess(device)
                BleLog.e(TAG, "onNotifySuccess: " + device.bleName)
                if(device.bleName.startsWith("GTS")){
                    writeDataToBleDevice(bleDevice)
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
        App.mBle.write(bleDevice, command, object : BleWriteCallback<BleDevice>() {
            override fun onWriteSuccess(device: BleDevice?, characteristic: BluetoothGattCharacteristic?) {}
            override fun onWriteFailed(device: BleDevice?, failedCode: Int) {
                super.onWriteFailed(device, failedCode)
            }
        })

    }


}