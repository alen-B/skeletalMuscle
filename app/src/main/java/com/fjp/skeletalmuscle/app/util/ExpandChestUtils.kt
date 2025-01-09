package com.fjp.skeletalmuscle.app.util


/**
 * 数据点类，存储传感器数据。
 */
data class DataPoint( // 时间戳或样本序号
    var timestamp: Long,
    var pitch: Double,// Pitch角度（俯仰角）
    var yaw: Double, // Yaw角度（偏航角）
    var roll: Double,// Roll角度（横滚角）
    var ax: Double,//X方向加速度 (g)
    var ay: Double,// Y方向加速度 (g)
    var az: Double,// Z方向加速度 (g)
    var wx: Double, // 绕X轴角速度 (°/s)
    var wy: Double,// 绕Y轴角速度 (°/s)
    var wz: Double // 绕Z轴角速度 (°/s)
)

/**
 * 动作类型枚举。
 */
enum class ActionType {
    OVERHEAD,  // 上举运动
    EXPANSION // 扩胸运动
}

/**
 * 动作类，存储检测到的动作信息。
 */
class Action(type: ActionType, start: Long, end: Long, deltaPitch: Double, deltaYaw: Double, maxAngle: Double) {
    var type: ActionType
    var startTimestamp: Long
    var endTimestamp: Long
    var deltaPitch: Double
    var deltaYaw: Double
    var maxAngle // 上举时为最大Pitch，扩胸时为最大Yaw
            : Double

    init {
        this.type = type
        startTimestamp = start
        endTimestamp = end
        this.deltaPitch = deltaPitch
        this.deltaYaw = deltaYaw
        this.maxAngle = maxAngle
    }
}

