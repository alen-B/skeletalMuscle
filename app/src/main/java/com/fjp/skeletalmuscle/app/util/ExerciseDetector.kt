package com.fjp.skeletalmuscle.app.util

import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.data.model.bean.Record

// 滑动平均滤波类

object ExerciseDetector {
    private const val ACCELERATION_THRESHOLD_Z = 8.0 // z加速度阈值
    val upMinAngle=40
    val records: MutableList<Record> = mutableListOf()
    var leftUpCount = 0 // 上举运动次数
    var rightUpCount = 0 // 右手上举运动次数
    var chestCount = 0 // 扩胸运动次数
    private var leftIsUpInProgress = false // 上举运动是否正在进行
    private var rightIsUpInProgress = false // 右手上举运动是否正在进行

    var chestShowAngle = 0 // 扩胸最大角度
    private var leftYawTemp = 0
    private var rightYawTemp = 0

    // 角度变化阈值，用于判断是否完成一次扩胸运动
    private val angleChangeThreshold = 30f


    private var leftExpansionAngle = 0
    private var rightExpansionAngle = 0

    private var leftPreviousAngle: Int? = null
    private var leftCurrentMinAngle: Int? = null
    private var leftCurrentMaxAngle: Int? = null
    private var leftIsAscending = false

    private var rightPreviousAngle: Int? = null
    private var rightCurrentMinAngle: Int? = null
    private var rightCurrentMaxAngle: Int? = null
    private var rightIsAscending = false

    private var preRightCurrentMinAngle: Int? = null
    private var preLeftCurrentMinAngle: Int? = null

    fun clear() {
        leftUpCount = 0
        rightUpCount = 0
        chestCount = 0
        records.clear()
        leftExpansionAngle = 0
        rightExpansionAngle = 0
        rightCurrentMinAngle = null
        rightCurrentMaxAngle = null
        leftCurrentMinAngle = null
        leftCurrentMaxAngle = null
    }

    fun processLeftUpData(leftPitch: Double, leftAccelZ: Double) {
        if (leftPitch < upMinAngle) {
            // 检测上举运动
            if (leftAccelZ > ACCELERATION_THRESHOLD_Z && !leftIsUpInProgress) {
                leftIsUpInProgress = true
            } else if (leftAccelZ < 3 && leftIsUpInProgress) {
                leftUpCount++
                records.add(Record(0, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 1, 1))
                leftIsUpInProgress = false
            }
        }
    }

    fun processRightUpData(rightPitch: Double, rightAccelZ: Double) {
        if (rightPitch < upMinAngle) {
            // 检测上举运动
            if (rightAccelZ > ACCELERATION_THRESHOLD_Z && !rightIsUpInProgress) {
                rightIsUpInProgress = true
            } else if (rightAccelZ < 3 && rightIsUpInProgress) {
                rightUpCount++
                records.add(Record(0, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 1, 2))
                rightIsUpInProgress = false
            }
        }
    }

    @Synchronized
    fun processData(leftPitch: Double, leftYaw: Double, leftRoll: Double, leftAccelX: Double, leftAccelY: Double, leftAccelZ: Double, rightPitch: Double, rightYaw: Double, rightRoll: Double, rightAccelX: Double, rightAccelY: Double, rightAccelZ: Double, isUp: Boolean) {
//        println("=======leftPitch:${leftPitch}")
        if (leftYaw < 0) {
            leftYawTemp = leftYaw.toInt() + 360
        } else {
            leftYawTemp = leftYaw.toInt()
        }
        if (leftPreviousAngle != null && leftYawTemp - leftPreviousAngle!! > 180) {
            leftYawTemp -= 360
        }
        if (leftPreviousAngle != null && leftYawTemp - leftPreviousAngle!! < -180) {
            leftYawTemp += 360
        }
        if (rightYaw < 0) {
            rightYawTemp = rightYaw.toInt() + 360
        } else {
            rightYawTemp = rightYaw.toInt()
        }
        if (rightPreviousAngle != null && rightYawTemp - rightPreviousAngle!! > 180) {
            rightYawTemp -= 360
        }
        if (rightPreviousAngle != null && rightYawTemp - rightPreviousAngle!! < -180) {
            rightYawTemp += 360
        }
        processAngle(leftYawTemp, rightYawTemp)

    }


