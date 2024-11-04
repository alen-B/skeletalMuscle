package com.fjp.skeletalmuscle.ui.highKnee

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.common.DeviceType
import com.fjp.skeletalmuscle.databinding.ActivityHighKneeMainBinding
import com.fjp.skeletalmuscle.utils.DeviceDataParse
import com.fjp.skeletalmuscle.utils.SMBleManager
import com.fjp.skeletalmuscle.view.pop.DeviceOffLinePop
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeViewModel
import com.lxj.xpopup.XPopup

class HighKneeMainActivity : BaseActivity<HighKneeViewModel,ActivityHighKneeMainBinding>(), SMBleManager.DeviceListener {
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var isRunning: Boolean = false
    private var pauseTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())
    private val LIFT_THRESHOLD = 16.6f // 抬起动作的阈值

    private var leftLegLifted = false
    private var rightLegLifted = false
    private var leftLegLifts = 0
    private var rightLegLifts = 0
    private val totalHeartRate = 0
    private val heartRateCount = 0
    private var leftLegAngleSum = 0.0
    private var rightLegAngleSum = 0.0
    private val maxPitchInCycle = 0f // 记录周期内最大的pitch值

    private val lastPitch = 0f // 上一个pitch值

    private val isDescending = false // 标记是否已经开始下降

    private val updateTimerTask = object : Runnable {
        override fun run() {
            val currentTime = if (isRunning) SystemClock.uptimeMillis() else pauseTime
            elapsedTime = currentTime - startTime
            updateTimerTextView()
            handler.postDelayed(this, 1000)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.high_knee_main_title))
        startTimer()
//        showOffLinePop()
        SMBleManager.addDeviceResultDataListener(this)
        SMBleManager.connectedDevices[DeviceType.GTS]?.let {
            SMBleManager.subscribeToNotifications(it, Constants.GTS_UUID_SERVICE,Constants.GTS_UUID_CHARACTERISTIC_WRITE)
        }
        SMBleManager.connectedDevices[DeviceType.LEFT_LEG]?.let {
            SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE,Constants.LEG__UUID_CHARACTERISTIC_WRITE)
        }
        SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]?.let {
            SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE,Constants.LEG__UUID_CHARACTERISTIC_WRITE)
        }

    }

    private fun startTimer() {
        if (!isRunning) {
            startTime = SystemClock.uptimeMillis() - elapsedTime
            isRunning = true
            mDatabind.stopBtn.text = getString(R.string.high_knee_main_stop)
            handler.post(updateTimerTask)
            val drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.stop_icon)
            mDatabind.stopBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
        }
    }

    private fun pauseTimer() {
        if (isRunning) {
            isRunning = false
            pauseTime = SystemClock.uptimeMillis()
            mDatabind.stopBtn.text = getString(R.string.high_knee_main_continue)
            handler.removeCallbacks(updateTimerTask)

            val drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.star_icon)
            mDatabind.stopBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
        }
    }

    private fun updateTimerTextView() {
        val minutes = ((elapsedTime / (1000 * 60)) % 60).toInt()
        val seconds = ((elapsedTime / 1000) % 60).toInt()
        val timeString = String.format("%02d:%02d", minutes, seconds)
        mViewModel.curTime.set(timeString)
        mDatabind.progressBar.setProgressPercentage((elapsedTime/1000/(mViewModel.maxTime/100f)).toDouble(),false)
    }
    inner class ProxyClick{

        fun clickFinish(){

        }
        fun clickComplete(){

        }
        fun clickStop(){
            when (isRunning) {
                false -> startTimer()
                true -> pauseTimer()
            }
        }
    }
    fun showOffLinePop(){
        val deviceOffLinePop = DeviceOffLinePop(this@HighKneeMainActivity,object: DeviceOffLinePop.Listener{
            override fun reconnect(pop:DeviceOffLinePop) {
                pop.dismiss()
            }


        })
        val pop = XPopup.Builder(this@HighKneeMainActivity)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .isDestroyOnDismiss(true)
            .autoOpenSoftInput(false)
            .asCustom(deviceOffLinePop)

        pop.show()
    }

    override fun GTSDisConnected() {
        showOffLinePop()
    }

    override fun leftLegDisConnected() {
        showOffLinePop()
    }

    override fun rightLegDisConnected() {
        showOffLinePop()
    }

    override fun onLeftLegData(data: ByteArray) {
        if (!isRunning){
            return
        }
        val angle = DeviceDataParse.parseData2Angle(data)
        if(angle!=0f){
//            println("左腿角度"+angle)
        }

    }

    override fun onRightLegData(data: ByteArray) {
        if (!isRunning){
            return
        }
        val angle = DeviceDataParse.parseData2Angle(data)
        if(angle!=0f){
//            println("右腿角度"+angle)
        }
    }

    override fun onGTSData(data: ByteArray) {
        if (!isRunning){
            return
        }
       val heartRate = DeviceDataParse.parseData2HeartRate(data)
        if(heartRate!=-1){
            mViewModel.heartRate.set(heartRate.toString())
        }
    }
    private fun detectLift(pitch: Float, isLeftLeg: Boolean) {
        // 假设pitch从接近0变化到一个介于十几到几十之间的数字再回到接近0，算一次
        if (Math.abs(pitch) > LIFT_THRESHOLD) {
            if (isLeftLeg && !leftLegLifted) {
                leftLegLifts++
                leftLegAngleSum += pitch.toDouble()
                leftLegLifted = true
            } else if (!isLeftLeg && !rightLegLifted) {
                rightLegLifts++
                rightLegLifted = true
                rightLegAngleSum += Math.abs(pitch).toDouble()
            }
        } else {
            if (isLeftLeg) {
                leftLegLifted = false
            } else {
                rightLegLifted = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SMBleManager.delDeviceResultDataListener(this)
    }

}