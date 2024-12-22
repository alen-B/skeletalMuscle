package com.fjp.skeletalmuscle.ui.assessment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.AssessmentUtils
import com.fjp.skeletalmuscle.app.util.BleScanHelper
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DeviceDataParse
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.app.weight.pop.DeviceOffLinePop
import com.fjp.skeletalmuscle.data.model.bean.AssessmentType
import com.fjp.skeletalmuscle.data.model.bean.BleDevice
import com.fjp.skeletalmuscle.data.model.bean.SaveAssessmentRequest
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivitySportsAssessmentBinding
import com.fjp.skeletalmuscle.viewmodel.state.SportsAssessmentViewModel
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.ext.parseState
import kotlin.math.abs
import kotlin.math.ceil

class SportsAssessmentActivity : BaseActivity<SportsAssessmentViewModel, ActivitySportsAssessmentBinding>(), SMBleManager.DeviceListener {
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var isRunning: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private val LIFT_THRESHOLD = 30f // 抬起动作的阈值

    private var leftLegLifts = 0//左腿抬高了多少次
    private var rightLegLifts = 0//右腿抬高了多少次
    private var sportsMinutes = 0 //运动了多少分钟

    //由于起坐时候用户可能带了两个设备或者一个设备，以最大次数为准
    private var totalSitUpTimes = 0 //起坐了多少次
    private var leftSitUpTimes = 0
    private var rightSitUpTimes = 0
    private val playAudioIsOpen = true

    private var leftLegmaxPitchInCycle = 0f // 记录周期内左腿最大的pitch值
    private var rightLegmaxPitchInCycle = 0f // 记录周期内右腿最大的pitch值

    private var seconds = 0

    private var leftLastPitch = 0f // 左腿上一个pitch值
    private var rightLastPitch = 0f // 右腿上一个pitch值

