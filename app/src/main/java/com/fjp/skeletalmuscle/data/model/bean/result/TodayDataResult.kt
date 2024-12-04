package com.fjp.skeletalmuscle.data.model.bean.result

import android.os.Parcelable
import com.fjp.skeletalmuscle.data.model.bean.Calorie
import com.fjp.skeletalmuscle.data.model.bean.HeartRate
import com.fjp.skeletalmuscle.data.model.bean.Record
import kotlinx.parcelize.Parcelize

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */

@Parcelize
data class TodayDataResult(
    val avg_rate_value: Int,
    val calorie_total: Int,
    val score: Int,
    val sport_dumbbell: SportDumbbell,
    val sport_flat_support: SportFlatSupport,
    val sport_lift_leg: SportLiftLeg,
    val sport_time: Int
): Parcelable
@Parcelize
data class SportDumbbell(
    val avg_expand_chest_degree: Int,
    val avg_rate_value: Int,
    val avg_up_degree:Int,
    val calorie: List<Calorie>,
    val end_time: Long,
    val expand_chest_degree: List<ExpandChestDegree>,
    val expand_chest_times: Int,
    val heart_rate: List<HeartRate>,
    val id: Int,
    val max_rate_value: Int,
    val min_rate_value: Int,
    val score: Int,
    val start_time: Long,
    val sum_calorie: Int,
    val up_degree: List<UpDegree>,
    val up_times: Int,
    val user_id: Int,
    val weight: Int
): Parcelable
@Parcelize
data class SportFlatSupport(
    val avg_rate_value: Int,
    val calorie: List<Calorie>,
    val end_time: Long,
    val heart_rate: List<HeartRate>,
    val id: Int,
    val max_rate_value: Int,
    val min_rate_value: Int,
    val score: Int,
    val start_time: Long,
    val sum_calorie: Int,
    val user_id: Int
): Parcelable
@Parcelize
data class SportLiftLeg(
    val avg_left_degree: List<Int>,
    val avg_rate_value: Int,
    val avg_right_degree: Int,
    val calorie: List<Calorie>,
    val cardiorespiratory_endurance: Int,
    val efficient_grease_burning: Int,
    val end_time: Long,
    val extreme_breakthrough: Int,
    val heart_lung_enhancement: Int,
    val heart_rate: List<HeartRate>,
    val id: Int,
    val left_times: Long,
    val max_rate_value: Int,
    val min_rate_value: Int,
    val record: List<Record>,
    val right_times: Int,
    val score: Int,
    val start_time: Long,
    val sum_calorie: Int,
    val user_id: Int,
    val warm_up_activation: Int
): Parcelable

@Parcelize
data class ExpandChestDegree(
    val data_id: Int,
    val expand_chest_degree: Int,
    val id: Int,
    val record_time: Int,
    val type: Int,
    val up_degree: Int,
    val user_id: Int
): Parcelable

@Parcelize
data class UpDegree(
    val data_id: Int,
    val expand_chest_degree: Int,
    val id: Int,
    val record_time: Int,
    val type: Int,
    val up_degree: Int,
    val user_id: Int
): Parcelable
