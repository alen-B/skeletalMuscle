package com.fjp.skeletalmuscle.data.model.bean.result

import com.fjp.skeletalmuscle.data.model.bean.result.Trend

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class SportTrendFlatSupportCalorieResult(
    val total: Int,
    val trend: List<Trend>
)
