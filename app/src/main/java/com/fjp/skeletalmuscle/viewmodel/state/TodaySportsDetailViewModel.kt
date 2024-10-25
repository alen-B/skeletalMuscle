package com.fjp.skeletalmuscle.viewmodel.state

import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.TodaySports
import com.fjp.skeletalmuscle.data.model.bean.TodaySportsType

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class TodaySportsDetailViewModel : SMBaseViewModel() {
    val dataArr = mutableListOf<TodaySports>()

    init {
        dataArr.add(TodaySports(TodaySportsType.HIGH_KNEE))
        dataArr.add(TodaySports(TodaySportsType.DUMBBELL))
        dataArr.add(TodaySports(TodaySportsType.Plank))
        dataArr.add(TodaySports(TodaySportsType.DURATION_OF_EXERCISE))
        dataArr.add(TodaySports(TodaySportsType.Heart_RATE))
        dataArr.add(TodaySports(TodaySportsType.MUSCLE_PROPORTION))
        dataArr.add(TodaySports(TodaySportsType.AEROBIC_ENERGY_CONSUMPTION))
    }

}
