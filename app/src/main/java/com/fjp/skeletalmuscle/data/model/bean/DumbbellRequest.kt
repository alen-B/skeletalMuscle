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
    val calorie: Int,
    val record_time: String
)

data class HeartRate(
    val rate_value: Int,
    val record_time: String
)

data class Record(
    val degree: Int,
    val record_time: String,
    val type: Int
)