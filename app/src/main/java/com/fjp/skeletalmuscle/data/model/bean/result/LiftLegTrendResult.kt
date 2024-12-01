package com.fjp.skeletalmuscle.data.model.bean.result

import com.fjp.skeletalmuscle.data.model.bean.result.Trend

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class LiftLegTrendResult(
    val avg_left_degree: Int,
    val avg_right_degree: Int,
    val trend: List<Trend>
)
