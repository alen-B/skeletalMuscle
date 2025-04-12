package com.fjp.skeletalmuscle.ui.sports

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.ext.dp
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DataPoint
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.util.DeviceDataParse
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.ExerciseDetector
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.app.weight.pop.DeviceOffLinePop
import com.fjp.skeletalmuscle.data.model.bean.Calorie
import com.fjp.skeletalmuscle.data.model.bean.DumbbellRequest
import com.fjp.skeletalmuscle.data.model.bean.HeartRate
import com.fjp.skeletalmuscle.data.model.bean.HeartRateLevel
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityDumbbellMainBinding
import com.fjp.skeletalmuscle.viewmodel.state.DumbbellViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.DateUtils
import java.util.Date
import kotlin.math.ceil
import kotlin.math.max

class DumbbellMainActivity : BaseActivity<DumbbellViewModel, ActivityDumbbellMainBinding>(), SMBleManager.DeviceListener {
    val dumbbellRequest = DumbbellRequest(mutableListOf(), 0, mutableListOf(), mutableListOf(), 0, System.currentTimeMillis() / 1000, 1, 0, 0)
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
    private var oldCaloriesBurned: Double = 0.0 //上个阶段消耗的卡路里
    private var sportsTotalScore: Int = 0//运动中的总得分
    private var sportsAvgScore: Int = 0//运动中的平均分数
    private var age = 1
    private var weight = 1
    private var isMale = true
    private var seconds = 0
    private var upSeconds = 0
    private var warmupTime = 0.0
    private var fatBurningTime = 0.0
    private var cardioTime = 0.0
    private var breakTime = 0.0
    private var isUp = true//刚进来是在做上举运动

    private var rightOldLevel = -1
    private var leftOldLevel = -1
    private var leftLevelViews = mutableListOf<ImageView>()
    private var rightLevelViews = mutableListOf<ImageView>()
    lateinit var countDownTimer: CountDownTimer
    private var oldTime = 0L
    lateinit var pop: BasePopupView
    private val updateTimerTask = object : Runnable {
        override fun run() {
            val currentTime = if (isRunning) SystemClock.uptimeMillis() else pauseTime
            elapsedTime = currentTime - startTime
            // 检查是否暂停
            seconds++
            updateTimerTextView()
            if (isRunning) {
                handler.postDelayed(this, 1000)
            }
        }
    }

