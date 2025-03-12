package com.fjp.skeletalmuscle.app.util

import com.fjp.skeletalmuscle.data.model.bean.Record
import java.util.TreeSet
import kotlin.math.abs

// 滑动平均滤波类
class MovingAverageFilter(private val windowSize: Int) {
    private val buffer = mutableListOf<Double>()

    fun filter(value: Double): Double {
        buffer.add(value)
        if (buffer.size > windowSize) {
            buffer.removeAt(0)
        }
        return buffer.average()
    }
}
// 陀螺仪数据类，包含 X、Y、Z 轴的角度和加速度
data class GyroData(
    val ax: Float, // X 轴加速度
    val ay: Float, // Y 轴加速度
    val az: Float, // Z 轴加速度
    val rx: Float, // X 轴角度
    val ry: Float, // Y 轴角度
    val rz: Float  // Z 轴角度
)
object ExerciseDetector {
    private const val ACCELERATION_THRESHOLD_Z = 5.0 // z加速度阈值
    private const val ACCELERATION_THRESHOLD_AccelX = 8.0 //AccelX阈值
    val records: MutableList<Record> = mutableListOf()
    var upCount = 0 // 上举运动次数
    var chestCount = 0 // 扩胸运动次数
    private var isUpInProgress = false // 上举运动是否正在进行
    private var isDescending = false // 标记扩胸角度是否已经开始下降
    private var leftAngleSet=TreeSet<Double>()
    private var rightAngleSet=TreeSet<Double>()
    private var isExpanding = false
    private var startAngleLeft: Double = 0.0
    private var startAngleRight: Double = 0.0
    private var endAngleLeft: Double = 0.0
    private var endAngleRight: Double = 0.0
    private var expansionCount = 0 // 扩胸次数计数器

    // 为左右陀螺仪的 Y 轴角度创建滑动平均滤波器
    private var initialLeftData: GyroData? = null
    private var initialRightData: GyroData? = null
    private var isCalibrated = false
    private var calibrationInterval = 100 // 持续校准间隔，单位：数据帧
    private var frameCount = 0

    // 动态阈值相关变量
    private var axThresholdStart = 0.5f
    private var axThresholdEnd = 0.2f
    private var azThresholdStart = 0.5f
    private var azThresholdEnd = 0.2f
    private var rxThresholdStart = 5f
    private var rxThresholdEnd = 2f
    private var rzThresholdStart = 5f
    private var rzThresholdEnd = 2f
    private var ryThresholdStart = 10f
    private var ryThresholdEnd = 5f

    // 为各轴的参数创建滑动平均滤波器
    private val leftAxFilter = MovingAverageFilter(5)
    private val rightAxFilter = MovingAverageFilter(5)
    private val leftAzFilter = MovingAverageFilter(5)
    private val rightAzFilter = MovingAverageFilter(5)
    private val leftRxFilter = MovingAverageFilter(5)
    private val rightRxFilter = MovingAverageFilter(5)
    private val leftRyFilter = MovingAverageFilter(5)
    private val rightRyFilter = MovingAverageFilter(5)
    private val leftRzFilter = MovingAverageFilter(5)
    private val rightRzFilter = MovingAverageFilter(5)

    // 滤波处理
    private fun filterData(data: Double, isLeft: Boolean): Double {
        val filteredRy = if (isLeft) leftRyFilter.filter(data) else rightRyFilter.filter(data)
        return filteredRy
    }

    //上举运动只记录一个哑铃的，两个哑铃都记录次数显示会不对
    @Synchronized
    fun processData(leftPitch: Double, leftYaw: Double,leftRoll: Double, leftAccelX: Double,leftAccelY: Double, leftAccelZ: Double, rightPitch: Double, rightYaw: Double,rightRoll: Double,rightAccelX: Double,rightAccelY: Double,rightAccelZ: Double, isLeftData: Boolean) {
        if (leftPitch < 120 && leftPitch > 80) {
            if (isLeftData) {
                // 检测上举运动
                if (leftAccelZ > ACCELERATION_THRESHOLD_Z && !isUpInProgress) {
                    isUpInProgress = true
                } else if (leftAccelZ < ACCELERATION_THRESHOLD_Z && isUpInProgress) {
                    upCount++
                    records.add(Record(0, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 1))
                    isUpInProgress = false
                }
            }
        } else {
            val filteredLeftData = filterData(leftYaw, true)
            val filteredRightData = filterData(rightYaw, false)

            // 特征提取：根据 Y 轴角度变化判断是否开始扩胸
            if (!isExpanding && abs(filteredLeftData- filteredRightData) > 10f) {
                isExpanding = true
                startAngleLeft = filteredLeftData
                startAngleRight = filteredRightData
            }

            // 判断是否结束扩胸
            if (isExpanding && abs(filteredLeftData - filteredRightData) < 5f) {
                isExpanding = false
                endAngleLeft = filteredLeftData
                endAngleRight = filteredRightData
                expansionCount++ // 扩胸运动结束，计数器加一

                records.add(Record(0, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2))
            }

//            if (isLeftData) {
//                leftAngleSet.add(leftYaw)
//                rightAngleSet.add(rightYaw)
//                // 检测扩胸运动
//                if (leftAccelX > ACCELERATION_THRESHOLD_AccelX && !isDescending) {
//                    isDescending = true
//                } else if (leftAccelX < ACCELERATION_THRESHOLD_AccelX && isDescending) {
//                    val leftAngle = leftAngleSet.last()-leftAngleSet.first()
//                    val rightAngle = rightAngleSet.last()-rightAngleSet.first()
//                    if(leftAngle > 50 && rightAngle>50){
//                        isDescending = false
//                        chestCount++
//                        println("111111111111=leftAngle:   ${leftAngle}     rightAngle:${rightAngle}")
//                        records.add(Record(0, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2))
//                        leftAngleSet.clear()
//                        rightAngleSet.clear()
//                    }
//
//
//                }
//            }
//            if(isLeftData){
//
//                println("========leftAccelX:${leftAccelX}       leftAccelY:${leftAccelY}")
//            }
//            println("=====leftAccelX${leftAccelX}     rightAccelX: ${rightAccelX}")
//            println("-----leftAccelY${leftAccelY}     rightAccelY: ${rightAccelY}")
//            println("-----leftAccelZ${leftAccelZ}     rightAccelZ: ${rightAccelZ}")

//            useAngle = (leftYaw +rightYaw) / 2 // 防止用户所占东南方向会导致数先变小然后再次变大。面向南是0度，面向东和西是90度。面向北是180度
//            curAngle = (abs(rightYaw - useAngle) + abs(leftYaw - useAngle)).toInt()
//            if (curAngle > maxAngle) {
//                maxAngle = curAngle
//                isDescending = false
//            }
//
//            if (curAngle < preAngle) {
//                if (!isDescending) {
//                    if (curAngle > thresholdAngle) {
//                        chestCount++
//                        records.add(Record(maxAngle.toInt(), DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2))
//                    }
//
//                }
//                isDescending = true
//            }
//
//            if (isDescending && curAngle < thresholdAngle) {
//                maxAngle = 0
//            }
//
//            preAngle = curAngle






//
//            if (abs(rightYaw - useAngle) + abs(leftYaw - useAngle) < thresholdAngle) {
//                isAdded = false
//                chestCount++
//                records.add(Record(maxAngle.toInt(), DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2))
//
//                maxAngle = 0.0
//            } else if (curAngle > thresholdAngle) {
//                isAdded = true
//            }


        }

    }


}