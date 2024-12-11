package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

class MainSportsDumbbellViewModel : SMBaseViewModel() {
    val curScore = ObservableField("-")
    val sportsTime = ObservableField("-")
    val heartRate = ObservableField("-")
    val weight = ObservableField("-")
    val heat = ObservableField("-")
}