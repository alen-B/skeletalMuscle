package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class SingleSelectViewModel : SMBaseViewModel() {
    val dataArr = mutableListOf<String>()


}

enum class SingleSelectType(val number:Int){
    HEIGHT(0),WEIGHT(1),WAIST_LINE(2),DAY_ONE_WEEK(3),SUPPORT_TIME(4)
}