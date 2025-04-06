package com.fjp.skeletalmuscle.app.util

import com.fjp.skeletalmuscle.data.model.bean.Record
import java.util.TreeSet

// 滑动平均滤波类

object ExerciseDetector {
    private const val ACCELERATION_THRESHOLD_Z = 5.0 // z加速度阈值
    private const val ACCELERATION_THRESHOLD_AccelX = 8.0 //AccelX阈值
    val records: MutableList<Record> = mutableListOf()
    var leftUpCount = 0 // 上举运动次数
    var rightUpCount = 0 // 右手上举运动次数
    var chestCount = 0 // 扩胸运动次数
    private var leftIsUpInProgress = false // 上举运动是否正在进行
    private var rightIsUpInProgress = false // 右手上举运动是否正在进行
    private var isDescending = false // 标记扩胸角度是否已经开始下降
    private var leftAngleSet = TreeSet<Double>()
    private var rightAngleSet = TreeSet<Double>()
    private var isExpanding = false
    private var leftIsExpanding = false
    private var rightIsExpanding = false
    private var startAngleLeft: Double = 0.0
    private var startAngleRight: Double = 0.0
    private var endAngleLeft: Double = 0.0
    private var endAngleRight: Double = 0.0

    //    private var expansionCount = 0 // 扩胸次数计数器
    var maxAngle = 0 // 扩胸最大角度
    private var leftYawTemp = 0
    private var leftYawMin = 360//左设备的最小角度
    private var leftYawMax = 0//左设备的最大角度
    private var rightYawTemp = 0
    private var rightYawMin = 360//右设备的最小角度
    private var rightYawMax = 0//右设备的最大角度
    var curAngle = 0
    private var useAngle = 0//用户面朝的角度
    private var totalAngle = 0f
    private var startAngle = 0f

    private var leftNegativeMinAngle = 0//左设备负值的最小值
    private var leftNegativeMaxAngle = 0//左设备负值的最大值

    private var leftPositiveMinAngle = 0//左设备正值的最小值
    private var leftPositiveMaxAngle = 0//左设备正值的最大值

    private var rightNegativeMinAngle = 0//右设备负值的最小值
    private var rightNegativeMaxAngle = 0//右设备负值的最大值

    private var rightPositiveMinAngle = 0//右设备正值的最小值
    private var rightPositiveMaxAngle = 0//右设备正值的最大值

    private var pointAngle = 0 //参考角度
    private var angles = mutableListOf<Int>()

    // 角速度阈值，用于判断是否开始运动
    private val angularVelocityThreshold = 1.0f

    // 角度变化阈值，用于判断是否完成一次扩胸运动
    private val angleChangeThreshold = 30f

    //上举运动只记录一个哑铃的，两个哑铃都记录次数显示会不对

    private var isAdd = false//数据是否是在增加

    private var expansionCount = 0
    private var previousLeftAngle: Int? = null
    private var currentLeftMinAngle: Int? = null
    private var currentLeftMaxAngle: Int? = null
    private var leftDeviceIsAscending = false
    private var leftExpansionAngle = 0
    private var rightExpansionAngle = 0


    private var previousRightAngle: Int? = null
    private var currentRightMinAngle: Int? = null
    private var currentRightMaxAngle: Int? = null
    private var rightDeviceIsAscending = false

    fun processLeftUpData(leftPitch: Double, leftAccelZ: Double) {
        if (leftPitch < 50) {
            // 检测上举运动
            if (leftAccelZ > ACCELERATION_THRESHOLD_Z && !leftIsUpInProgress) {
                leftIsUpInProgress = true
            } else if (leftAccelZ < ACCELERATION_THRESHOLD_Z && leftIsUpInProgress) {
                leftUpCount++
                records.add(Record(0, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 1, 1))
                leftIsUpInProgress = false
            }
        }
    }