    private var rightOldLevel = 0
    private var leftOldLevel = 0
    private var leftIsDescending = false // 标记是否已经开始下降
    private var rightIsDescending = false // 标记是否已经开始下降
    private var mediaPlayer_high: MediaPlayer? = null
    private var mediaPlayer_low: MediaPlayer? = null
    private var leftLevelViews = mutableListOf<ImageView>()
    private var rightLevelViews = mutableListOf<ImageView>()
    private var totalTime = 60//总共运行一分钟
    private var totalGripTimes = 3//总共运行一分钟
    private var curType = AssessmentType.HighLeg
    private var weight: Int = 0//用户选择的当前体重
    private var waistline: Int = 0//用户选择的当前腰围
    private val updateTimerTask = object : Runnable {
        override fun run() {

            val currentTime = SystemClock.uptimeMillis()
            elapsedTime = currentTime - startTime
            // 检查是否暂停
            seconds++
            updateTimerTextView()
            if (isRunning) {
                handler.postDelayed(this, 1000)
            }
        }
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

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set("高抬腿运动即将开始")
        weight = intent.getIntExtra(Constants.INTENT_KEY_WEIGHT, 0)
        waistline = intent.getIntExtra(Constants.INTENT_KEY_WAISTLINE, 0)
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

    override fun createObserver() {
        super.createObserver()
        mViewModel.sportsCalorieResult.observe(this) {
            parseState(it, {
                val intent = Intent(this@SportsAssessmentActivity, SportsAssessmentResultActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }, {
                showToast(getString(R.string.request_failed))
            })
        }
    }

    private fun startTimer() {
        if (!isRunning) {
            startTime = SystemClock.uptimeMillis() - elapsedTime
            isRunning = true
            handler.post(updateTimerTask)
        }
    }

    private fun updateTimerTextView() {
        sportsMinutes = ((elapsedTime / (1000 * 60)) % 60).toInt()
        val timeString = String.format("%02d:%02d", sportsMinutes, seconds)
        mViewModel.curTime.set(timeString)
        if (curType != AssessmentType.Grip) {
            mDatabind.countdownTv.text = "还剩${totalTime - seconds}S"
        }
        if (totalTime - seconds < 20) {
            if (curType == AssessmentType.HighLeg) {
                if (!mDatabind.nextSportsIv.isVisible) {
                    mDatabind.nextSportsIv.visibility = View.VISIBLE
                }
            } else if (curType == AssessmentType.UpDown) {
                if (!mDatabind.nextSportsIv.isVisible) {
                    mDatabind.nextSportsIv.visibility = View.VISIBLE
                    mDatabind.nextSportsIv.setImageResource(R.drawable.assessment_08)
                }
            }
        }
        if (totalTime - seconds == 0) {
            completed()
        }
    }

    fun completed() {
        if (isRunning) {
            isRunning = false
            handler.removeCallbacks(updateTimerTask)
        }
        elapsedTime = 0
        seconds = 0
        if (curType != AssessmentType.Grip) {
            mViewModel.sportsNumber.set("0")
        }

        mDatabind.startTv.visibility = View.VISIBLE
        mDatabind.centerIv.visibility = View.VISIBLE
        mDatabind.nextSportsIv.visibility = View.GONE
        mDatabind.centerBigIv.visibility = View.GONE
        when (curType) {
            AssessmentType.HighLeg -> {
                isHighLeg(false)
                curType = AssessmentType.UpDown
                mViewModel.title.set("起坐运动即将开始！")
                mDatabind.heartTv.setText("起坐次数")
                mDatabind.nextSportsIv.setImageResource(R.drawable.assessment_06)
                mDatabind.centerIv.setImageResource(R.drawable.assessment_07)

            }

            AssessmentType.UpDown -> {
                curType = AssessmentType.Grip
                mViewModel.title.set("握力运动即将开始！")
                mDatabind.heartTv.setText("握力最大值")
                mDatabind.sportsNumberUnitTv.visibility = View.VISIBLE
                mDatabind.nextSportsIv.setImageResource(R.drawable.assessment_08)
                mDatabind.centerIv.setImageResource(R.drawable.assessment_09)
                //移除高抬腿设备监听
                SMBleManager.delDeviceResultDataListener(this)
            }

            else -> {
                println("=============================")
                println("抬腿次数：" + (leftLegLifts + rightLegLifts))
                println("起坐：" + totalSitUpTimes)
                println("最大握力：" + maxGrip)
                println("=============================")
                //TODO 提交代码
                mViewModel.title.set("运动测评已全部完成！")
                mDatabind.startTv.text = "提交"
                mDatabind.startTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.fly), null)
            }
        }

    }

    fun isHighLeg(show: Boolean) {
        mDatabind.rTimesView.visibility = if (show) View.VISIBLE else View.GONE
        mDatabind.lTimesTv.visibility = if (show) View.VISIBLE else View.GONE
        mDatabind.lUnitTv.visibility = if (show) View.VISIBLE else View.GONE
        mDatabind.lTimesView.visibility = if (show) View.VISIBLE else View.GONE
        mDatabind.lLC.visibility = if (show) View.VISIBLE else View.GONE
        mDatabind.rLC.visibility = if (show) View.VISIBLE else View.GONE
        mDatabind.rTimesView.visibility = if (show) View.VISIBLE else View.GONE
        mDatabind.rTimesTv.visibility = if (show) View.VISIBLE else View.GONE
        mDatabind.rUnitTv.visibility = if (show) View.VISIBLE else View.GONE

    }

