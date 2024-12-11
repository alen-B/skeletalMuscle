package com.fjp.skeletalmuscle.app.weight.pop

import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.clj.fastble.BleManager
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class DeviceOffLinePop(context: Context, val listener: Listener) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout
    private var disConnectedAccount=0//未连接的设备数量
    interface Listener {
        fun reconnect()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_device_offline
    }

    override fun onCreate() {
        super.onCreate()

        val leftIv = findViewById<ImageView>(R.id.leftIv)
        val titleTv = findViewById<TextView>(R.id.titleTv)
        val braceletLL = findViewById<LinearLayoutCompat>(R.id.braceletLL)
        val braceletTv = findViewById<TextView>(R.id.braceletTv)
        val braceletIv = findViewById<ImageView>(R.id.braceletIv)
        val rightKneeLL = findViewById<LinearLayoutCompat>(R.id.rightKneeLL)
        val leftKneeTv = findViewById<TextView>(R.id.leftKneeTv)
        val leftKneeIv = findViewById<ImageView>(R.id.leftKneeIv)
        val leftKneeLL = findViewById<LinearLayoutCompat>(R.id.leftKneeLL)
        val rightKneeTv = findViewById<TextView>(R.id.rightKneeTv)
        val rightKneeIv = findViewById<ImageView>(R.id.rightKneeIv)
        val GTSDevice = SMBleManager.connectedDevices[DeviceType.GTS]
        if (GTSDevice == null) {
            braceletLL.setBackgroundResource(R.drawable.bg_device_offline)
            braceletTv.text = context.getString(R.string.device_off_line_bracelet)
        }
        val leftLegDevice = SMBleManager.connectedDevices[DeviceType.LEFT_LEG]
        if (leftLegDevice == null) {
            leftKneeLL.setBackgroundResource(R.drawable.bg_device_offline)
            leftKneeTv.text = context.getString(R.string.device_off_line_left_knee)
        }

        val rightLegDevice = SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]
        if (rightLegDevice == null) {
            rightKneeLL.setBackgroundResource(R.drawable.bg_device_offline)
            rightKneeTv.text = context.getString(R.string.device_off_line_right_knee)
        }
        findViewById<Button>(R.id.connectBtn).setOnClickListener {
            val gts =SMBleManager.connectedDevices[DeviceType.GTS]
            val leftLeg =SMBleManager.connectedDevices[DeviceType.LEFT_LEG]
            val rightLeg =SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]
            if(gts==null){
                disConnectedAccount++
                SMBleManager.scanDevices(DeviceType.GTS.value, DeviceType.GTS, object: SMBleManager.DeviceStatusListener{
                    override fun disConnected() {
                    }

                    override fun connected() {
                        disConnectedAccount--
                        listener.reconnect()
                    }

                })
            }
            if(leftLeg==null){
                disConnectedAccount++
                SMBleManager.scanDevices(DeviceType.LEFT_LEG.value, DeviceType.LEFT_LEG,  object: SMBleManager.DeviceStatusListener{
                    override fun disConnected() {
                    }

                    override fun connected() {
                        disConnectedAccount--
                        if(disConnectedAccount==0){
                            listener.reconnect()
                        }
                    }

                })
            }
            if(rightLeg==null){
                disConnectedAccount++
                SMBleManager.scanDevices(DeviceType.RIGHT_LEG.value, DeviceType.RIGHT_LEG,  object: SMBleManager.DeviceStatusListener{
                    override fun disConnected() {
                    }

                    override fun connected() {
                        disConnectedAccount--
                        listener.reconnect()
                    }

                })
            }

        }
        blurLayout = findViewById<BlurLayout>(R.id.blurLayout)
        blurLayout.startBlur()
    }

    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}