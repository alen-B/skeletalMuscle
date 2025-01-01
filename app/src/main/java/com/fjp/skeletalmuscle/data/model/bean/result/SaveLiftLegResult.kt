package com.fjp.skeletalmuscle.data.model.bean.result

import android.os.Parcelable
import com.fjp.skeletalmuscle.data.model.bean.Calorie
import com.fjp.skeletalmuscle.data.model.bean.HeartRate
import com.fjp.skeletalmuscle.data.model.bean.Record
import kotlinx.parcelize.Parcelize

/**
 *Author:Mr'x
 *Time:2024/12/31
 *Description:
 */
@Parcelize
data class SaveLiftLegResult(
    val avg_left_degree: Double,
    val avg_rate_value: Int,
    val avg_right_degree: Double,
    val calorie: Int,
    val calorie_list: List<Calorie>,
    val cardiorespiratory_endurance: Double,
    val device_no: String,
    val efficient_grease_burning: Int,
    val end_time: Int,
    val extreme_breakthrough: Int,
    val heart_lung_enhancement: Int,
    val heart_rate: List<HeartRate>,
    val is_finish: Int,
    val khr1: Double,
    val khr2: Double,
    val left_sport_amount: Double,
    val left_times: Int,
    val max_rate_value: Int,
    val min_rate_value: Int,
    val plan_sport_time: Int,
    val record: List<Record>,
    val right_sport_amount: Double,
    val right_times: Int,
    val score: Int,
    val sport_amount_total: Double,
    val sport_time: Long,
    val start_time: Int,
    val times_total: Int,
    val user_id: Int,
    val warm_up_activation: Int
): Parcelable
