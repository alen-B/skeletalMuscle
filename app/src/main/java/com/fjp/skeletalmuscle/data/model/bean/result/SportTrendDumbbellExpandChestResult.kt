package com.fjp.skeletalmuscle.data.model.bean.result

import com.fjp.skeletalmuscle.data.model.bean.result.Trend

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class SportTrendDumbbellExpandChestResult(
    val avg_expand_chest_degree: Int,
    val trend: List<Trend>
)
