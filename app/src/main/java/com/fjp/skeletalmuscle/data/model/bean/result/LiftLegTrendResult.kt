package com.fjp.skeletalmuscle.data.model.bean.result

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class LiftLegTrendResult(val avg_left_degree: Int, val avg_right_degree: Int, val trend: List<LeftTrend>)

data class LeftTrend(val time: String, val left_degree: Double, val right_degree: Double)