package com.fjp.skeletalmuscle.viewmodel.state

import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class TodaySportsDetailFragmentViewModel : SMBaseViewModel() {

}

enum class ChartType(val type: Int) {
    BURN_CALORIES(0),//消耗卡路里
    HEART_RATE_TREND(1),//心率趋势
    LEG_LIFTING_ANGLE(2),//抬腿角度
    INTENSITY_AND_TIME(3),//强度与时间
}

enum class DateType {
    DAY,//日
    MONTH,//月
    YEAR,//年
    All,//总
}