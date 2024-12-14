package com.fjp.skeletalmuscle.ui.sports

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.util.DeviceDataParse
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.app.weight.pop.DeviceOffLinePop
import com.fjp.skeletalmuscle.data.model.bean.Calorie
import com.fjp.skeletalmuscle.data.model.bean.FlatSupportRequest
import com.fjp.skeletalmuscle.data.model.bean.HeartRate
import com.fjp.skeletalmuscle.data.model.bean.HeartRateLevel
import com.fjp.skeletalmuscle.data.model.bean.HighKneeSports
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityPlankBinding
import com.fjp.skeletalmuscle.viewmodel.state.PlankViewModel
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.DateUtils
import java.util.Date
import java.util.Random
import kotlin.math.abs

class PlankActivity : BaseActivity<PlankViewModel, ActivityPlankBinding>(), SMBleManager.DeviceListener {
    private var requestStartTime: Long = System.currentTimeMillis() / 1000
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var isRunning: Boolean = false
    private var pauseTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())
    private var totalHeartRate = 0
    private var maxHeartRate = 0//最大心率
    private var minHeartRate = 0//最小心率
    private var heartRateCount = 0
    private var sportsMinutes = 0 //运动了多少分钟
    private var caloriesBurned: Double = 0.0 //消耗了多少千卡
    private var age = 1
    private var weight = 1.0
    private var isMale = true
    private var seconds = 0
    private var score = 0

    private var warmupTime = 0
    private var fatBurningTime = 0
    private var cardioTime = 0
    private var breakTime = 0

    private var calories: MutableList<Calorie> = arrayListOf()
    private var heartRate: MutableList<HeartRate> = arrayListOf()

    private val updateTimerTask = object : Runnable {
        override fun run() {
            val currentTime = if (isRunning) SystemClock.uptimeMillis() else pauseTime
            elapsedTime = currentTime - startTime
            // 检查是否暂停
            seconds++
            updateTimerTextView()
            if(isRunning){
                handler.postDelayed(this, 1000)
            }
        }
    }

    var curHeartRateLevel: HeartRateLevel = HeartRateLevel.WARMUP_TIME
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set("平板支撑运动")
        startTimer()
        //TODO 整个流程完成后需要计算出当前用户的年龄
        App.userInfo?.let {
            age = DateUtils.calculateAge(DateTimeUtil.formatDate(DateTimeUtil.DATE_PATTERN, it.birthday), Date(System.currentTimeMillis()))
            weight = it.weight.toDouble()
            isMale = it.sex == getString(R.string.setting_sex_man)
        }