/**
 * 动作检测器类，实现动作识别和计数。
 */
 class ActionDetector {
    companion object {
        // 阈值参数，可根据实际数据调整
        private const val THRESHOLD_DELTA_PITCH = 16.0 // 上举动作Pitch增量阈值
        private const val THRESHOLD_DELTA_YAW = 50.0 // 扩胸动作Yaw增量阈值

        // 权重参数，可根据实际数据调整
        private const val ALPHA1 = 1.0
        private const val ALPHA2 = 1.0
        private const val ALPHA3 = 1.0
        private const val BETA1 = 1.0
        private const val BETA2 = 1.0

        // 阈值判断动作结束的回落范围
        private const val THRESHOLD_PITCH_END = 90.0 // 上举动作结束时Pitch回落阈值
        private const val THRESHOLD_YAW_END = 20.0 // 扩胸动作结束时Yaw回落阈值
    }
    // 存储检测到的动作列表
    private val detectedActions: MutableList<Action> = ArrayList()

    // 动作计数
    var overheadCount = 0
    var expansionCount = 0
    var inAction = false
    var currentActionType: ActionType? = null
    var actionStart = 0L
    var actionEnd = 0L
    var initialPitch = 0.0
    var initialYaw = 0.0
    var maxPitch = Double.MIN_VALUE
    var maxYaw = Double.MIN_VALUE
    var oldDataPoint: DataPoint? = null

    /**
     * 处理传感器数据，检测动作。
     *
     * @param dataPoints 数据点列表
     */
    fun process(dataPoints: DataPoint) {
        if (oldDataPoint == null) {
            oldDataPoint = dataPoints
            return
        }
      println(dataPoints)
        val prev = oldDataPoint!!
        val current = dataPoints
        if (!inAction) {
            // 检测动作开始
            val deltaPitch = Math.abs(current.pitch - prev.pitch)
            val deltaYaw = current.yaw - prev.yaw
            // 判断是否为上举动作开始
            if (deltaPitch > THRESHOLD_DELTA_PITCH && Math.abs(deltaYaw) < THRESHOLD_DELTA_YAW) {
                inAction = true
                currentActionType = ActionType.OVERHEAD
                actionStart = prev.timestamp
                initialPitch = prev.pitch
                initialYaw = prev.yaw
                maxPitch = current.pitch
            } else if (Math.abs(deltaYaw) > THRESHOLD_DELTA_YAW && Math.abs(deltaPitch) < THRESHOLD_DELTA_PITCH) {
                inAction = true
                currentActionType = ActionType.EXPANSION
                actionStart = prev.timestamp
                initialPitch = prev.pitch
                initialYaw = prev.yaw
                maxYaw = current.yaw
            }
        } else {
            // 动作进行中
            if (currentActionType == ActionType.OVERHEAD) {
                // 更新最大Pitch
                if (current.pitch > maxPitch) {
                    maxPitch = current.pitch
                }

                // 检测动作结束
                if (current.pitch < THRESHOLD_PITCH_END) {
                    inAction = false
                    actionEnd = current.timestamp
                    // 计算增量和其他特征
                    val deltaPitch = maxPitch - initialPitch
                    val deltaYaw = Math.abs(current.yaw - initialYaw)
                    val aVert = Math.abs(current.az) // 垂直方向加速度
                    val wPitch = Math.abs(current.wy) // 假设绕Y轴的角速度关联Pitch

                    // 计算评分
                    val upScore = ALPHA1 * deltaPitch + ALPHA2 * aVert + ALPHA3 * wPitch
                    val outScore = BETA1 * deltaYaw + BETA2 * 0.0 // 扩胸评分为0，因为是上举
                    // 分类
                    var detectedType: ActionType
//                    if (upScore > outScore) {
                        detectedType = ActionType.OVERHEAD
                        overheadCount++
//                    } else {
//                        detectedType = ActionType.EXPANSION
//                        expansionCount++
//                    }

                    // 记录动作
                    val action = Action(detectedType, actionStart, actionEnd, deltaPitch, deltaYaw, maxPitch)
                    detectedActions.add(action)

                    // 打印动作信息
                    if (detectedType == ActionType.OVERHEAD) {
                        println("Delta Pitch: $deltaPitch°, Max Pitch: $maxPitch°")
                    } else {
                        println("Delta Yaw: $deltaYaw°, Max Yaw: $maxYaw°")
                    }
                    println("=======================================")
                }
            } else if (currentActionType == ActionType.EXPANSION) {
                // 更新最大Yaw
                if (current.yaw > maxYaw) {
                    maxYaw = current.yaw
                }

                // 检测动作结束
                if (Math.abs(current.yaw - initialYaw) < THRESHOLD_YAW_END) {
                    inAction = false
                    actionEnd = current.timestamp
                    // 计算增量和其他特征
                    val deltaYaw = Math.abs(maxYaw - initialYaw)
                    val deltaPitch = Math.abs(current.pitch - initialPitch)
                    val wYaw = Math.abs(current.wz) // 假设绕Z轴的角速度关联Yaw

                    // 计算评分
                    val upScore = ALPHA1 * 0.0 + ALPHA2 * 0.0 + ALPHA3 * 0.0 // 上举评分为0，因为是扩胸
                    val outScore = BETA1 * deltaYaw + BETA2 * wYaw

                    // 分类
                    var detectedType: ActionType
//                    if (outScore > upScore) {
                        detectedType = ActionType.EXPANSION
                        expansionCount++
//                    } else {
//                        detectedType = ActionType.OVERHEAD
//                        overheadCount++
//                    }

                    // 记录动作
                    val action = Action(detectedType, actionStart, actionEnd, deltaPitch, deltaYaw, maxYaw)
                    detectedActions.add(action)

                    // 打印动作信息
                    if (detectedType == ActionType.EXPANSION) {
                        println("Delta Yaw: $deltaYaw°, Max Yaw: $maxYaw°")
                    } else {
                        println("Delta Pitch: $deltaPitch°, Max Pitch: $maxPitch°")
                    }

                }
            }
        }
        oldDataPoint = dataPoints
        // 打印总计数
        println("Total Overhead Movements: $overheadCount")
        println("Total Expansion Movements: $expansionCount")
    }

    /**
     * 获取检测到的动作列表。
     *
     * @return 动作列表
     */
    fun getDetectedActions(): List<Action> {
        return detectedActions
    }

}