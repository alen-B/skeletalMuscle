package com.fjp.skeletalmuscle.viewmodel.state

import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.TodaySports
import com.fjp.skeletalmuscle.data.model.bean.TodayDetailSportsType

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class TodaySportsDetailViewModel : SMBaseViewModel() {
    val dataArr = mutableListOf<TodaySports>()

    init {
        dataArr.add(TodaySports(TodayDetailSportsType.HIGH_KNEE))
        dataArr.add(TodaySports(TodayDetailSportsType.DUMBBELL))
        dataArr.add(TodaySports(TodayDetailSportsType.Plank))
        dataArr.add(TodaySports(TodayDetailSportsType.DURATION_OF_EXERCISE))
        dataArr.add(TodaySports(TodayDetailSportsType.Heart_RATE))
        dataArr.add(TodaySports(TodayDetailSportsType.MUSCLE_PROPORTION))
        dataArr.add(TodaySports(TodayDetailSportsType.AEROBIC_ENERGY_CONSUMPTION))
    }

}
