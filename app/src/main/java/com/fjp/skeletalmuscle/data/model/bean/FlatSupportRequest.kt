package com.fjp.skeletalmuscle.data.model.bean

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class FlatSupportRequest(
    val calorie: List<Calorie>,
    val end_time: Int,
    val heart_rate: List<HeartRate>,
    val score: Int,
    val start_time: Int
)