//        showOffLinePop()

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
    }

    private fun startTimer() {
        startTime = SystemClock.uptimeMillis() - elapsedTime
        isRunning = true
        mDatabind.stopBtn.text = getString(R.string.high_knee_main_stop)
        handler.post(updateTimerTask)
        val drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.stop_icon)
        mDatabind.stopBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
    }

    private fun pauseTimer() {
        isRunning = false
        pauseTime = SystemClock.uptimeMillis()
        mDatabind.stopBtn.text = getString(R.string.high_knee_main_continue)
        handler.removeCallbacks(updateTimerTask)

        val drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.star_icon)
        mDatabind.stopBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
    }

    fun completed() {
        showToast("完成了运动")
        pauseTimer()
        score = getScore()
        val request = FlatSupportRequest(requestStartTime, System.currentTimeMillis() / 1000, score, calories, heartRate)
        mViewModel.saveflatSupport(request)
    }

    private fun updateTimerTextView() {
        sportsMinutes = ((elapsedTime / (1000 * 60)) % 60).toInt()
        val seconds = ((elapsedTime / 1000) % 60).toInt()
        if (sportsMinutes >= mViewModel.maxTime) {
            completed()
        }
        val timeString = String.format("%02d:%02d", sportsMinutes, seconds)
        mViewModel.curTime.set(timeString)
        mDatabind.progressBar.setProgressPercentage((elapsedTime / 1000 / (mViewModel.maxTime * 60 / 100f)).toDouble(), false)
    }


    private fun getScore(): Int {
        val random = Random().nextInt(9)
        val score = (elapsedTime / 1000) / App.sportsTime * 100
        if (score == 100L) {
            return 100
        } else if (score > 90) {
            return 90 + random
        } else if (score > 80) {
            return 80 + random
        } else if (score > 70) {
            return 70 + random
        } else if (score > 60) {
            return 60 + random
        }
        return 50
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.plankLiveData.observe(this) {
            parseState(it, {
                val intent = Intent(this@PlankActivity, SportsCompletedActivity::class.java)
                val highKneeSports = HighKneeSports(SportsType.PLANK.type,elapsedTime/1000, minHeartRate, maxHeartRate, 0, DateUtils.formatDouble(abs(caloriesBurned)), score, warmupTime, fatBurningTime, cardioTime, breakTime)
                intent.putExtra(Constants.INTENT_COMPLETED, highKneeSports)
                startActivity(intent)
                finish()
            }, {
                showToast(getString(R.string.request_failed))
            })
        }
    }

    inner class ProxyClick {

        fun clickFinish() {
            showExitDialog()
        }

        fun clickComplete() {
            showCompletedDialog()
        }

        fun clickStop() {
            when (isRunning) {
                false -> startTimer()
                true -> pauseTimer()
            }
        }
    }

    fun showOffLinePop() {
        val deviceOffLinePop = DeviceOffLinePop(this@PlankActivity, object : DeviceOffLinePop.Listener {
            override fun reconnect() {
            }
        })
        val pop = XPopup.Builder(this@PlankActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(deviceOffLinePop)
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

    override fun leftHandGripsConnected() {
    }

    override fun rightHandGripsConnected() {
    }

    override fun onLeftDeviceData(data: ByteArray) {
    }

    override fun onRightDeviceData(data: ByteArray) {
    }

    override fun onGTSData(data: ByteArray) {
        if (!isRunning) {
            return
        }
        val interestedValue = DeviceDataParse.parseData2HeartRate(data)
        if (interestedValue != -1) {
            //设置最大心率
            if (maxHeartRate < interestedValue) {
                maxHeartRate = interestedValue
            }
            //设置最小心率
            if (minHeartRate == 0 || minHeartRate > interestedValue) {
                minHeartRate = interestedValue
            }
            mViewModel.heartRate.set(interestedValue.toString())
            totalHeartRate += interestedValue // interestedValue是心率的值

            heartRateCount++
            heartRateCount++
            val maxHeartRate: Double = (220 - age).toDouble()
            val heartRatePercentage: Double = interestedValue / maxHeartRate
            // 更新区间时间，每次心率读数都假设是10秒钟的时间
            if (heartRatePercentage < 0.6) {
                warmupTime += 10
                mViewModel.title.set(getString(R.string.high_knee_main_title))
            } else if (heartRatePercentage >= 0.6 && heartRatePercentage < 0.7) {
                fatBurningTime += 10
                mViewModel.title.set(getString(R.string.high_knee_main_title_fat_burning))
            } else if (heartRatePercentage >= 0.7 && heartRatePercentage < 0.8) {
                cardioTime += 10
                mViewModel.title.set(getString(R.string.high_knee_main_title_cardio))
            } else {
                breakTime += 10
                mViewModel.title.set(getString(R.string.high_knee_main_title_break))
            }
            // 计算卡路里消耗
            caloriesBurned = calculateCaloriesBurned(age, weight, interestedValue, seconds.toFloat() / 60, isMale)
            //消耗 caloriesBurned 千卡
            //暖身激活时间 warmupTime 秒
            //高效燃脂 fatBurningTime 秒
            //心肺提升时间 cardioTime 秒
            //极限突破 breakTime 秒
            println("===消耗 caloriesBurned 千卡:  " + caloriesBurned)

            calories.add(Calorie((caloriesBurned*1000).toInt(), DateTimeUtil.formatDate(Date(), DateTimeUtil.DATE_PATTERN_SS)))
            heartRate.add(HeartRate(interestedValue, DateTimeUtil.formatDate(Date(), DateTimeUtil.DATE_PATTERN_SS)))
        }
    }

    override fun onLeftHandGripsData(data: ByteArray) {
    }

    override fun onRightHandGripsData(data: ByteArray) {
    }

    private fun calculateCaloriesBurned(age: Int, weight: Double, heartRate: Int, exerciseTimeMinutes: Float, isMale: Boolean): Double {
        return if (isMale) (age * 0.2017 + weight * 0.09036 + heartRate * 0.6309 - 55.0969) * exerciseTimeMinutes / 4.184 else (age * 0.074 + weight * 0.05741 + heartRate * 0.4472 - 20.4022) * exerciseTimeMinutes / 4.184
    }


    var standardAction = 0
    private fun getCurScore(pitch: Float): Int {
        if (pitch in 80.0..95.0) {
            if (standardAction != 0) {
                return 101
            }
            return 100
        } else if ((pitch in 65.0..79.0) || (pitch >= 96 && pitch < 105)) {
            standardAction++
            if (standardAction != 0) {
                return 81
            }
            return 80
        } else if ((pitch in 50.0..64.0) || (pitch in 106.0..120.0)) {
            standardAction = 0
            return 50
        } else {
            standardAction = 0
            return 15
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimerTask)
        SMBleManager.delDeviceResultDataListener(this)
    }

    fun showExitDialog() {
        AlertDialog.Builder(this).setTitle("当前正在运动").setMessage("您确定要退出吗？").setPositiveButton("确定") { dialog, which ->
//            BleManager.getInstance().disconnectAllDevice()
//            BleManager.getInstance().destroy()
            finish() // 关闭所有 Activity 并退出应用
        }.setNegativeButton("取消", null).show()
    }

    fun showCompletedDialog() {
        AlertDialog.Builder(this).setTitle("当前正在运动").setMessage("您确定要结束运动吗？").setPositiveButton("确定") { dialog, which ->
//            BleManager.getInstance().disconnectAllDevice()

            completed()
        }.setNegativeButton("取消", null).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showExitDialog()

    }

}