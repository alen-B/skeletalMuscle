package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.MainSports
import java.util.Calendar

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class MainViewModel : SMBaseViewModel() {
    private var calendar: Calendar = Calendar.getInstance()
    private var dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)
    val sportsData = mutableListOf<MainSports>()
    val currentDay = ObservableField(dayOfMonth.toString())
    val curScore = ObservableField("0")
    val sportsTime = ObservableField("0")
    val heartRate = ObservableField("0")
    val endurance = ObservableField("0")
    val heat = ObservableField("0")

}