    inner class ProxyClick {

        fun clickFinish() {
            showExitDialog()
        }

        fun clickStart() {


            //标示所有都做完了
            if (mDatabind.startTv.text.equals("提交")) {
                val result = AssessmentUtils.testResult(leftLegLifts + rightLegLifts, totalSitUpTimes, maxGrip, "男".equals(App.userInfo.sex))
                val saveAssessmentRequest = SaveAssessmentRequest(maxGrip, leftLegLifts + rightLegLifts, result, totalSitUpTimes, waistline, weight)
                mViewModel.saveAssessment(saveAssessmentRequest)
            } else {
                startCountdown(4)
                mDatabind.centerIv.visibility = View.GONE
                mDatabind.startTv.visibility = View.GONE
            }

            mDatabind.centerIv.visibility = View.GONE
            if (curType == AssessmentType.HighLeg) {
                mViewModel.title.set("请高抬腿运动一分钟")

                mDatabind.centerBigIv.visibility = View.VISIBLE
                isHighLeg(true)
            } else if (curType == AssessmentType.UpDown) {
                mDatabind.centerBigIv.visibility = View.VISIBLE
                mDatabind.centerBigIv.setBackgroundResource(R.drawable.sports_sit_up_temp)
                mViewModel.title.set("请起坐运动一分钟")

            } else {
                mViewModel.title.set("请握3次握力器")
                mDatabind.centerBigIv.visibility = View.VISIBLE
                mDatabind.centerBigIv.setBackgroundResource(R.drawable.sports_grip_temp)
                mDatabind.countdownTv.text = "还剩3次"
                initBluetooth()
            }


        }
    }

