package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

class TodaySportsDumbbellViewModel : SMBaseViewModel() {
    val curScore = ObservableField("90")
    val sportsTime = ObservableField("30")
    val heartRate = ObservableField("0")
    val weight = ObservableField("0.32")
    val heat = ObservableField("0")
    val calorie = ObservableField("0")
    val upTimes = ObservableField("0")
    val expandChestDegree = ObservableField("0")
    val expandChestTimes = ObservableField("0")
}