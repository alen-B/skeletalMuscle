package com.fjp.skeletalmuscle.data.model.bean

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class LiftLegRequest(
    var calorie: MutableList<Calorie>,//消耗的卡路里
    var end_time: Long,//结束时间
    var heart_rate: MutableList<HeartRate>,//记录心率
    var record: MutableList<Record>,//记录高抬腿数据
    var score: Int,//分数
    var start_time: Long,//开始时间
    var cardiorespiratory_endurance: Double,//心肺耐力
    var efficient_grease_burning: Int,//高效燃脂
    var warm_up_activation: Int,//暖身激活
    var extreme_breakthrough: Int,//极限突破
    var heart_lung_enhancement: Int,//心肺提升
    var sport_time: Int,//运动时长
)
