package com.fjp.skeletalmuscle.ui.highKnee

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.ImageView
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
import kotlin.math.abs
import kotlin.math.ceil

class HighKneeMainActivity : BaseActivity<HighKneeViewModel, ActivityHighKneeMainBinding>(), SMBleManager.DeviceListener {
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
    private var totalHeartRate = 0
    private var heartRateCount = 0
    private var leftLegAngleSum = 0.0
    private var rightLegAngleSum = 0.0
    private var maxPitchInCycle = 0f // 记录周期内最大的pitch值
    private var age = 1
    private var weight = 1f
    private var isMale = true
    private var seconds = 0
    private var warmupTime = 0.0
    private var fatBurningTime = 0.0
    private var cardioTime = 0.0
    private var breakTime = 0.0
    private var lastPitch = 0f // 上一个pitch值

    private var rightOldLevel = -1
    private var leftOldLevel = -1
    private var isDescending = false // 标记是否已经开始下降
    private var mediaPlayer_high: MediaPlayer? = null
    private var mediaPlayer_low: MediaPlayer? = null
    private var leftLevelViews = mutableListOf<ImageView>()
    private var rightLevelViews = mutableListOf<ImageView>()
    private val updateTimerTask = object : Runnable {
        override fun run() {
            val currentTime = if (isRunning) SystemClock.uptimeMillis() else pauseTime
            elapsedTime = currentTime - startTime
            // 检查是否暂停
            seconds++
            updateTimerTextView()
            handler.postDelayed(this, 1000)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.high_knee_main_title))
        startTimer()
        //TODO 整个流程完成后需要计算出当前用户的年龄
//        age = App.userInfo.born
//        weight = App.userInfo.weight
//            isMale = App.userInfo.sex == SEX.MAN.value

//        showOffLinePop()
        leftLevelViews.add(mDatabind.lIv1)
        leftLevelViews.add(mDatabind.lIv2)
        leftLevelViews.add(mDatabind.lIv3)
        leftLevelViews.add(mDatabind.lIv4)
        leftLevelViews.add(mDatabind.lIv5)

