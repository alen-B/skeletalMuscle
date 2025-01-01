package com.fjp.skeletalmuscle.data.model.bean.result

import android.os.Parcelable
import com.fjp.skeletalmuscle.data.model.bean.Calorie
import com.fjp.skeletalmuscle.data.model.bean.HeartRate
import com.fjp.skeletalmuscle.data.model.bean.Record
import kotlinx.parcelize.Parcelize

/**
 *Author:Mr'x
 *Time:2025/1/1
 *Description:
 */
@Parcelize
data class SaveDumbbellResult(
    val avg_expand_chest_degree: Int,
    val avg_rate_value: Int,
    val avg_up_degree: Int,
    val calorie: Int,
    val calorie_list: List<Calorie>,
    val device_no: String,
    val end_time: Int,
    val expand_chest_times: Int,
    val heart_rate: List<HeartRate>,
    val is_finish: Int,
    val khr1: Double,
    val khr2: Double,
    val max_rate_value: Int,
    val min_rate_value: Int,
    val plan_sport_time: Int,
    val record: List<Record>,
    val score: Int,
    val sport_time: Long,
    val start_time: Int,
    val up_times: Int,
    val user_id: Int,
    val weight: Int
): Parcelable