    var curHeartRateLevel: HeartRateLevel = HeartRateLevel.WARMUP_TIME
    override fun initView(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        ExerciseDetector.clear()

        mViewModel.title.set("上举运动")
        leftLevelViews.add(mDatabind.lIv1)
        leftLevelViews.add(mDatabind.lIv2)
        leftLevelViews.add(mDatabind.lIv3)
        leftLevelViews.add(mDatabind.lIv4)
        leftLevelViews.add(mDatabind.lIv5)
        App.userInfo?.let {
            age = DateUtils.calculateAge(DateTimeUtil.formatDate(DateTimeUtil.DATE_PATTERN, it.birthday), Date(System.currentTimeMillis()))
            weight = it.weight
            isMale = it.sex == getString(R.string.setting_sex_man)
        }

        startCountdown()
        SMBleManager.addDeviceResultDataListener(this)
        SMBleManager.connectedDevices[DeviceType.GTS]?.let {
            SMBleManager.subscribeToNotifications(it, Constants.GTS_UUID_SERVICE, Constants.GTS_UUID_CHARACTERISTIC_WRITE)
        }
        SMBleManager.connectedDevices[DeviceType.LEFT_LEG]?.let {
            SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_NOTIFY_CHAR)
        }
        SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]?.let {
            SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_NOTIFY_CHAR)
        }
    }


    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(4000L, 1000) {
            override fun onTick(millis: Long) {
                if (Math.ceil(millis / 1000.0).toInt() - 1 == 0) {
                    mDatabind.countdownText.text = "GO"
                } else {
                    mDatabind.countdownText.text = (Math.ceil(millis / 1000.0).toInt() - 1).toString()
                }
                animateText()


            }

            override fun onFinish() {
                mDatabind.countdownText.visibility = View.GONE
                mDatabind.centerIv.visibility = View.VISIBLE
//                mViewModel.title.set(getString(R.string.high_knee_main_title))
                startTimer()
            }

        }
        countDownTimer.start()
    }

    private fun animateText() {
        val scaleAnimation = ScaleAnimation(1f, 1.5f,  // 从1扩大到1.5
            1f, 1.5f,  // 从1扩大到1.5
            Animation.RELATIVE_TO_SELF, 0.5f,  // 在中心点放大
            Animation.RELATIVE_TO_SELF, 0.5f // 在中心点放大
        )
        scaleAnimation.duration = 1000 // 动画持续时间
        scaleAnimation.repeatCount = 1 // 动画重复次数
        scaleAnimation.repeatMode = Animation.REVERSE // 反向播放
        mDatabind.countdownText.startAnimation(scaleAnimation) // 启动动画
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

    private fun updateTimerTextView() {
        sportsMinutes = ((elapsedTime / (1000 * 60)) % 60).toInt()
        val seconds = ((elapsedTime / 1000) % 60).toInt()
        if (isUp && elapsedTime / 1000.toInt() >= mViewModel.maxTime * 60 - 5) {
            mDatabind.nextSportsIv.visibility = View.VISIBLE
        }
        if (sportsMinutes == mViewModel.maxTime) {
            if (this.isUp) {
                setExpandView()

            } else {
                showToast("完成了运动")
                completed()
            }

        }
        val timeString = String.format("%02d:%02d", sportsMinutes, seconds)
        mViewModel.curTime.set(timeString)
        mDatabind.progressBar.setProgressPercentage((elapsedTime / 1000 / (mViewModel.maxTime * 60 / 100f)).toDouble(), false)
    }

    private fun setExpandView() {
        countDownTimer.cancel()
        handler.removeCallbacks(updateTimerTask)

        this.isUp = false
        startCountdown()
        upSeconds = seconds
        mViewModel.leftImg.set(R.drawable.title_left_default_icon)
        mViewModel.title.set("扩胸运动")
        mDatabind.lCurDataTv.text = "扩胸"
        mDatabind.lLC.visibility = View.VISIBLE
        mDatabind.upLLC.visibility = View.GONE
        mDatabind.upRightLLC.visibility = View.GONE
        mDatabind.rTimesView.visibility = View.GONE
        mDatabind.rTimesTv.visibility = View.GONE
        mDatabind.rUnitTv.visibility = View.GONE
        mDatabind.nextSportsIv.visibility = View.GONE
        mDatabind.centerIv.setBackgroundResource(R.drawable.older)
        sportsMinutes = 0
        elapsedTime = 0
        startTime = SystemClock.uptimeMillis() - elapsedTime
        updateTimerTextView()

        mViewModel.maxTime = App.expandSportsTime
    }

    fun completed() {
        pauseTimer()
        if (seconds > App.expandSportsTime * 60) {
            seconds = App.expandSportsTime * 60
        }
        if (upSeconds > App.upSportsTime * 60) {
            upSeconds = App.upSportsTime * 60
        }
        dumbbellRequest.sport_time = upSeconds + seconds
        dumbbellRequest.end_time = System.currentTimeMillis() / 1000
        dumbbellRequest.plan_sport_time = App.upSportsTime * 60 + App.expandSportsTime * 60
        dumbbellRequest.record.addAll(ExerciseDetector.records)
        dumbbellRequest.score = getScore()
        mViewModel.saveDumbbell(dumbbellRequest)
    }


    private fun getScore(): Int {
        var upliftScore = ((ExerciseDetector.leftUpCount / (App.upliftAccount * 1.0)) * 100f).toInt()
        if (upliftScore > 100) {
            upliftScore = 100
        }
        var expandChestScore = ((ExerciseDetector.chestCount / (App.expandChestAccount * 1.0)) * 100).toInt()
        if (expandChestScore > 100) {
            expandChestScore = 100
        }
        return max(60, ((upliftScore + expandChestScore) / 2).toInt())
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.dumbbellLiveData.observe(this) {
            parseState(it, {
                val intent = Intent(this@DumbbellMainActivity, SportsDumbbellCompletedActivity::class.java)
                intent.putExtra(Constants.INTENT_COMPLETED, it)
                startActivity(intent)
                finish()
            }, {
                println("上传失败：${it.errorMsg}")
                it.printStackTrace()
                showToast(getString(R.string.request_failed))
                showReCompletedDialog()
            })
        }
        eventViewModel.sportWarningEvent.observeInActivity(this) {
            if (isUp) {
                if (it) {
                    if (mViewModel.leftImg.get() != R.drawable.title_icon_device_connecting) {
                        mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
                    }
                } else {
                    if (mViewModel.leftImg.get() != R.drawable.title_left_default_icon) {
                        mViewModel.leftImg.set(R.drawable.title_left_default_icon)
                    }
                    mViewModel.leftLegCount.set(ExerciseDetector.leftUpCount.toString())
                    mViewModel.rightLegCount.set(ExerciseDetector.rightUpCount.toString())
                }
                mViewModel.title.set(ExerciseDetector.leftUpTitle)
            } else {
                if (it) {
                    if (mViewModel.leftImg.get() != R.drawable.title_icon_device_connecting) {

                        mViewModel.leftImg.set(R.drawable.title_icon_device_connecting)
                    }
                } else {
                    if (mViewModel.leftImg.get() != R.drawable.title_left_default_icon) {
                        mViewModel.leftImg.set(R.drawable.title_left_default_icon)
                    }
                    mDatabind.lCurDataTv.text = "${ExerciseDetector.chestShowAngle.toString()}°"
                    mViewModel.leftLegCount.set(ExerciseDetector.chestCount.toString())
                }
                mViewModel.title.set(ExerciseDetector.chestTitle)
            }


        }

    }

    inner class ProxyClick {

        fun clickFinish() {
            showExitDialog()
        }

        fun clickComplete() {
            if (isUp) {
                setExpandView()

            } else {
                showCompletedDialog()
            }
        }

        fun clickStop() {
            when (isRunning) {
                false -> startTimer()
                true -> pauseTimer()
            }
        }
    }

    private fun showOffLinePop() {
        if (this@DumbbellMainActivity::pop.isInitialized && pop.isShow) {
            pop.dismiss()
        }
        val deviceOffLinePop = DeviceOffLinePop(this@DumbbellMainActivity, SportsType.DUMBBELL, object : DeviceOffLinePop.Listener {
            override fun reconnect(type: DeviceType) {
                if (type == DeviceType.GTS) {
                    SMBleManager.connectedDevices[DeviceType.GTS]?.let { SMBleManager.subscribeToNotifications(it, Constants.GTS_UUID_SERVICE, Constants.GTS_UUID_CHARACTERISTIC_WRITE) }
                } else if (type == DeviceType.LEFT_LEG) {
                    SMBleManager.connectedDevices[DeviceType.LEFT_LEG]?.let { SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_NOTIFY_CHAR) }
                } else if (type == DeviceType.RIGHT_LEG) {
                    SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]?.let { SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_NOTIFY_CHAR) }
                }

            }

            override fun completed() {

            }

        })
        pop = XPopup.Builder(this@DumbbellMainActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(deviceOffLinePop)

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

    private var leftDataPoint: DataPoint = DataPoint(0L, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    private var rightDataPoint: DataPoint = DataPoint(0L, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    override fun onLeftDeviceData(data: ByteArray) {
        if (!isRunning) {
            return
        }
        val parseData = DeviceDataParse.parseData2DataPoint(data)
        if (parseData != null) {
            leftDataPoint = parseData
        }

        if (isUp) {
            ExerciseDetector.processLeftUpData(leftDataPoint.pitch,leftDataPoint.roll, leftDataPoint.az)
        } else {
            ExerciseDetector.processData(leftDataPoint.pitch, leftDataPoint.yaw, leftDataPoint.roll, leftDataPoint.ax, leftDataPoint.ay, leftDataPoint.az, rightDataPoint.pitch, rightDataPoint.yaw, rightDataPoint.roll, rightDataPoint.ax, rightDataPoint.ay, rightDataPoint.az, this.isUp)
        }
    }


    override fun onRightDeviceData(data: ByteArray) {
//        println("pitch角度：${leftDataPoint.pitch}")
        if (!isRunning) {
            return
        }
        val parseData = DeviceDataParse.parseData2DataPoint(data)
        if (parseData != null) {
            rightDataPoint = parseData
        }
        if (isUp) {
            ExerciseDetector.processRightUpData(rightDataPoint.pitch,rightDataPoint.roll, rightDataPoint.az)
        } else {

//            ExerciseDetector.processData(leftDataPoint.pitch, leftDataPoint.yaw, leftDataPoint.roll, leftDataPoint.ax, leftDataPoint.ay, leftDataPoint.az, rightDataPoint.pitch, rightDataPoint.yaw, rightDataPoint.roll, rightDataPoint.ax, rightDataPoint.ay, rightDataPoint.az, false, this.isUp)
//            mViewModel.leftLegCount.set(ExerciseDetector.chestCount.toString())
        }

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
            val maxHeartRate: Double = (220 - age).toDouble()
            val heartRatePercentage: Double = interestedValue / maxHeartRate
            if (interestedValue > maxHeartRate) {
                showToast("您的⼼率已超标，请注意休息！")
            }
            // 计算卡路里消耗
            caloriesBurned = calculateCaloriesBurned(age, weight.toDouble(), interestedValue, seconds / 60f, isMale)
            //消耗 caloriesBurned 千卡
            //暖身激活时间 warmupTime 秒
            //高效燃脂 fatBurningTime 秒
            //心肺提升时间 cardioTime 秒
            //极限突破 breakTime 秒
            dumbbellRequest.calorie.add(Calorie((((caloriesBurned - oldCaloriesBurned).coerceAtLeast(0.0)) * 1000).toInt(), DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS)))
            dumbbellRequest.heart_rate.add(HeartRate(interestedValue, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS)))
            oldCaloriesBurned = caloriesBurned
        }
    }


    override fun onLeftHandGripsData(data: ByteArray) {
    }

    override fun onRightHandGripsData(data: ByteArray) {
    }

    private fun calculateCaloriesBurned(age: Int, weight: Double, heartRate: Int, exerciseTimeMinutes: Float, isMale: Boolean): Double {
        return if (isMale) (age * 0.2017 + weight * 0.09036 + heartRate * 0.6309 - 55.0969) * exerciseTimeMinutes / 4.184 else (age * 0.074 + weight * 0.05741 + heartRate * 0.4472 - 20.4022) * exerciseTimeMinutes / 4.184
    }

    private fun setLeftCurLevelByPitch(pitch: Float) {
        val leftLevel = calculateLevelFromAngle(pitch)
        if (leftOldLevel != -1) {
            leftLevelViews[leftOldLevel].setImageDrawable(null)
        }
        leftLevelViews[leftLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected))
        leftOldLevel = leftLevel
    }

    private fun calculateLevelFromAngle(angle: Float): Int {
        // 假设角度从0到90度，分成7个等级
        // 你可以根据实际需求调整这个方法
        val maxAngle = 220
        val levels = 5
        // 计算每个等级的角度
        val anglePerLevel = (maxAngle / levels).toFloat()
        // 计算当前角度对应的等级
        val level = levels - ceil((angle / anglePerLevel).toDouble()).toInt() // 反转等级
        // 确保返回的level不会超出triangleViews的大小
        val minLevel = Math.min(level, 5)
        if (minLevel in 1..4) {
            return minLevel
        }
        return 0
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        handler.removeCallbacks(updateTimerTask)
        SMBleManager.delDeviceResultDataListener(this)
    }

    fun showExitDialog() {
        val pop = XPopup.Builder(this).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).popupWidth(400.dp.toInt()).asConfirm("当前正在运动", "您确定要退出吗？", {
            finish()
        }, { })

        pop.show()

    }

    fun showCompletedDialog() {
        val pop = XPopup.Builder(this).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).popupWidth(400.dp.toInt()).asConfirm("当前正在运动", "您确定要结束运动吗？", {
            completed()
        }, { })
        pop.show()
    }

    fun showReCompletedDialog() {
        val pop = XPopup.Builder(this).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).popupWidth(400.dp.toInt()).asConfirm("数据提交失败", "是否再次提交数据?", {
            completed()
        }, { })
        pop.show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        showExitDialog()

    }

}