    fun showOffLinePop() {
        val deviceOffLinePop = DeviceOffLinePop(this@SportsAssessmentActivity, SportsType.HIGH_KNEE, object : DeviceOffLinePop.Listener {


            override fun reconnect(type: DeviceType) {
            }

            override fun completed() {
            }
        })
        val pop = XPopup.Builder(this@SportsAssessmentActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(deviceOffLinePop)
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
        val roll = DeviceDataParse.parseData2Roll(data)
        val yaw = DeviceDataParse.parseData2Yaw(data)
        println("===leftroll:${roll}")
        println("===leftyaw:${yaw}")
        // 检查pitch是否大于100度
        if (pitch > 90) {
            // 抬腿过高，检查MediaPlayer是否已经在播放
            if (playAudioIsOpen && mediaPlayer_high != null && !mediaPlayer_high!!.isPlaying && curType == AssessmentType.HighLeg) {
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
                    if (curType == AssessmentType.HighLeg) {
                        leftLegLifts++
                        mViewModel.leftLegAngle.set("L ${leftLegmaxPitchInCycle.toInt()}°")
                        mViewModel.sportsNumber.set((leftLegLifts + rightLegLifts).toString())
                        mViewModel.leftLegCount.set(leftLegLifts.toString())
                    } else if (curType == AssessmentType.UpDown) {
                        leftSitUpTimes++
                        totalSitUpTimes = Math.max(leftSitUpTimes, rightSitUpTimes)
                        mViewModel.sportsNumber.set(totalSitUpTimes.toString())
                    }
                }
            }
            leftIsDescending = true
        }
        // 如果已经开始下降，并且当前pitch值比较低，认为一个周期结束
        if (leftIsDescending && pitch < 20) { // someLowThreshold是您定义的较低的值，比如10或更低
            // 周期结束，检查最大pitch值是否在25到30之间
            if (leftLegmaxPitchInCycle >= 25 && leftLegmaxPitchInCycle <= 30 && curType == AssessmentType.HighLeg) {
                // 播放提示音
                if (mediaPlayer_low != null && !mediaPlayer_low!!.isPlaying()) {
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
        leftLevelViews[leftLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected))
        leftOldLevel = leftLevel
    }

    override fun onRightDeviceData(data: ByteArray) {
        if (!isRunning) {
            return
        }
        val pitch = DeviceDataParse.parseData2Pitch(data)
        val roll = DeviceDataParse.parseData2Roll(data)
        val yaw = DeviceDataParse.parseData2Yaw(data)
        println("===rightroll:${roll}")
        println("===rightyaw:${yaw}")
        if (pitch > 90) {
            // 抬腿过高，播放提示音
            // 抬腿过高，检查MediaPlayer是否已经在播放
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
                    if (curType == AssessmentType.HighLeg) {
                        rightLegLifts++
                        mViewModel.sportsNumber.set((leftLegLifts + rightLegLifts).toString())
                        mViewModel.rightLegAngle.set("R ${rightLastPitch.toInt()}°")
                        mViewModel.rightLegCount.set(rightLegLifts.toString())
                    } else if (curType == AssessmentType.UpDown) {
                        rightSitUpTimes++
                        totalSitUpTimes = Math.max(leftSitUpTimes, rightSitUpTimes)
                        mViewModel.sportsNumber.set(totalSitUpTimes.toString())
                    }
                }
            }
            rightIsDescending = true
        }
        // 如果已经开始下降，并且当前pitch值比较低，认为一个周期结束
        if (rightIsDescending && pitch < 20) { // someLowThreshold是您定义的较低的值，比如10或更低
            // 周期结束，检查最大pitch值是否在25到30之间
            if (rightLegmaxPitchInCycle in 25.0..30.0) {
                // 播放提示音
                if (playAudioIsOpen && mediaPlayer_low != null && !mediaPlayer_low!!.isPlaying) {
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
        rightLevelViews[rightLevel].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sports_current_data_selected))
        rightOldLevel = rightLevel
    }

    override fun onGTSData(data: ByteArray) {
    }

    override fun onLeftHandGripsData(data: ByteArray) {
    }

    override fun onRightHandGripsData(data: ByteArray) {
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

    private lateinit var mBluetoothAdapter: BluetoothAdapter

    //蓝牙扫描辅助类
    private lateinit var mBleScanHelper: BleScanHelper
    private var maxGrip = 0
    private var gripCount = 0
    private var countTemp = 3//数据连续低于最大值5次则定位一次握力
    private var oldGrip = 0//odlGrip和grip连续相同三次定位一次握力，
    private var preGrip = -1//防止用户握完之后没动，握力次数会自动增加，记录上一次被记录的握力值，如果相同不计数
    private fun initBluetooth() {
        //初始化ble设配器
        val manager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = manager.adapter
        //判断蓝牙是否开启，如果关闭则请求打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            showToast("当前蓝牙不可用，请打开蓝牙")
        }
        //创建扫描辅助类
        mBleScanHelper = BleScanHelper(this)
        mBleScanHelper.setOnScanListener(object : BleScanHelper.onScanListener {
            @SuppressLint("MissingPermission")
            override fun onNext(device: BleDevice) {
                if (device.device.name != null && device.device.name.startsWith(DeviceType.LEFT_HAND_GRIPS.value)) {
                    val rawDataStr = DeviceDataParse.bytesToHexString(device.scanRecordBytes)
                    var lengthStr = rawDataStr?.substring(46, 50)
                    //将长度转10进制
                    val grip = Integer.parseInt(lengthStr, 16)
                    println("===握力值：" + grip)
                    if (maxGrip < grip) {
                        maxGrip = grip
                    }
                    if (preGrip == grip) {//如果用户握力一次之后没有再握，不计数
                        return
                    }
                    if (oldGrip == grip) {
                        countTemp--
                    }
                    oldGrip = grip
                    if (countTemp == 0) {//如果连续次数小于最大值标示是一次握力
                        mViewModel.sportsNumber.set((maxGrip / 10f).toString())
                        gripCount++
                        preGrip = oldGrip
                        oldGrip = 0
                        println("===握力值gripCount：" + gripCount)
                        countTemp = 3
                        mDatabind.countdownTv.text = "还剩${totalGripTimes - gripCount}次"
                        if (gripCount == 3) {
                            completed()
                        }
                    }
                }
            }

            override fun onFinish() {
            }
        })
        mBleScanHelper.startScanBle(1)
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

    fun showExitDialog() {
        val pop = XPopup.Builder(this).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).popupWidth(400).asConfirm("当前正在运动", "您确定要退出吗？", {
            finish()
        }, { })

        pop.show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        showExitDialog()

    }

}