package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

class MainSportsHighKneeViewModel : SMBaseViewModel() {
    val curScore = ObservableField("-")
    val sportsTime = ObservableField("-")
    val heartRate = ObservableField("-")
    val endurance = ObservableField("-")
    val heat = ObservableField("-")
}