package com.fjp.skeletalmuscle.data.model.bean

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class DumbbellRequest(
    val calorie: List<Calorie>,
    val end_time: Int,
    val heart_rate: List<HeartRate>,
    val record: List<Record>,
    val score: Int,
    val start_time: Int,
    val weight: Int
)

data class Calorie(
    val calorie: Int,//卡路里
    val record_time: String//时间
)

data class HeartRate(
    val rate_value: Int,//心率
    val record_time: String//时间
)

data class Record(
    val degree: Int,//抬腿角度
    val record_time: String,//抬腿时间
    val type: Int //抬腿类型1左腿，2右腿
)