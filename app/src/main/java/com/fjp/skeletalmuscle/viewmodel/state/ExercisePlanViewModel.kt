package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

/**
 *Author:Mr'x
 *Time:2024/10/26
 *Description:
 */
class ExercisePlanViewModel : SMBaseViewModel() {
    val sportsIcon = ObservableField(R.drawable.exercise_plan_height_leg)
    val sportsType = ObservableField("")
    val sportsTime = ObservableField("2")
    val sportsWeight = ObservableField("1")
    val sportsUplift = ObservableField("10")
    val sportsExpandChest = ObservableField("10")
    val sportsAngle = ObservableField("90")

}