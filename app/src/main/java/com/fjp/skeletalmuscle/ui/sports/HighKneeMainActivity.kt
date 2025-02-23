package com.fjp.skeletalmuscle.ui.sports

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
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
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.ActionDetector
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.util.DeviceDataParse
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.app.weight.pop.DeviceOffLinePop
import com.fjp.skeletalmuscle.data.model.bean.Calorie
import com.fjp.skeletalmuscle.data.model.bean.HeartRate
import com.fjp.skeletalmuscle.data.model.bean.HeartRateLevel
import com.fjp.skeletalmuscle.data.model.bean.LiftLegRequest
import com.fjp.skeletalmuscle.data.model.bean.Record
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.result.SportLiftLeg
import com.fjp.skeletalmuscle.data.model.bean.result.TodayRecord
import com.fjp.skeletalmuscle.databinding.ActivityHighKneeMainBinding
import com.fjp.skeletalmuscle.viewmodel.request.RequestHighKneeViewModel
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeViewModel
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.DateUtils
import java.util.Date
import kotlin.math.abs
import kotlin.math.ceil


class HighKneeMainActivity : BaseActivity<HighKneeViewModel, ActivityHighKneeMainBinding>(), SMBleManager.DeviceListener {
    var liftLegRequest = LiftLegRequest(mutableListOf(), 0, mutableListOf(), mutableListOf(), 0, System.currentTimeMillis() / 1000, 0.0, 0, 0, 0, 0,0,0)
    private val requestHighKneeViewModel: RequestHighKneeViewModel by viewModels()
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var isRunning: Boolean = false
    private var pauseTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())
    private val LIFT_THRESHOLD = 30f // 抬起动作的阈值
    private var leftLegLifted = false
    private var rightLegLifted = false
    private var leftLegLifts = 0//左腿抬高了多少次
    private var rightLegLifts = 0//右腿抬高了多少次
    private var totalHeartRate = 0
    private var maxHeartRate = 0//最大心率
    private var minHeartRate = 0//最小心率
    private var heartRateCount = 0
    private var sportsMinutes = 0 //运动了多少分钟
    private var sportsSecond = 0 //运动了多少秒
    private var caloriesBurned: Double = 0.0 //消耗了多少千卡
    private var oldCaloriesBurned: Double = 0.0 //上个阶段消耗的卡路里
    private var sportsTotalScore: Int = 0//运动中的总得分
    private var sportsAvgScore: Int = 0//运动中的平均分数

    private var leftLegAngleSum = 0.0
    private var rightLegAngleSum = 0.0
    private var leftLegmaxPitchInCycle = 0f // 记录周期内左腿最大的pitch值
    private var rightLegmaxPitchInCycle = 0f // 记录周期内右腿最大的pitch值
    private var age = 1
    private var weight = 1.0
    private var isMale = true
    private var seconds = 0
    private var oldSeconds = 0
    private var warmupTime = 0
    private var fatBurningTime = 0
    private var cardioTime = 0
    private var breakTime = 0
    private var leftLastPitch = 0f // 左腿上一个pitch值
    private var rightLastPitch = 0f // 右腿上一个pitch值

    private var rightOldLevel = -1
    private var leftOldLevel = -1
    private var leftIsDescending = false // 标记是否已经开始下降
    private var rightIsDescending = false // 标记是否已经开始下降
    private var mediaPlayer_high: MediaPlayer? = null
    private var playAudioIsOpen = true
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
        mViewModel.title.set(getString(R.string.now_start))
        App.userInfo?.let {
            age = DateUtils.calculateAge(DateTimeUtil.formatDate(DateTimeUtil.DATE_PATTERN, it.birthday), Date(System.currentTimeMillis()))
            weight = it.weight.toDouble()
            isMale = it.sex == getString(R.string.setting_sex_man)
        }
        playAudioIsOpen = CacheUtil.getVoiceInteraction()

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
        startCountdown(4)
    }

    private fun startCountdown(startNumber: Int) {
        val countDownTimer = object : CountDownTimer(startNumber * 1000L, 1000) {
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
                mDatabind.scoreView.visibility = View.VISIBLE
                mDatabind.scoreViewTv.visibility = View.VISIBLE
                mDatabind.scoreViewTv.visibility = View.VISIBLE
                mViewModel.title.set(getString(R.string.high_knee_main_title))
                liftLegRequest = LiftLegRequest(mutableListOf(), 0, mutableListOf(), mutableListOf(), 0, System.currentTimeMillis() / 1000, 0.0, 0, 0, 0, 0,0,0)
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
        sportsSecond = ((elapsedTime / 1000) % 60).toInt()
        if (sportsMinutes >= mViewModel.maxTime) {
            completed()
        }
        val timeString = String.format("%02d:%02d", sportsMinutes, sportsSecond)
        mViewModel.curTime.set(timeString)
        mDatabind.progressBar.setProgressPercentage((elapsedTime / 1000 / (mViewModel.maxTime * 60 / 100f)).toDouble(), false)
    }

    fun completed() {
        showToast("完成了运动")
        pauseTimer()
        liftLegRequest.end_time = System.currentTimeMillis() / 1000
        liftLegRequest.cardiorespiratory_endurance = getCardiorespiratorEndurance()

        if (liftLegRequest.end_time - liftLegRequest.start_time > App.sportsTime * 60) {
            liftLegRequest.end_time = liftLegRequest.start_time + App.sportsTime * 60
        }
        if(seconds> App.sportsTime*60){
            seconds = App.sportsTime*60
        }
        liftLegRequest.sport_time = seconds
        liftLegRequest.plan_sport_time = App.sportsTime*60
        requestHighKneeViewModel.saveLiftLeg(liftLegRequest)

    }

    private fun getCardiorespiratorEndurance(): Double {
        // 抬腿频率
        val rate = (leftLegLifts + rightLegLifts)*1f / (System.currentTimeMillis() / 1000 - liftLegRequest.start_time) / 60
        return String.format("%.2f", rate * (leftLegAngleSum + rightLegAngleSum) / (leftLegLifts + rightLegLifts)).toDouble()
    }

    override fun createObserver() {
        super.createObserver()
        requestHighKneeViewModel.liftLegLiveData.observe(this) {
            parseState(it, {
                showToast("发送成功")
                val intent = Intent(this@HighKneeMainActivity, SportsHighKneeCompletedActivity::class.java)
                intent.putExtra(Constants.INTENT_SPORT_LIFT_LEG, it)
                startActivity(intent)
                finish()
            }, {
                showToast(getString(R.string.request_failed))
                showReCompletedDialog()
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
        val deviceOffLinePop = DeviceOffLinePop(this@HighKneeMainActivity, SportsType.HIGH_KNEE, object : DeviceOffLinePop.Listener {
            override fun reconnect(type: DeviceType) {
                if (type == DeviceType.GTS) {
                    SMBleManager.connectedDevices[DeviceType.GTS]?.let { SMBleManager.subscribeToNotifications(it, Constants.GTS_UUID_SERVICE, Constants.GTS_UUID_CHARACTERISTIC_WRITE) }
                } else if (type == DeviceType.LEFT_LEG) {
                    SMBleManager.connectedDevices[DeviceType.LEFT_LEG]?.let { SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_CHARACTERISTIC_WRITE) }
                } else if (type == DeviceType.RIGHT_LEG) {
                    SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]?.let { SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_CHARACTERISTIC_WRITE) }
                }

            }

            override fun completed() {

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

    override fun leftHandGripsConnected() {
    }

    override fun rightHandGripsConnected() {
    }

    override fun onLeftDeviceData(data: ByteArray) {
        if (!isRunning) {
            return
        }

        val pitch = DeviceDataParse.parseData2Pitch(data)
//        val roll = DeviceDataParse.parseData2Roll(data)
//        val yaw = DeviceDataParse.parseData2Yaw(data)
        if(pitch > 180){
            return
        }
        if (pitch > 90) {
            // 抬腿过高，播放提示音
            // 抬腿过高，检查MediaPlayer是否已经在播放
            mViewModel.title.set("左腿抬腿角度偏高！")
            if (playAudioIsOpen && mediaPlayer_high != null && !mediaPlayer_high!!.isPlaying) {
                mediaPlayer_high!!.start() // 播放音频
            }
        }
        // 更新周期内最大的pitch值
        if (pitch > leftLegmaxPitchInCycle) {
            leftLegmaxPitchInCycle = pitch
            // 当pitch值达到新的高点时，重置下降标记
            leftIsDescending = false
        }

        // 检查是否开始下降
        if (pitch < leftLastPitch) {
            if (!leftIsDescending) {
                if (abs(pitch) > LIFT_THRESHOLD) {
                    mViewModel.leftLegAngle.set("L ${leftLegmaxPitchInCycle.toInt()}°")
                    if (leftLegmaxPitchInCycle > 70 && leftLegmaxPitchInCycle < 90) {
                        mViewModel.title.set("太棒了，您做的很好，继续！")
                    }
                    leftLegLifts++
                    liftLegRequest.record.add(Record(leftLegmaxPitchInCycle.toInt(), DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 1))
                    leftLegAngleSum += leftLegmaxPitchInCycle.toDouble()
                    mViewModel.leftLegCount.set(leftLegLifts.toString())
                    getAvgScore(abs(leftLegmaxPitchInCycle))
                }
            }
            leftIsDescending = true
        }
        // 如果已经开始下降，并且当前pitch值比较低，认为一个周期结束
        if (leftIsDescending && pitch < 20) { // someLowThreshold是您定义的较低的值，比如10或更低
            // 周期结束，检查最大pitch值是否在25到30之间
            if (leftLegmaxPitchInCycle >= 25 && leftLegmaxPitchInCycle <= 30) {
                mViewModel.title.set("左腿抬腿角度偏低！")
                // 播放提示音
                if (playAudioIsOpen && mediaPlayer_low != null && !mediaPlayer_low!!.isPlaying()) {
                    mediaPlayer_low!!.start()
                }
            }
            // 重置周期内最大的pitch值和下降标记
            leftLegmaxPitchInCycle = 0f
//            leftIsDescending = false
        }
        // 更新上一个pitch值
        leftLastPitch = pitch
        setLeftCurLevelByPitch(pitch)
    }

    fun setLeftCurLevelByPitch(pitch: Float) {
        val leftLevel = calculateLevelFromAngle(pitch)
        if (leftOldLevel != -1) {
            leftLevelViews[leftOldLevel].setImageDrawable(null)
        }
        if (curHeartRateLevel == HeartRateLevel.WARMUP_TIME) {
            leftLevelViews[leftLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected))
        } else if (curHeartRateLevel == HeartRateLevel.FAT_BURNING_TIME) {
            leftLevelViews[leftLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected_fat_burning))
        } else if (curHeartRateLevel == HeartRateLevel.CARDIO_TIME) {
            leftLevelViews[leftLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected_cardio))
        } else if (curHeartRateLevel == HeartRateLevel.BREAK_TIME) {
            leftLevelViews[leftLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected_break))
        }
        leftOldLevel = leftLevel
    }

    override fun onRightDeviceData(data: ByteArray) {
        if (!isRunning) {
            return
        }
        val pitch = DeviceDataParse.parseData2Pitch(data)
//        val roll = DeviceDataParse.parseData2Roll(data)
//        val yaw = DeviceDataParse.parseData2Yaw(data)
        if(pitch > 180){
            return
        }
        if (pitch > 90) {
            // 抬腿过高，播放提示音
            // 抬腿过高，检查MediaPlayer是否已经在播放
            mViewModel.title.set("右腿抬腿角度偏高！")
            if (mediaPlayer_high != null && !mediaPlayer_high!!.isPlaying) {
                mediaPlayer_high!!.start() // 播放音频
            }
        }
        // 更新周期内最大的pitch值
        if (pitch > rightLegmaxPitchInCycle) {
            rightLegmaxPitchInCycle = pitch
            rightIsDescending = false // 当pitch值达到新的高点时，重置下降标记
        }
        // 检查是否开始下降
        if (pitch < rightLastPitch) {
            if (!rightIsDescending) {
                if (abs(pitch) > LIFT_THRESHOLD) {
                    if (leftLegmaxPitchInCycle > 70 && leftLegmaxPitchInCycle < 90) {
                        mViewModel.title.set("太棒了，您做的很好，继续！")
                    }
                    rightLegLifts++
                    mViewModel.rightLegAngle.set("R ${rightLastPitch.toInt()}°")
                    rightLegAngleSum += abs(rightLegmaxPitchInCycle).toDouble()
                    liftLegRequest.record.add(Record(rightLegmaxPitchInCycle.toInt(), DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2))
                    mViewModel.rightLegCount.set(rightLegLifts.toString())
                    getAvgScore(abs(rightLegmaxPitchInCycle))

                }
            }
            rightIsDescending = true
        }
        // 如果已经开始下降，并且当前pitch值比较低，认为一个周期结束
        if (rightIsDescending && pitch < 20) { // someLowThreshold是您定义的较低的值，比如10或更低
            // 周期结束，检查最大pitch值是否在25到30之间
            if (rightLegmaxPitchInCycle in 25.0..30.0) {
                mViewModel.title.set("右腿抬腿角度偏低！")
                // 播放提示音
                if (mediaPlayer_low != null && !mediaPlayer_low!!.isPlaying) {
                    mediaPlayer_low!!.start()
                }
            }
            // 重置周期内最大的pitch值和下降标记
            rightLegmaxPitchInCycle = 0f
//            rightIsDescending = false
        }
        // 更新上一个pitch值
        rightLastPitch = pitch

        setRightCurLevelByPitch(pitch)
    }

    fun setRightCurLevelByPitch(pitch: Float) {
        val rightLevel = calculateLevelFromAngle(pitch)
        if (rightOldLevel != -1) {
            rightLevelViews[rightOldLevel].setImageDrawable(null)
        }
        if (curHeartRateLevel == HeartRateLevel.WARMUP_TIME) {
            rightLevelViews[rightLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected))
        } else if (curHeartRateLevel == HeartRateLevel.FAT_BURNING_TIME) {
            rightLevelViews[rightLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected_fat_burning))
        } else if (curHeartRateLevel == HeartRateLevel.CARDIO_TIME) {
            rightLevelViews[rightLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected_cardio))
        } else if (curHeartRateLevel == HeartRateLevel.BREAK_TIME) {
            rightLevelViews[rightLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected_break))
        }
        rightOldLevel = rightLevel
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
            // 更新区间时间，每次心率读数都假设是10秒钟的时间
            if (heartRatePercentage < 0.6) {
                warmupTime += 10
                if (curHeartRateLevel != HeartRateLevel.WARMUP_TIME) {
                    showWarmupUI()
                }
                curHeartRateLevel = HeartRateLevel.WARMUP_TIME
                mViewModel.title.set(getString(R.string.high_knee_main_title))
            } else if (heartRatePercentage >= 0.6 && heartRatePercentage < 0.7) {
                fatBurningTime += 10
                if (curHeartRateLevel != HeartRateLevel.FAT_BURNING_TIME) {
                    showFatBurningUI()
                }
                curHeartRateLevel = HeartRateLevel.FAT_BURNING_TIME
                mViewModel.title.set(getString(R.string.high_knee_main_title_fat_burning))
            } else if (heartRatePercentage >= 0.7 && heartRatePercentage < 0.8) {
                cardioTime += 10
                if (curHeartRateLevel != HeartRateLevel.CARDIO_TIME) {
                    showCardioUI()
                }
                curHeartRateLevel = HeartRateLevel.CARDIO_TIME
                mViewModel.title.set(getString(R.string.high_knee_main_title_cardio))
            } else {
                breakTime += 10
                if (curHeartRateLevel != HeartRateLevel.BREAK_TIME) {
                    showBreakUI()
                }
                curHeartRateLevel = HeartRateLevel.BREAK_TIME
                mViewModel.title.set(getString(R.string.high_knee_main_title_break))
            }
            // 计算卡路里消耗
            caloriesBurned = calculateCaloriesBurned(age, weight, interestedValue, seconds / 60f, isMale)
            println("seconds - oldSeconds:${seconds - oldSeconds} interestedValue：${interestedValue} oldCaloriesBurned:${oldCaloriesBurned}    totalCalor:${caloriesBurned}")

            //消耗 caloriesBurned 千卡
            //暖身激活时间 warmupTime 秒
            //高效燃脂 fatBurningTime 秒
            //心肺提升时间 cardioTime 秒
            //极限突破 breakTime 秒
            println("===消耗 caloriesBurned 千卡:  " + caloriesBurned)
            println("===暖身激活时间:  " + warmupTime)
            println("===高效燃脂:  " + fatBurningTime)
            println("===心肺提升时间:  " + cardioTime)
            println("===极限突破:  " + breakTime)
            liftLegRequest.heart_rate.add(HeartRate(interestedValue, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS)))
            liftLegRequest.calorie.add(Calorie((((caloriesBurned - oldCaloriesBurned).coerceAtLeast(0.0)) * 1000).toInt(), DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS)))
            liftLegRequest.heart_lung_enhancement = cardioTime
            liftLegRequest.efficient_grease_burning = fatBurningTime
            liftLegRequest.extreme_breakthrough = breakTime
            liftLegRequest.warm_up_activation = warmupTime
            oldCaloriesBurned = caloriesBurned
        }
    }


    private fun showWarmupUI() {
        mDatabind.stopBtn.setBackgroundResource(R.drawable.btn_blue_selector)
        mDatabind.progressBar.setProgressDrawableColor(ContextCompat.getColor(this, R.color.color_blue))
        mDatabind.progressBar.setBackgroundDrawableColor(ContextCompat.getColor(this, R.color.color_ccc6d1fc))
        mDatabind.rTimesView.setBackgroundResource(R.drawable.bg_3d4e71ff_20)
        mDatabind.lTimesView.setBackgroundResource(R.drawable.bg_3d4e71ff_20)
        mDatabind.lTimesView.setBackgroundResource(R.drawable.bg_3d4e71ff_20)
        mDatabind.lIv1.setBackgroundResource(R.drawable.bg_e64e71ff_8)
        mDatabind.lIv2.setBackgroundResource(R.drawable.bg_b34e71ff_8)
        mDatabind.lIv3.setBackgroundResource(R.drawable.bg_804e71ff_8)
        mDatabind.lIv4.setBackgroundResource(R.drawable.bg_3d4e71ff_8)
        mDatabind.lIv5.setBackgroundResource(R.drawable.bg_1a4e71ff_8)

        mDatabind.rIv1.setBackgroundResource(R.drawable.bg_e64e71ff_8)
        mDatabind.rIv2.setBackgroundResource(R.drawable.bg_b34e71ff_8)
        mDatabind.rIv3.setBackgroundResource(R.drawable.bg_804e71ff_8)
        mDatabind.rIv4.setBackgroundResource(R.drawable.bg_3d4e71ff_8)
        mDatabind.rIv5.setBackgroundResource(R.drawable.bg_1a4e71ff_8)

    }

    private fun showFatBurningUI() {

        mDatabind.stopBtn.setBackgroundResource(R.drawable.bg_btn_fat_burning)
        mDatabind.progressBar.setProgressDrawableColor(ContextCompat.getColor(this, R.color.color_ccffc019))
        mDatabind.progressBar.setBackgroundDrawableColor(ContextCompat.getColor(this, R.color.color_ccfcefce))
        mDatabind.rTimesView.setBackgroundResource(R.drawable.bg_high_knee_times_fat_burning)
        mDatabind.lTimesView.setBackgroundResource(R.drawable.bg_high_knee_times_fat_burning)
        mDatabind.lIv1.setBackgroundResource(R.drawable.bg_high_knee_level_5_fat_burning)
        mDatabind.lIv2.setBackgroundResource(R.drawable.bg_high_knee_level_4_fat_burning)
        mDatabind.lIv3.setBackgroundResource(R.drawable.bg_high_knee_level_3_fat_burning)
        mDatabind.lIv4.setBackgroundResource(R.drawable.bg_high_knee_level_2_fat_burning)
        mDatabind.lIv5.setBackgroundResource(R.drawable.bg_high_knee_level_1_fat_burning)

        mDatabind.rIv1.setBackgroundResource(R.drawable.bg_high_knee_level_5_fat_burning)
        mDatabind.rIv2.setBackgroundResource(R.drawable.bg_high_knee_level_4_fat_burning)
        mDatabind.rIv3.setBackgroundResource(R.drawable.bg_high_knee_level_3_fat_burning)
        mDatabind.rIv4.setBackgroundResource(R.drawable.bg_high_knee_level_2_fat_burning)
        mDatabind.rIv5.setBackgroundResource(R.drawable.bg_high_knee_level_1_fat_burning)

    }

    private fun showCardioUI() {

        mDatabind.stopBtn.setBackgroundResource(R.drawable.bg_btn_cardio)
        mDatabind.progressBar.setProgressDrawableColor(ContextCompat.getColor(this, R.color.color_ff824c))
        mDatabind.progressBar.setBackgroundDrawableColor(ContextCompat.getColor(this, R.color.color_3dff824c))
        mDatabind.rTimesView.setBackgroundResource(R.drawable.bg_high_knee_times_cardio)
        mDatabind.lTimesView.setBackgroundResource(R.drawable.bg_high_knee_times_cardio)
        mDatabind.lIv1.setBackgroundResource(R.drawable.bg_high_knee_level_5_cardio)
        mDatabind.lIv2.setBackgroundResource(R.drawable.bg_high_knee_level_4_cardio)
        mDatabind.lIv3.setBackgroundResource(R.drawable.bg_high_knee_level_3_cardio)
        mDatabind.lIv4.setBackgroundResource(R.drawable.bg_high_knee_level_2_cardio)
        mDatabind.lIv5.setBackgroundResource(R.drawable.bg_high_knee_level_1_cardio)

        mDatabind.rIv1.setBackgroundResource(R.drawable.bg_high_knee_level_5_cardio)
        mDatabind.rIv2.setBackgroundResource(R.drawable.bg_high_knee_level_4_cardio)
        mDatabind.rIv3.setBackgroundResource(R.drawable.bg_high_knee_level_3_cardio)
        mDatabind.rIv4.setBackgroundResource(R.drawable.bg_high_knee_level_2_cardio)
        mDatabind.rIv5.setBackgroundResource(R.drawable.bg_high_knee_level_1_cardio)

    }

    private fun showBreakUI() {

        mDatabind.stopBtn.setBackgroundResource(R.drawable.bg_btn_break)
        mDatabind.progressBar.setProgressDrawableColor(ContextCompat.getColor(this, R.color.color_ff574c))
        mDatabind.progressBar.setBackgroundDrawableColor(ContextCompat.getColor(this, R.color.color_1aff574c))
        mDatabind.rTimesView.setBackgroundResource(R.drawable.bg_high_knee_times_break)
        mDatabind.lTimesView.setBackgroundResource(R.drawable.bg_high_knee_times_break)
        mDatabind.lIv1.setBackgroundResource(R.drawable.bg_high_knee_level_5_break)
        mDatabind.lIv2.setBackgroundResource(R.drawable.bg_high_knee_level_4_break)
        mDatabind.lIv3.setBackgroundResource(R.drawable.bg_high_knee_level_3_break)
        mDatabind.lIv4.setBackgroundResource(R.drawable.bg_high_knee_level_2_break)
        mDatabind.lIv5.setBackgroundResource(R.drawable.bg_high_knee_level_1_break)

        mDatabind.rIv1.setBackgroundResource(R.drawable.bg_high_knee_level_5_break)
        mDatabind.rIv2.setBackgroundResource(R.drawable.bg_high_knee_level_4_break)
        mDatabind.rIv3.setBackgroundResource(R.drawable.bg_high_knee_level_3_break)
        mDatabind.rIv4.setBackgroundResource(R.drawable.bg_high_knee_level_2_break)
        mDatabind.rIv5.setBackgroundResource(R.drawable.bg_high_knee_level_1_break)
    }

    override fun onLeftHandGripsData(data: ByteArray) {
    }

    override fun onRightHandGripsData(data: ByteArray) {
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
                getAvgScore(abs(pitch))
            } else if (!isLeftLeg && !rightLegLifted) {
                rightLegLifts++
                rightLegLifted = true
                rightLegAngleSum += abs(pitch).toDouble()
                mViewModel.rightLegCount.set(rightLegLifts.toString())
                getAvgScore(abs(pitch))
            }

        } else {
            if (isLeftLeg) {
                leftLegLifted = false
            } else {
                rightLegLifted = false
            }
        }

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

    private fun getAvgScore(pitch: Float) {
        val curScore = getCurScore(pitch)
        sportsTotalScore += curScore
        sportsAvgScore = ((sportsTotalScore / ((leftLegLifts + rightLegLifts) * 100f)) * 100).toInt()
        mDatabind.scoreViewTv.text = Math.min(sportsAvgScore, 100).toString()
        liftLegRequest.score = sportsAvgScore.coerceAtMost(100)
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
        if (minLevel > 0) {
            return minLevel
        }
        return 0
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimerTask)
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

    fun showExitDialog() {
        val pop = XPopup.Builder(this).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).popupWidth(400).asConfirm("当前正在运动", "您确定要退出吗？", {
            finish()
        }, { })

        pop.show()

    }

    fun showCompletedDialog() {
        val pop = XPopup.Builder(this).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).popupWidth(400).asConfirm("当前正在运动", "您确定要结束运动吗？", {
            completed()
        }, { })
        pop.show()
    }

    fun showReCompletedDialog() {
        val pop = XPopup.Builder(this).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).popupWidth(400).asConfirm("数据提交失败", "是否再次提交数据?", {
            requestHighKneeViewModel.saveLiftLeg(liftLegRequest)
        }, { })
        pop.show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        showExitDialog()

    }

}