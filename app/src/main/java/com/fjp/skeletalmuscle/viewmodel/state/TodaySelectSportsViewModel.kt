package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.TodaySportsType
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class TodaySelectSportsViewModel : SMBaseViewModel() {
    val sportsType = ObservableField<ArrayList<TodaySportsType>>()

    init {
        val list = mutableListOf<TodaySportsType>()
        list.add(TodaySportsType(SportsType.HIGH_KNEE, appContext.getString(R.string.today_sports_data_type1), appContext.getString(R.string.today_sports_height_leg_detail), 5, true))
        list.add(TodaySportsType(SportsType.DUMBBELL, appContext.getString(R.string.today_sports_data_type2), appContext.getString(R.string.today_sports_dumbbell_detail), 4, false))
        list.add(TodaySportsType(SportsType.PLANK, appContext.getString(R.string.today_sports_data_type3), appContext.getString(R.string.today_sports_plank_detail), 3, false))
        sportsType.set(list as ArrayList<TodaySportsType>?)
    }

    var curSports: TodaySportsType = sportsType.get()!![0]

}