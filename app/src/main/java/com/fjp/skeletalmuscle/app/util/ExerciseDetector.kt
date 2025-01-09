package com.fjp.skeletalmuscle.app.util

object ExerciseDetector {
    private const val PITCH_THRESHOLD_UP = 40.0 // 上举运动的俯仰角阈值
    private const val PITCH_THRESHOLD_DOWN = 20.0 // 上举运动的俯仰角阈值
    private const val YAW_THRESHOLD = 30.0 // 扩胸运动的横滚角阈值
    private const val ACCELERATION_THRESHOLD = 10.0 // 加速度阈值
    private const val ACCELERATION_THRESHOLD_Z = 5.0 // z加速度阈值
    var upCount = 0 // 上举运动次数
        private set
    var chestCount = 0 // 扩胸运动次数
        private set
    private var isUpInProgress = false // 上举运动是否正在进行
    private var isChestInProgress = false // 扩胸运动是否正在进行
    private var minYaw = 0.0
    private var maxYaw = 0.0
    private var isExtend = false
    private var isAdded = false//当前动作是否已经添加了一次扩胸运动
    fun processData(pitch: Double, yaw: Double, accelY: Double, accelZ: Double) {
        if (pitch < 120 && pitch > 80) {
            // 检测上举运动
            if (accelZ > ACCELERATION_THRESHOLD_Z && !isUpInProgress) {
                isUpInProgress = true
            } else if (accelZ < ACCELERATION_THRESHOLD_Z && isUpInProgress) {
                upCount++
                isUpInProgress = false
            }
        } else {
//            if (minYaw == 0.0) {
//                minYaw = yaw
//                maxYaw = yaw
//            }
//
//            if (maxYaw < yaw) {
//                maxYaw = yaw
//                //表示在扩胸
//                isExtend = true
//            }else{
//                isAdded=false
//                isExtend = false
//            }
//            println("+++角度:maxYaw：${maxYaw }   minYaw:${minYaw}  yaw:${yaw}")
//            if (!isExtend && !isAdded) {
//                isAdded=true
//                chestCount++
//                isExtend = false
////                println("+++最大角度:${Math.abs(maxYaw - minYaw)}")
//                minYaw = yaw
//                maxYaw = yaw
//            }
//            if (yaw > YAW_THRESHOLD && !isChestInProgress) {
//                isChestInProgress = true
//            } else if (yaw < YAW_THRESHOLD && accelY > ACCELERATION_THRESHOLD && isChestInProgress) {
//                chestCount++
//                isChestInProgress = false
//            }

            println("==yaw:${yaw}    accelY:${accelY}   isChestInProgress:${isChestInProgress}")
        }


        // 检测扩胸运动

    }

    fun movingAverageFilter(data: DoubleArray, windowSize: Int): DoubleArray {
        val dataLength = data.size
        if (dataLength < windowSize) {
            return data
        }
        val filteredData = DoubleArray(dataLength - windowSize + 1)
        for (i in filteredData.indices) {
            var sum = 0.0
            for (j in 0 until windowSize) {
                sum += data[i + j]
            }
            filteredData[i] = sum / windowSize
        }
        return filteredData
    }


}