        rightLevelViews.add(mDatabind.rIv1)
        rightLevelViews.add(mDatabind.rIv2)
        rightLevelViews.add(mDatabind.rIv3)
        rightLevelViews.add(mDatabind.rIv4)
        rightLevelViews.add(mDatabind.rIv5)
        SMBleManager.addDeviceResultDataListener(this)
        SMBleManager.connectedDevices[DeviceType.GTS]?.let {
            SMBleManager.subscribeToNotifications(it, Constants.GTS_UUID_SERVICE, Constants.GTS_UUID_CHARACTERISTIC_WRITE)
        }
        SMBleManager.connectedDevices[DeviceType.LEFT_LEG]?.let {
            SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_CHARACTERISTIC_WRITE)
        }
        SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]?.let {
            SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_CHARACTERISTIC_WRITE)
        }

        initMediaPlayerHigh()
        initMediaPlayerLow()
    }

    private fun initMediaPlayerHigh() {
        mediaPlayer_high = MediaPlayer.create(this, R.raw.ttgg) // 假设您的音频文件放在res/raw目录下
        mediaPlayer_high!!.setOnCompletionListener { mp ->
            // 准备MediaPlayer以便下次播放
            mp.release() // 释放旧的MediaPlayer资源
            mediaPlayer_high = MediaPlayer.create(applicationContext, R.raw.ttgg) // 更新引用
        }
    }

    private fun initMediaPlayerLow() {
        mediaPlayer_low = MediaPlayer.create(this, R.raw.tgd) // 假设您的音频文件放在res/raw目录下
        mediaPlayer_low!!.setOnCompletionListener { mp ->
            // 准备MediaPlayer以便下次播放
            mp.release() // 释放旧的MediaPlayer资源
            mediaPlayer_low = MediaPlayer.create(applicationContext, R.raw.tgd) // 更新引用
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
        mDatabind.progressBar.setProgressPercentage((elapsedTime / 1000 / (mViewModel.maxTime / 100f)).toDouble(), false)
    }

    inner class ProxyClick {

        fun clickFinish() {

        }

        fun clickComplete() {

        }

        fun clickStop() {
            when (isRunning) {
                false -> startTimer()
                true -> pauseTimer()
            }
        }
    }

    fun showOffLinePop() {
        val deviceOffLinePop = DeviceOffLinePop(this@HighKneeMainActivity, object : DeviceOffLinePop.Listener {
            override fun reconnect(pop: DeviceOffLinePop) {
                pop.dismiss()
            }


        })
        val pop = XPopup.Builder(this@HighKneeMainActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(deviceOffLinePop)

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
        if (!isRunning) {
            return
        }
        val pitch = DeviceDataParse.parseData2Pitch(data)
//        val roll = DeviceDataParse.parseData2Roll(data)
//        val yaw = DeviceDataParse.parseData2Yaw(data)
        // 检查pitch是否大于100度
        if (pitch > 90) {
            // 抬腿过高，播放提示音
            // 抬腿过高，检查MediaPlayer是否已经在播放
//            if (mediaPlayer_high != null && !mediaPlayer_high!!.isPlaying) {
//                mediaPlayer_high!!.start() // 播放音频
//            }
        }
        // 更新周期内最大的pitch值
        if (pitch > maxPitchInCycle) {
            maxPitchInCycle = pitch
            isDescending = false // 当pitch值达到新的高点时，重置下降标记
        }
        // 检查是否开始下降
        // 检查是否开始下降
        if (pitch < lastPitch) {
            isDescending = true

        }
        // 如果已经开始下降，并且当前pitch值比较低，认为一个周期结束
        if (isDescending && pitch < 20) { // someLowThreshold是您定义的较低的值，比如10或更低
            // 周期结束，检查最大pitch值是否在25到30之间
            if (maxPitchInCycle >= 25 && maxPitchInCycle <= 30) {
                // 播放提示音
                if (mediaPlayer_low != null && !mediaPlayer_low!!.isPlaying()) {
                    mediaPlayer_low!!.start()
                }
            }
            // 重置周期内最大的pitch值和下降标记
            maxPitchInCycle = 0f
            isDescending = false
        }
        // 更新上一个pitch值
        lastPitch = pitch
        detectLift(pitch, true)
        mViewModel.leftLegAngle.set("L ${pitch.toInt()}°")
        val leftlevel = calculateLevelFromAngle(pitch)
        if (leftOldLevel != -1) {
            leftLevelViews[leftOldLevel].setImageDrawable(null)
        }
        leftLevelViews[leftlevel].setImageDrawable(getDrawable(R.drawable.sports_current_data_selected))
        leftOldLevel = leftlevel

    }

    override fun onRightLegData(data: ByteArray) {
        if (!isRunning) {
            return
        }
        val pitch = DeviceDataParse.parseData2Pitch(data)
        val roll = DeviceDataParse.parseData2Roll(data)
        val yaw = DeviceDataParse.parseData2Yaw(data)
        if (pitch > 90) {
            // 抬腿过高，播放提示音
            // 抬腿过高，检查MediaPlayer是否已经在播放
//            if (mediaPlayer_high != null && !mediaPlayer_high!!.isPlaying) {
//                mediaPlayer_high!!.start() // 播放音频
//            }
        }
        // 更新周期内最大的pitch值
        if (pitch > maxPitchInCycle) {
            maxPitchInCycle = pitch
            isDescending = false // 当pitch值达到新的高点时，重置下降标记
        }
        // 检查是否开始下降
        if (pitch < lastPitch) {
            isDescending = true

        }
        // 如果已经开始下降，并且当前pitch值比较低，认为一个周期结束
        if (isDescending && pitch < 20) { // someLowThreshold是您定义的较低的值，比如10或更低
            // 周期结束，检查最大pitch值是否在25到30之间
            if (maxPitchInCycle >= 25 && maxPitchInCycle <= 30) {
                // 播放提示音
                if (mediaPlayer_low != null && !mediaPlayer_low!!.isPlaying()) {
                    mediaPlayer_low!!.start()
                }
            }
            // 重置周期内最大的pitch值和下降标记
            maxPitchInCycle = 0f
            isDescending = false
        }
        // 更新上一个pitch值
        lastPitch = pitch
        detectLift(pitch, false)
        mViewModel.rightLegAngle.set("R ${pitch.toInt()}°")

        val rightlevel = calculateLevelFromAngle(pitch)
        if (rightOldLevel != -1) {
            rightLevelViews[rightOldLevel].setImageDrawable(null)
        }
        rightLevelViews[rightlevel].setImageDrawable(getDrawable(R.drawable.sports_current_data_selected))
        rightOldLevel = rightlevel
    }

    override fun onGTSData(data: ByteArray) {
        if (!isRunning) {
            return
        }
        val interestedValue = DeviceDataParse.parseData2HeartRate(data)
        if (interestedValue != -1) {
            mViewModel.heartRate.set(interestedValue.toString())
            totalHeartRate += interestedValue // interestedValue是心率的值

            heartRateCount++
            val maxHeartRate: Double = (220 - age).toDouble()
            val heartRatePercentage: Double = interestedValue / maxHeartRate
            // 更新区间时间，每次心率读数都假设是10秒钟的时间
            if (heartRatePercentage < 0.6) {
                warmupTime += 10 / 60.0 // 转换为分钟
            } else if (heartRatePercentage >= 0.6 && heartRatePercentage < 0.7) {
                fatBurningTime += 10 / 60.0 // 转换为分钟
            } else if (heartRatePercentage >= 0.7 && heartRatePercentage < 0.8) {
                cardioTime += 10 / 60.0 // 转换为分钟
            } else {
                breakTime += 10 / 60.0 // 转换为分钟
            }
            // 计算卡路里消耗
            val caloriesBurned: Double = calculateCaloriesBurned(age, weight.toDouble(), interestedValue, seconds.toFloat() / 60, isMale)
            //消耗 caloriesBurned 千卡
            //暖身激活时间 warmupTime 分钟
            //高效燃脂 fatBurningTime
            //心肺提升时间 cardioTime 分钟
            //极限突破 breakTime
        }
    }

    private fun calculateCaloriesBurned(age: Int, weight: Double, heartRate: Int, exerciseTimeMinutes: Float, isMale: Boolean): Double {
        return if (isMale) (age * 0.2017 + weight * 0.09036 + heartRate * 0.6309 - 55.0969) * exerciseTimeMinutes / 4.184 else (age * 0.074 + weight * 0.05741 + heartRate * 0.4472 - 20.4022) * exerciseTimeMinutes / 4.184
    }

    private fun detectLift(pitch: Float, isLeftLeg: Boolean) {
        // 假设pitch从接近0变化到一个介于十几到几十之间的数字再回到接近0，算一次
        if (abs(pitch) > LIFT_THRESHOLD) {
            if (isLeftLeg && !leftLegLifted) {
                leftLegLifts++
                leftLegAngleSum += pitch.toDouble()
                leftLegLifted = true
                mViewModel.leftLegCount.set(leftLegLifts.toString())
            } else if (!isLeftLeg && !rightLegLifted) {
                rightLegLifts++
                rightLegLifted = true
                rightLegAngleSum += abs(pitch).toDouble()
                mViewModel.rightLegCount.set(rightLegLifts.toString())
            }
        } else {
            if (isLeftLeg) {
                leftLegLifted = false
            } else {
                rightLegLifted = false
            }
        }
    }

    private fun calculateLevelFromAngle(angle: Float): Int {
        // 假设角度从0到90度，分成7个等级
        // 你可以根据实际需求调整这个方法
        val maxAngle = 90
        val levels = 5
        // 计算每个等级的角度
        val anglePerLevel = (maxAngle / levels).toFloat()
        // 计算当前角度对应的等级
        val level = levels - ceil((angle / anglePerLevel).toDouble()).toInt() // 反转等级
        // 确保返回的level不会超出triangleViews的大小
        val minLevel = Math.min(level, 5)
        if(minLevel>0){
            return minLevel
        }
        return 0
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimerTask)
        SMBleManager.delDeviceResultDataListener(this)
        if (mediaPlayer_high != null) {
            mediaPlayer_high!!.release() // 释放MediaPlayer资源
            mediaPlayer_high = null
        }
        if (mediaPlayer_low != null) {
            mediaPlayer_low!!.release() // 释放MediaPlayer资源
            mediaPlayer_low = null
        }
    }

}