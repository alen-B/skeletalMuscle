package com.fjp.skeletalmuscle.app.weight.pop

import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.clj.fastble.data.BleDevice
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class AssessmentDeviceOffLinePop(context: Context, val listener: Listener) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout
    private var disConnectedAccount = 0//未连接的设备数量

    interface Listener {
        fun reconnect(type: DeviceType)
        fun completed()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_assessment_device_offline
    }

    override fun onCreate() {
        super.onCreate()
        val leftIv = findViewById<ImageView>(R.id.leftIv)
        val titleTv = findViewById<TextView>(R.id.titleTv)

        val rightKneeLL = findViewById<LinearLayoutCompat>(R.id.rightKneeLL)
        val leftKneeTv = findViewById<TextView>(R.id.leftKneeTv)
        val leftKneeIv = findViewById<ImageView>(R.id.leftKneeIv)
        val leftKneeLL = findViewById<LinearLayoutCompat>(R.id.leftKneeLL)
        val rightKneeTv = findViewById<TextView>(R.id.rightKneeTv)
        val rightKneeIv = findViewById<ImageView>(R.id.rightKneeIv)
        val connectBtn = findViewById<Button>(R.id.connectBtn)

        val leftLegDevice = SMBleManager.connectedDevices[DeviceType.LEFT_LEG]
        if (leftLegDevice == null) {
            disConnectedAccount++
            leftKneeLL.setBackgroundResource(R.drawable.bg_device_offline)
            leftKneeTv.text = context.getString(R.string.device_off_line_left_knee)
            connectBtn.text = appContext.getString(R.string.device_off_line_reconnect)
            leftIv.setBackgroundResource(R.drawable.off_line)
            leftKneeIv.setBackgroundResource(R.drawable.device_disconnect)
        }

        val rightLegDevice = SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]
        if (rightLegDevice == null) {
            disConnectedAccount++
            rightKneeLL.setBackgroundResource(R.drawable.bg_device_offline)
            leftIv.setBackgroundResource(R.drawable.off_line)
            rightKneeTv.text = context.getString(R.string.device_off_line_right_knee)
            connectBtn.text = appContext.getString(R.string.device_off_line_reconnect)
            rightKneeIv.setBackgroundResource(R.drawable.device_disconnect)
        }


        connectBtn.setOnClickListener {
            val leftLeg = SMBleManager.connectedDevices[DeviceType.LEFT_LEG]
            val rightLeg = SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]
            if (leftLeg == null) {
                SMBleManager.scanDevices(DeviceType.LEFT_LEG.value, DeviceType.LEFT_LEG, object : SMBleManager.DeviceStatusListener {
                    override fun disConnected() {
                    }

                    override fun connected(bleDevice: BleDevice) {
                        disConnectedAccount--
                        leftKneeLL.setBackgroundResource(R.drawable.bg_device_connected)
                        leftKneeTv.text = context.getString(R.string.device_off_line_left_knee_connected)
                        leftKneeIv.setBackgroundResource(R.drawable.device_connected)
                        if (disConnectedAccount == 0) {
                            titleTv.text = appContext.getString(R.string.device_off_line_connected)
                            leftIv.setBackgroundResource(R.drawable.title_left_default_icon)
                            connectBtn.text = appContext.getString(R.string.device_off_line_complete)
                            listener.reconnect(DeviceType.LEFT_LEG)
                        }
                    }

                })
            }
            if (rightLeg == null) {
                SMBleManager.scanDevices(DeviceType.RIGHT_LEG.value, DeviceType.RIGHT_LEG, object : SMBleManager.DeviceStatusListener {
                    override fun disConnected() {
                    }

                    override fun connected(bleDevice: BleDevice) {
                        disConnectedAccount--
                        rightKneeLL.setBackgroundResource(R.drawable.bg_device_connected)
                        rightKneeTv.text = context.getString(R.string.device_off_line_right_knee_connected)
                        rightKneeIv.setBackgroundResource(R.drawable.device_connected)
                        if (disConnectedAccount == 0) {
                            titleTv.text = appContext.getString(R.string.device_off_line_connected)
                            leftIv.setBackgroundResource(R.drawable.title_left_default_icon)
                            connectBtn.text = appContext.getString(R.string.device_off_line_complete)
                            listener.reconnect(DeviceType.RIGHT_LEG)
                        }
                    }

                })
            }
            if (leftLeg != null && rightLeg != null) {
                listener.completed()
                dismiss()
            }
        }
        blurLayout = findViewById(R.id.blurLayout)
        blurLayout.startBlur()
    }

    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}