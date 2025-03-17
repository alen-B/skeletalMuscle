package com.fjp.skeletalmuscle.data.model.bean.result

import android.os.Parcelable
import com.fjp.skeletalmuscle.data.model.bean.Calorie
import com.fjp.skeletalmuscle.data.model.bean.HeartRate
import kotlinx.parcelize.Parcelize

/**
 *Author:Mr'x
 *Time:2024/12/31
 *Description:
 */
@Parcelize
data class SavePlankResult(val avg_rate_value: Int, val calorie: Int, val device_no: String, val end_time: Int, val is_finish: Int, val khr1: Double, val khr2: Double, val max_rate_value: Int, val min_rate_value: Int, val plan_sport_time: Int, val score: Int, val sport_time: Long, val start_time: Int, val user_id: Int, val calorie_list: List<Calorie>, val heart_rate: List<HeartRate>

) : Parcelable