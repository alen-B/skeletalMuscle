package com.fjp.skeletalmuscle.app.weight.pop

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class DeviceOffLinePop(context: Context, val sportsType: SportsType, val listener: Listener) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout
    private var disConnectedAccount = 0//未连接的设备数量

    interface Listener {
        fun reconnect(type: DeviceType)
        fun completed()
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
        val connectBtn = findViewById<Button>(R.id.connectBtn)
        if (sportsType == SportsType.HIGH_KNEE) {
            val GTSDevice = SMBleManager.connectedDevices[DeviceType.GTS]
            if (GTSDevice == null) {
                disConnectedAccount++
                braceletLL.setBackgroundResource(R.drawable.bg_device_offline)
                braceletTv.text = context.getString(R.string.device_off_line_bracelet)
                connectBtn.text = appContext.getString(R.string.device_off_line_reconnect)
                braceletIv.setBackgroundResource(R.drawable.device_disconnect)
                leftIv.setBackgroundResource(R.drawable.off_line)

            }
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

        } else if (sportsType == SportsType.PLANK) {
            rightKneeLL.visibility = View.GONE
            leftKneeLL.visibility = View.GONE
            val GTSDevice = SMBleManager.connectedDevices[DeviceType.GTS]
            if (GTSDevice == null) {
                disConnectedAccount++
                braceletLL.setBackgroundResource(R.drawable.bg_device_offline)
                braceletTv.text = context.getString(R.string.device_off_line_bracelet)
                connectBtn.text = appContext.getString(R.string.device_off_line_reconnect)
                braceletIv.setBackgroundResource(R.drawable.device_disconnect)
                leftIv.setBackgroundResource(R.drawable.off_line)

            }
        } else if (sportsType == SportsType.ASSESSMENT) {
            braceletLL.visibility = View.GONE
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
        }



        connectBtn.setOnClickListener {
            val gts = SMBleManager.connectedDevices[DeviceType.GTS]
            val leftLeg = SMBleManager.connectedDevices[DeviceType.LEFT_LEG]
            val rightLeg = SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]
            if (sportsType == SportsType.HIGH_KNEE) {
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
                if (leftLeg == null) {
                    SMBleManager.scanDevices(DeviceType.LEFT_LEG.value, DeviceType.LEFT_LEG, object : SMBleManager.DeviceStatusListener {
                        override fun disConnected() {
                        }

                        override fun connected() {
                            disConnectedAccount--
                            leftKneeLL.setBackgroundResource(R.drawable.bg_device_connected)
                            leftKneeTv.text = context.getString(R.string.device_off_line_bracelet_connected)
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

                        override fun connected() {
                            disConnectedAccount--
                            rightKneeLL.setBackgroundResource(R.drawable.bg_device_connected)
                            rightKneeTv.text = context.getString(R.string.device_off_line_bracelet_connected)
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
                if (gts != null && leftLeg != null && rightLeg != null) {
                    listener.completed()
                    dismiss()
                }
            } else if (sportsType == SportsType.PLANK) {
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
            } else if (sportsType == SportsType.DUMBBELL) {
                listener.completed()
                dismiss()
            } else if (sportsType == SportsType.ASSESSMENT) {
                if (leftLeg == null) {
                    SMBleManager.scanDevices(DeviceType.LEFT_LEG.value, DeviceType.LEFT_LEG, object : SMBleManager.DeviceStatusListener {
                        override fun disConnected() {
                        }

                        override fun connected() {
                            disConnectedAccount--
                            leftKneeLL.setBackgroundResource(R.drawable.bg_device_connected)
                            leftKneeTv.text = context.getString(R.string.device_off_line_bracelet_connected)
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

                        override fun connected() {
                            disConnectedAccount--
                            rightKneeLL.setBackgroundResource(R.drawable.bg_device_connected)
                            rightKneeTv.text = context.getString(R.string.device_off_line_bracelet_connected)
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
        }
        blurLayout = findViewById<BlurLayout>(R.id.blurLayout)
        blurLayout.startBlur()
    }

    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}