package com.fjp.skeletalmuscle.viewmodel.state

import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.Sports
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class SuportsViewModel : SMBaseViewModel() {
    val dataArr = mutableListOf<Sports>()
    init {
        dataArr.add(Sports(appContext.getString(R.string.sports_type_no),false))
        dataArr.add(Sports(appContext.getString(R.string.sports_type_run),false))
        dataArr.add(Sports(appContext.getString(R.string.sports_type_hie),false))
        dataArr.add(Sports(appContext.getString(R.string.sports_type_jumping_rope),false))
        dataArr.add(Sports(appContext.getString(R.string.sports_type_badminton),false))
        dataArr.add(Sports(appContext.getString(R.string.sports_type_jumping_exercises),false))
    }

}