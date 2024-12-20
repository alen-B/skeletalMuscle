package com.fjp.skeletalmuscle.app.weight.pop

import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
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
class PlankDeviceOffLinePop(context: Context, val listener: Listener) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout
    private var disConnectedAccount = 0//未连接的设备数量

    interface Listener {
        fun reconnect(type: DeviceType)
        fun completed()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_plank_device_offline
    }

    override fun onCreate() {
        super.onCreate()
        val leftIv = findViewById<ImageView>(R.id.leftIv)
        val titleTv = findViewById<TextView>(R.id.titleTv)
        val braceletLL = findViewById<LinearLayoutCompat>(R.id.braceletLL)
        val braceletTv = findViewById<TextView>(R.id.braceletTv)
        val braceletIv = findViewById<ImageView>(R.id.braceletIv)

        val connectBtn = findViewById<Button>(R.id.connectBtn)
        val GTSDevice = SMBleManager.connectedDevices[DeviceType.GTS]
        if (GTSDevice == null) {
            disConnectedAccount++
            braceletLL.setBackgroundResource(R.drawable.bg_device_offline)
            braceletTv.text = context.getString(R.string.device_off_line_bracelet)
            connectBtn.text = appContext.getString(R.string.device_off_line_reconnect)
            braceletIv.setBackgroundResource(R.drawable.device_disconnect)
            leftIv.setBackgroundResource(R.drawable.off_line)

        }


        connectBtn.setOnClickListener {
            val gts = SMBleManager.connectedDevices[DeviceType.GTS]
            if (gts == null) {
                SMBleManager.scanDevices(DeviceType.GTS.value, DeviceType.GTS, object : SMBleManager.DeviceStatusListener {
                    override fun disConnected() {
                    }

                    override fun connected() {
                        disConnectedAccount--
                        braceletLL.setBackgroundResource(R.drawable.bg_device_connected)
                        braceletTv.text = context.getString(R.string.device_off_line_bracelet_connected)
                        braceletIv.setBackgroundResource(R.drawable.device_connected)

                        if (disConnectedAccount == 0) {
                            titleTv.text = appContext.getString(R.string.device_off_line_connected)
                            leftIv.setBackgroundResource(R.drawable.title_left_default_icon)
                            connectBtn.text = appContext.getString(R.string.device_off_line_complete)
                            listener.reconnect(DeviceType.GTS)
                        }
                    }

                })
            }

            if (gts != null) {
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