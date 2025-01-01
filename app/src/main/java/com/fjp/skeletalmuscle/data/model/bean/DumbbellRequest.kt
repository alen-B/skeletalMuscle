package com.fjp.skeletalmuscle.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class DumbbellRequest(val calorie: List<Calorie>, val end_time: Int, val heart_rate: List<HeartRate>, val record: List<Record>, val score: Int, val start_time: Int, val weight: Int,var sport_time:Int)

@Parcelize
data class Calorie(val calorie: Int, val record_time: String): Parcelable

@Parcelize
data class HeartRate(val rate_value: Int, val record_time: String): Parcelable

@Parcelize
data class Record(val degree: Int, val record_time: String, val type: Int): Parcelable