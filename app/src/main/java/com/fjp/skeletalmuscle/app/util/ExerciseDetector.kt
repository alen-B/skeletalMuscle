package com.fjp.skeletalmuscle.app.util

import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.data.model.bean.Record

// 滑动平均滤波类

object ExerciseDetector {
    private const val ACCELERATION_THRESHOLD_Z = 8.0 // z加速度阈值
    val upMinAngle = 40
    val records: MutableList<Record> = mutableListOf()
    var leftUpCount = 0 // 上举运动次数
    var rightUpCount = 0 // 右手上举运动次数
    var chestCount = 0 // 扩胸运动次数
    private var leftIsUpInProgress = false // 上举运动是否正在进行
    private var rightIsUpInProgress = false // 右手上举运动是否正在进行

    var chestShowAngle = 0 // 扩胸最大角度
    private var leftYawTemp = 0
    private var rightYawTemp = 0
    var leftUpTitle = "上举运动"
    var chestTitle = "扩胸运动"

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

    private var leftErrorTimes = 0
    private var rightErrorTimes = 0
    private var chestExpansionTimes = 0

    var sendAngleZero = false

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

    fun processLeftUpData(leftPitch: Double,leftRoll: Double ,leftAccelZ: Double) {

        // 检测上举运动
        if (leftAccelZ > ACCELERATION_THRESHOLD_Z && !leftIsUpInProgress) {
            leftIsUpInProgress = true
            leftErrorTimes = 0
        } else if (leftAccelZ < 3 && leftIsUpInProgress) {
            if (leftPitch > upMinAngle||leftRoll>upMinAngle) {
                leftErrorTimes++
            }
            if (leftErrorTimes != 0) {
                leftIsUpInProgress = false
                leftUpTitle = "您左手握哑铃方式不正确，请及时调整"
                eventViewModel.sportWarningEvent.postValue(true)
                leftErrorTimes = 0
                return
            }
            leftIsUpInProgress = false
            leftErrorTimes = 0
            leftUpCount++
            records.add(Record(0, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 1, 1))
            leftUpTitle = "上举运动"
            eventViewModel.sportWarningEvent.postValue(false)
        }
    }

    fun processRightUpData(rightPitch: Double, rightRoll: Double, rightAccelZ: Double) {
        // 检测上举运动
        if (rightAccelZ > ACCELERATION_THRESHOLD_Z && !rightIsUpInProgress) {
            rightIsUpInProgress = true
            rightErrorTimes = 0
        } else if (rightAccelZ < 3 && rightIsUpInProgress) {

            if (rightPitch > upMinAngle || rightRoll > upMinAngle) {
                rightErrorTimes++
            }
            if (rightErrorTimes != 0) {
                rightIsUpInProgress = false
                leftUpTitle = "您右手握哑铃方式不正确，请及时调整"
                eventViewModel.sportWarningEvent.postValue(true)
                rightErrorTimes = 0
                return
            }
            rightIsUpInProgress = false
            rightErrorTimes = 0
            rightUpCount++
            records.add(Record(0, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 1, 2))
            if (leftUpTitle != "上举运动") {
                leftUpTitle = "上举运动"
            }
            eventViewModel.sportWarningEvent.postValue(false)
        }
    }

    @Synchronized
    fun processData(leftPitch: Double, leftYaw: Double, leftRoll: Double, leftAccelX: Double, leftAccelY: Double, leftAccelZ: Double, rightPitch: Double, rightYaw: Double, rightRoll: Double, rightAccelX: Double, rightAccelY: Double, rightAccelZ: Double, isUp: Boolean) {
        if (leftPitch > 120 || leftPitch < 70 || rightPitch > 120 || rightPitch < 70) {
            chestExpansionTimes++
        }
        println("leftRoll:${leftRoll}")
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


    private fun processAngle(leftAngle: Int, rightAngle: Int) {
        println("当前角度：${leftAngle}")
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


            if (leftAngle > leftPreviousAngle!!) {
                // 角度上升
                if (!leftIsAscending) {
                    leftIsAscending = true
                    chestExpansionTimes = 0
                }

            } else {
                // 角度下降
                if (leftIsAscending) {
                    // 从上升转为下降，完成一次扩胸
                    leftIsAscending = false
                    sendAngleZero = false
                    leftExpansionAngle = leftCurrentMaxAngle!! - leftCurrentMinAngle!!
                    rightExpansionAngle = rightCurrentMaxAngle!! - rightCurrentMinAngle!!
                    if (leftExpansionAngle > angleChangeThreshold && rightExpansionAngle > angleChangeThreshold && leftExpansionAngle + rightExpansionAngle < 280) {
                        if (chestTitle != "扩胸运动") {
                            chestTitle = "扩胸运动"
                        }

                        if (chestExpansionTimes >= 6) {
                            chestTitle = "扩胸运动姿势不标准，请及时调整"
                            eventViewModel.sportWarningEvent.postValue(true)
                            chestExpansionTimes = 0
                            rightCurrentMinAngle = null
                            rightCurrentMaxAngle = null
                            leftCurrentMinAngle = null
                            leftCurrentMaxAngle = null
                            return
                        }
                        chestExpansionTimes = 0
                        chestShowAngle = leftExpansionAngle + rightExpansionAngle

                        chestCount++
                        preRightCurrentMinAngle = rightCurrentMinAngle
                        preLeftCurrentMinAngle = leftCurrentMinAngle
                        records.add(Record(leftExpansionAngle + rightExpansionAngle, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2, 1))
                        eventViewModel.sportWarningEvent.postValue(false)

                    } else if (leftExpansionAngle > 50 && rightExpansionAngle < 50 || (leftExpansionAngle < 50 && rightExpansionAngle > 50)) {
                        chestTitle = "双手打开角度不够，请及时调整"
                        eventViewModel.sportWarningEvent.postValue(true)
                    }
                }
                rightCurrentMinAngle = null
                rightCurrentMaxAngle = null
                leftCurrentMinAngle = null
                leftCurrentMaxAngle = null
            }
        }
        leftPreviousAngle = leftAngle
        rightPreviousAngle = rightAngle


    }


}