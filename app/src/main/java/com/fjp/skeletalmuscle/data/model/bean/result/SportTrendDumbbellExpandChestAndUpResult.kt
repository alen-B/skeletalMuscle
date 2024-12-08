package com.fjp.skeletalmuscle.data.model.bean.result


/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class SportTrendDumbbellExpandChestAndUpResult(val avg_expand_chest_degree: Int, val trend: List<DummbbellTrend>)

data class DummbbellTrend(val time: String, val up_degree: Double)