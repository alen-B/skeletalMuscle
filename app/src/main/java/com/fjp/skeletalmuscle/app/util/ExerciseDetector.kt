package com.fjp.skeletalmuscle.app.util

import com.fjp.skeletalmuscle.data.model.bean.Record
import java.util.TreeSet
import kotlin.math.abs

// 滑动平均滤波类

object ExerciseDetector {
    private const val ACCELERATION_THRESHOLD_Z = 5.0 // z加速度阈值
    private const val ACCELERATION_THRESHOLD_AccelX = 8.0 //AccelX阈值
    val records: MutableList<Record> = mutableListOf()
    var upCount = 0 // 上举运动次数
    var chestCount = 0 // 扩胸运动次数
    private var isUpInProgress = false // 上举运动是否正在进行
    private var isDescending = false // 标记扩胸角度是否已经开始下降
    private var leftAngleSet = TreeSet<Double>()
    private var rightAngleSet = TreeSet<Double>()
    private var isExpanding = false
    private var startAngleLeft: Double = 0.0
    private var startAngleRight: Double = 0.0
    private var endAngleLeft: Double = 0.0
    private var endAngleRight: Double = 0.0
    private var expansionCount = 0 // 扩胸次数计数器
    var maxAngle = 0 // 扩胸最大角度
    private var leftYawTemp = 0
    private var rightYawTemp = 0
    var curAngle = 0
    private var useAngle = 0//用户面朝的角度
    private var totalAngle = 0f
    private var startAngle = 0f

    // 角速度阈值，用于判断是否开始运动
    private val angularVelocityThreshold = 1.0f

    // 角度变化阈值，用于判断是否完成一次扩胸运动
    private val angleChangeThreshold = 30f

    //上举运动只记录一个哑铃的，两个哑铃都记录次数显示会不对
    @Synchronized
    fun processData(leftPitch: Double, leftYaw: Double, leftRoll: Double, leftAccelX: Double, leftAccelY: Double, leftAccelZ: Double, rightPitch: Double, rightYaw: Double, rightRoll: Double, rightAccelX: Double, rightAccelY: Double, rightAccelZ: Double, isLeftData: Boolean, isUp: Boolean) {
//        println("=======leftPitch:${leftPitch}")
        println("===leftYaw:${leftYaw}    ===rightYaw:${rightYaw}")
        println("===curAngle:${curAngle}")
        if (leftPitch < 40 && isUp) {
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
        }

        if (!isUp) {
            if (isLeftData) {
                if (leftYaw < 0) {
                    leftYawTemp = leftYaw.toInt() + 360
                } else {
                    leftYawTemp = leftYaw.toInt()
                }
                if (rightYaw < 0) {
                    rightYawTemp = rightYaw.toInt() + 360
                } else {
                    rightYawTemp = rightYaw.toInt()
                }
                if (leftYaw < 90 && rightYaw > -90) {
                    curAngle = abs(leftYaw - rightYaw).toInt()
                } else if (rightYaw < 90 && leftYaw > -90) {
                    curAngle = abs(rightYaw - leftYaw).toInt()
                } else {
                    curAngle = abs(leftYawTemp - rightYawTemp)
                }
                println("===leftYaw:${leftYaw}    ===rightYaw:${rightYaw}")
//                println("===rightYawTemp:${rightYawTemp}   ===rightYaw:${rightYaw}")
                println("===curAngle:${curAngle}")


                if (maxAngle < curAngle) {
                    maxAngle = curAngle
                }
                if (maxAngle > 180) {
                    maxAngle = 360 - 180
                }
                if (curAngle > 60 && !isExpanding) {
                    isExpanding = true
                } else if (curAngle < 60 && isExpanding) {
                    chestCount++
                    records.add(Record(maxAngle, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2))
                    isExpanding = false
//                    println("===maxAngle:${maxAngle}")
                    maxAngle = 0
                }
            }


        }

    }


}