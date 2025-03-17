package com.fjp.skeletalmuscle.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class DumbbellRequest(val calorie: MutableList<Calorie>, var end_time: Long, val heart_rate: MutableList<HeartRate>, val record: MutableList<Record>, val score: Int, val start_time: Long, val weight: Int, var sport_time: Int, var plan_sport_time: Int)

@Parcelize
data class Calorie(val calorie: Int, val record_time: String) : Parcelable

@Parcelize
data class HeartRate(val rate_value: Int, val record_time: String) : Parcelable


//type:1上举，2扩胸
@Parcelize
data class Record(val degree: Int, val record_time: String, val type: Int) : Parcelable