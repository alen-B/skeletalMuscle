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