    fun processRightUpData(rightPitch: Double, rightAccelZ: Double) {
        if (rightPitch < 50) {
            // 检测上举运动
            if (rightAccelZ > ACCELERATION_THRESHOLD_Z && !rightIsUpInProgress) {
                rightIsUpInProgress = true
            } else if (rightAccelZ < ACCELERATION_THRESHOLD_Z && rightIsUpInProgress) {
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
        if (leftPreviousAngle != null && kotlin.math.abs(leftYawTemp - leftPreviousAngle!!) > 180) {
            leftYawTemp -= 360
        }

        if (rightYaw < 0) {
            rightYawTemp = rightYaw.toInt() + 360
        } else {
            rightYawTemp = rightYaw.toInt()
        }
        if (rightPreviousAngle != null && kotlin.math.abs(rightYawTemp - rightPreviousAngle!!) > 180) {
            rightYawTemp -= 360
        }
        processAngle(leftYawTemp, rightYawTemp)
        if (leftCurrentMaxAngle != null && leftCurrentMinAngle != null && rightCurrentMaxAngle != null && rightCurrentMinAngle != null) {
            println("当前角度: leftYawTemp:${leftYawTemp}     rightYawTemp:${rightYawTemp}   ${(leftCurrentMaxAngle!! - leftCurrentMinAngle!!) + (rightCurrentMaxAngle!! - rightCurrentMinAngle!!)}")
        } else {
            println("当前角度: leftYawTemp:${leftYawTemp}     rightYawTemp:${rightYawTemp}")
        }

    }

    private var leftPreviousAngle: Int? = null
    private var leftCurrentMinAngle: Int? = null
    private var leftCurrentMaxAngle: Int? = null
    private var leftIsAscending = false

    private var rightPreviousAngle: Int? = null
    private var rightCurrentMinAngle: Int? = null
    private var rightCurrentMaxAngle: Int? = null
    private var rightIsAscending = false

    fun processAngle(leftAngle: Int, rightAngle: Int) {
        if (leftPreviousAngle != null) {
            if (leftCurrentMinAngle ==null) {
                leftCurrentMinAngle = leftAngle
            }
            if( leftCurrentMinAngle!! > leftAngle){
                leftCurrentMinAngle = leftAngle
            }
            if (leftCurrentMaxAngle == null) {
                leftCurrentMaxAngle = leftAngle
            }
            if( leftCurrentMaxAngle!! < leftAngle){
                leftCurrentMaxAngle = leftAngle
            }
            if (leftAngle >= leftPreviousAngle!!) {
                // 角度上升
                if (!leftIsAscending) {
                    // 开始上升阶段，重置最大最小值
                    leftIsAscending = true
//                    leftCurrentMinAngle = leftPreviousAngle
//                    leftCurrentMaxAngle = leftAngle
                }
            } else {
                // 角度下降
                if (leftIsAscending) {
                    // 从上升转为下降，完成一次扩胸
                    leftIsAscending = false
                    if (leftCurrentMinAngle != null && leftCurrentMaxAngle != null) {
                        leftExpansionAngle = leftCurrentMaxAngle!! - leftCurrentMinAngle!!
                        if (rightCurrentMinAngle != null && rightCurrentMaxAngle != null) {
                            rightExpansionAngle = rightCurrentMaxAngle!! - rightCurrentMinAngle!!
                        }
                        if (leftExpansionAngle > 30 && leftExpansionAngle<250) {
                            println("===左设备+1：${leftExpansionAngle}     左设备角度${rightExpansionAngle}")

                            chestCount++
                            records.add(Record(leftExpansionAngle + rightExpansionAngle, DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN_SS), 2, 1))
                        }
                    }
                    rightCurrentMinAngle = null
                    rightCurrentMaxAngle = null
                    leftCurrentMinAngle = null
                    leftCurrentMaxAngle = null
                }
            }
        }
        leftPreviousAngle = leftAngle


        if (rightPreviousAngle != null) {

            if (rightCurrentMinAngle == null) {
                rightCurrentMinAngle = rightAngle
            }
            if(rightCurrentMinAngle!! > rightAngle){
                rightCurrentMinAngle = rightAngle
            }
            if (rightCurrentMaxAngle == null) {
                rightCurrentMaxAngle = rightAngle
            }
            if( rightCurrentMaxAngle!! < rightAngle){
                rightCurrentMaxAngle = rightAngle
            }
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
        }
        rightPreviousAngle = rightAngle


    }


}