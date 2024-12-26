package com.fjp.skeletalmuscle.data.model.bean.result

/**
 *Author:Mr'x
 *Time:2024/12/7
 *Description:
 */
//data class AssessmentHistoryResult(
//    val `data`: List<Data>
//)

data class AssessmentHistoryData(
    val bmi: Double,
    val grip: Int,
    val id: Int,
    val lift_leg: Int,
    val month: Int,
    val result: String,
    val sit_up: Int,
    val test_time: Long,
    val user_id: Int,
    val waistline: Int,
    val weight: Int,
    val year: String,
    val result_description: String
)