    fun processAngle(leftAngle: Int, rightAngle: Int) {
        if (leftPreviousAngle != null) {
            if (leftCurrentMinAngle == null) {
                leftCurrentMinAngle = leftAngle
            }
            if (leftCurrentMinAngle!! > leftAngle) {
                leftCurrentMinAngle = leftAngle
            }
            if (leftCurrentMaxAngle == null) {
                leftCurrentMaxAngle = leftAngle
            }
            if (leftCurrentMaxAngle!! < leftAngle) {
                leftCurrentMaxAngle = leftAngle
            }

            if (rightCurrentMinAngle == null) {
                rightCurrentMinAngle = rightAngle
            }
            if (rightCurrentMinAngle!! > rightAngle) {
                rightCurrentMinAngle = rightAngle
            }
            if (rightCurrentMaxAngle == null) {
                rightCurrentMaxAngle = rightAngle
            }
            if (rightCurrentMaxAngle!! < rightAngle) {
                rightCurrentMaxAngle = rightAngle
            }


            if (leftAngle >= leftPreviousAngle!!) {

                // 角度上升
                if (!leftIsAscending) {
                    leftIsAscending = true
                    chestShowAngle = 0
                }
            } else {
                // 角度下降
                if (leftIsAscending) {
                    // 从上升转为下降，完成一次扩胸
                    leftIsAscending = false
                    leftExpansionAngle = leftCurrentMaxAngle!! - leftCurrentMinAngle!!
                    rightExpansionAngle = rightCurrentMaxAngle!! - rightCurrentMinAngle!!
                    if (leftExpansionAngle > angleChangeThreshold && rightExpansionAngle > angleChangeThreshold && leftExpansionAngle + rightExpansionAngle < 280) {
                        println("===左设备+1：   ${leftExpansionAngle.toInt()}     左设备角度      ${rightExpansionAngle.toInt()}")
                        eventViewModel.sportWarningEvent.postValue(false)
                        chestShowAngle = leftExpansionAngle + rightExpansionAngle
                        chestCount++
                        preRightCurrentMinAngle = rightCurrentMinAngle
                        preLeftCurrentMinAngle = leftCurrentMinAngle
                        records.add(Record(leftExpansionAngle + rightExpansionAngle, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2, 1))

                    }

                    if (leftExpansionAngle > 50 && rightExpansionAngle < 50 || (leftExpansionAngle < 50 && rightExpansionAngle > 50)) {
                        eventViewModel.sportWarningEvent.postValue(true)
                    }
                    rightCurrentMinAngle = null
                    rightCurrentMaxAngle = null
                    leftCurrentMinAngle = null
                    leftCurrentMaxAngle = null
                }
            }
        }
        leftPreviousAngle = leftAngle
        rightPreviousAngle = rightAngle

//        if (rightPreviousAngle != null) {


//            if (rightAngle >= rightPreviousAngle!!) {
//                // 角度上升
//                if (!rightIsAscending) {
//                    // 开始上升阶段，重置最大最小值
//                    rightIsAscending = true
////                    rightCurrentMinAngle = rightPreviousAngle
////                    rightCurrentMaxAngle = rightAngle
//                }
//            } else {
//                // 角度下降
//                if (rightIsAscending) {
//                    // 从上升转为下降，完成一次扩胸
//                    rightIsAscending = false
//                    if (rightCurrentMinAngle != null && rightCurrentMaxAngle != null) {
//                        rightExpansionAngle = rightCurrentMaxAngle!! - rightCurrentMinAngle!!
//                        if (leftCurrentMinAngle != null && leftCurrentMaxAngle != null) {
//                            leftExpansionAngle = leftCurrentMaxAngle!! - leftCurrentMinAngle!!
//                        }
//                        if (rightExpansionAngle > 30) {
//                            println("===右设备+1：${rightExpansionAngle}     左设备角度${leftExpansionAngle}")
//
//                            chestCount++
//                            records.add(Record(leftExpansionAngle + rightExpansionAngle, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2, 1))
//                        }
//                    }
//                    rightCurrentMinAngle = null
//                    rightCurrentMaxAngle = null
//                    leftIsAscending = false
//                    leftCurrentMinAngle = null
//                    leftCurrentMaxAngle = null
//                }
//            }
//        }


    }


}