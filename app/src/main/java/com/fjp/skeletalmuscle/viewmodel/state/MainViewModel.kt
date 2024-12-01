package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.MainSports
import java.util.Date

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class MainViewModel : SMBaseViewModel() {
    val sportsData = mutableListOf<MainSports>()
    val currentDay = ObservableField(DateTimeUtil.formatDate(Date(), DateTimeUtil.DATE_PATTERN2))
    val curScore = ObservableField("0")
    val sportsTime = ObservableField("0")
    val heartRate = ObservableField("0")
    val endurance = ObservableField("0")
    val heat = ObservableField("0")

}
