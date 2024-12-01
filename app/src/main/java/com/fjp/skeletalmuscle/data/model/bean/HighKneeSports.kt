package com.fjp.skeletalmuscle.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *Author:Mr'x
 *Time:2024/11/12
 *Description:
 * time:单位是分钟
 */
@Parcelize
data class HighKneeSports(
    var time: Long,
    var minHeartRate: Int,
    var maxHeartRate: Int,
    var times: Int,
    var calories: Double,
    var score: Int,
    var warmupTime: Double,
    var fatBurningTime: Double,
    var cardioTime: Double,
    var breakTime: Double,
) : Parcelable