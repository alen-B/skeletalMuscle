package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

/**
 *Author:Mr'x
 *Time:2024/11/1
 *Description:
 */
class SportsDumbbellCompletedViewModel : SMBaseViewModel() {
    val curScore = ObservableField("0")
    val sportsTime = ObservableField("0")
    val heartRate = ObservableField("0")
    val weight = ObservableField("0")
    val heat = ObservableField("0")
    val calorie = ObservableField("0")
    val upTimes = ObservableField("0")
    val upDegree = ObservableField("0")
    val expandChestDegree = ObservableField("0")
    val expandChestTimes = ObservableField("0")
}