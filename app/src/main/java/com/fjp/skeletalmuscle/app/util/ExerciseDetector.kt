package com.fjp.skeletalmuscle.app.util

import com.fjp.skeletalmuscle.data.model.bean.Record
import kotlin.math.abs

object ExerciseDetector {
    private const val ACCELERATION_THRESHOLD_Z = 5.0 // z加速度阈值
    val records:MutableList<Record> =  mutableListOf()
    var upCount = 0 // 上举运动次数
    var chestCount = 0 // 扩胸运动次数
    private var isUpInProgress = false // 上举运动是否正在进行
    private var isAdded = false//当前动作是否已经添加了一次扩胸运动
    private var minAngle = 30.0//最小角度变化
    private var maxAngle = 90.0//最大角度变化


    //上举运动只记录一个哑铃的，两个哑铃都记录次数显示会不对
    fun processData(leftPitch: Double, leftYaw: Double, leftAccelY: Double, leftAccelZ: Double, rightPitch: Double, rightYaw: Double,isLeftData:Boolean) {
        if (leftPitch < 120 && leftPitch > 80) {
            if(isLeftData){
                // 检测上举运动
                if (leftAccelZ > ACCELERATION_THRESHOLD_Z && !isUpInProgress) {
                    isUpInProgress = true
                } else if (leftAccelZ < ACCELERATION_THRESHOLD_Z && isUpInProgress) {
                    upCount++
                    records.add(Record(0,DateTimeUtil.formatDate(System.currentTimeMillis(),DateTimeUtil.DATE_PATTERN_SS),1))
                    isUpInProgress = false
                }
            }
        } else {
            println("-----rightYaw${rightYaw}   leftYaw:${leftYaw}    abs:${+abs(rightYaw - leftYaw)}")
            if (!isAdded && abs(rightYaw - leftYaw) > maxAngle) {
//                println("abs(leftYaw - rightYaw):${abs(rightYaw - leftYaw)}  isAdded:${isAdded}")
                chestCount++
                records.add(Record(0,DateTimeUtil.formatDate(System.currentTimeMillis(),DateTimeUtil.DATE_PATTERN_SS),2))
                isAdded = true
            }

            if (abs(rightYaw - leftYaw) < minAngle) {
                isAdded = false
            }

        }

    }


}