package com.fjp.skeletalmuscle.data.model.bean

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class FlatSupportRequest(val start_time: Long, var end_time: Long, val score: Int, val calorie: List<Calorie>, val heart_rate: List<HeartRate>, var plan_sport_time: Int = 0, var sport_time: Int = 0)
