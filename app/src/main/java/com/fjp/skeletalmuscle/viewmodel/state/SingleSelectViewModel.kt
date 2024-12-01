package com.fjp.skeletalmuscle.viewmodel.state

import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class SingleSelectViewModel : SMBaseViewModel() {
    val dataArr = mutableListOf<Int>()


}

enum class SingleSelectType(val type: Int) {
    HEIGHT(0), WEIGHT(1), WAIST_LINE(2), DAY_ONE_WEEK(3), SUPPORT_TIME(4), SEX(5)
}