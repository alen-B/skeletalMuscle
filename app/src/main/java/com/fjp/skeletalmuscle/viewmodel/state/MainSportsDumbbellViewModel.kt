package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

class MainSportsDumbbellViewModel : SMBaseViewModel() {
    val curScore = ObservableField("90")
    val sportsTime = ObservableField("0")
    val heartRate = ObservableField("0")
    val weight = ObservableField("0")
    val heat = ObservableField("0")
}