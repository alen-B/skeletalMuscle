package com.fjp.skeletalmuscle.app.util

import com.fjp.skeletalmuscle.data.model.bean.Record
import kotlin.math.abs

object ExerciseDetector {
    private const val ACCELERATION_THRESHOLD_Z = 5.0 // z加速度阈值
    val records: MutableList<Record> = mutableListOf()
    var upCount = 0 // 上举运动次数
    var chestCount = 0 // 扩胸运动次数
    private var isUpInProgress = false // 上举运动是否正在进行
    private var isAdded = false//当前动作是否已经添加了一次扩胸运动
    private var thresholdAngle = 30.0//
    private var maxAngle = 0//最大角度变化
    private var useAngle = 0.0//用户所面对的角度，正南方向是0度，面向东和面向西都是90度。面向北是180度
    private var isDescending = false // 标记扩胸角度是否已经开始下降
    private var preAngle = 0 // 上一次的角度
    private var curAngle = 0 // 当前角度

    //上举运动只记录一个哑铃的，两个哑铃都记录次数显示会不对
    @Synchronized
    fun processData(leftPitch: Double, leftYaw: Double, leftAccelY: Double, leftAccelZ: Double, rightPitch: Double, rightYaw: Double, isLeftData: Boolean) {
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
            println("-----rightYaw${rightYaw}   leftYaw:${leftYaw}    useAngle:${useAngle}   rightYaw-useAngle:${rightYaw - useAngle}  leftYaw-useAngle ${leftYaw - useAngle}")

            useAngle = abs(leftYaw - rightYaw) / 2 // 防止用户所占东南方向会导致数先变小然后再次变大。面向南是0度，面向东和西是90度。面向北是180度
            curAngle = (abs(rightYaw - useAngle) + abs(leftYaw - useAngle)).toInt()
            if (curAngle > maxAngle) {
                maxAngle = curAngle
                isDescending = false
            }

            if (curAngle < preAngle) {
                if (!isDescending) {
                    if (curAngle > thresholdAngle) {
                        chestCount++
                        records.add(Record(maxAngle.toInt(), DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2))
                    }

                }
                isDescending = true
            }

            if (isDescending && curAngle < thresholdAngle) {
                maxAngle = 0
            }

            preAngle = curAngle
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