package com.fjp.skeletalmuscle.data.model.bean.result

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class HeartRateResult(val avg: Double, val max: Int, val trend: List<HeartRateTrend>)
data class HeartRateTrend(val rate_value: Int, val time: String)