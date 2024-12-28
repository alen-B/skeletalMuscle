package com.fjp.skeletalmuscle.data.model.bean.result

import android.os.Parcelable
import com.fjp.skeletalmuscle.data.model.bean.Calorie
import com.fjp.skeletalmuscle.data.model.bean.HeartRate
import kotlinx.parcelize.Parcelize

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */

@Parcelize
data class TodayDataResult(val avg_rate_value: Int = 0, val calorie_total: Int = 0, val score: Int = 0, val sport_dumbbell: SportDumbbell, val sport_flat_support: SportFlatSupport, val sport_lift_leg: SportLiftLeg, val sport_time: Int = 0,var is_assess:Int) : Parcelable

@Parcelize
data class SportDumbbell(val sport_time: Long = 0,val avg_expand_chest_degree: Int = 0, val avg_rate_value: Int = 0, val avg_up_degree: Int = 0, val calorie: List<Calorie> = listOf(),  val expand_chest_degree: List<ExpandChestDegree> = listOf(), val expand_chest_times: Int = 0, val heart_rate: List<HeartRate> = listOf(), val id: Int = 0, val max_rate_value: Int = 0, val min_rate_value: Int = 0, val score: Int = 0, val sum_calorie: Int = 0, val up_degree: List<UpDegree> = listOf(), val up_times: Int = 0, val user_id: Int = 0, val weight: Int = 0) : Parcelable

@Parcelize
data class SportFlatSupport(val sport_time: Long = 0,val avg_rate_value: Int = 0, val calorie: List<Calorie> = listOf(),val heart_rate: List<HeartRate> = listOf(), val id: Int = 0, val max_rate_value: Int = 0, val min_rate_value: Int = 0, val score: Int = 0,  val sum_calorie: Int = 0, val user_id: Int = 0) : Parcelable

@Parcelize
data class SportLiftLeg(val sport_time: Long = 0, val avg_left_degree: Int = 0, val avg_rate_value: Int = 0, val avg_right_degree: Int = 0, val calorie: List<Calorie> = listOf(), val cardiorespiratory_endurance: Double = 0.0, val efficient_grease_burning: Int = 0, val extreme_breakthrough: Int = 0, val heart_lung_enhancement: Int = 0, val heart_rate: List<HeartRate> = listOf(), val id: Int = 0, val left_times: Int = 0, val max_rate_value: Int = 0, val min_rate_value: Int = 0, val record: List<TodayRecord> = listOf(), val right_times: Int = 0, val score: Int = 0, val sum_calorie: Int = 0, val user_id: Int = 0, val warm_up_activation: Int = 0,val left_sport_amount:Int,val right_sport_amount:Int,var plan_sport_time:Int=0) : Parcelable

@Parcelize
data class ExpandChestDegree(val data_id: Int = 0, val expand_chest_degree: Int = 0, val id: Int = 0, val record_time: Int = 0, val type: Int = 0, val up_degree: Int = 0, val user_id: Int = 0) : Parcelable

@Parcelize
data class UpDegree(val data_id: Int = 0, val expand_chest_degree: Int = 0, val id: Int = 0, val record_time: Int = 0, val type: Int = 0, val up_degree: Int = 0, val user_id: Int = 0) : Parcelable

@Parcelize
data class TodayRecord(val id: Int, val user_id: Int, val data_id: Int, val record_time: Int, val type: Int, val left_degree: Int, val right_degree: Int) : Parcelable