package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.SportsType

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class TodaySelectSuportsViewModel : SMBaseViewModel() {
    val sportsType = ObservableField<SportsType?>()


}