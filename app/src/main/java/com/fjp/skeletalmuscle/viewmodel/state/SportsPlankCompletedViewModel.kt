package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

/**
 *Author:Mr'x
 *Time:2024/11/1
 *Description:
 */
class SportsPlankCompletedViewModel : SMBaseViewModel() {
    val curScore = ObservableField("0")
    val sportsTime = ObservableField("0")
    val heartRate = ObservableField("0")
    val endurance = ObservableField("0.0")
    val heat = ObservableField("0")
    val calorie = ObservableField("0")
}