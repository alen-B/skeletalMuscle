package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

class TodaySportsHighKneeViewModel : SMBaseViewModel() {
    val curScore = ObservableField("90")
    val sportsTime = ObservableField("30")
    val heartRate = ObservableField("0")
    val endurance = ObservableField("0.32")
    val heat = ObservableField("0")
    val totalCount = ObservableField("0")
    val leftCount = ObservableField("0")
    val rightCount = ObservableField("0")
    val avgHeart = ObservableField("0")
    val maxHeart = ObservableField("0")
}