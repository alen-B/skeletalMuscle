package com.fjp.skeletalmuscle.data.model.bean.result

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class LiftLegCalorieResult(val total: Int, val trend: List<Trend>)

data class Trend(val calorie: Int, var time: String)