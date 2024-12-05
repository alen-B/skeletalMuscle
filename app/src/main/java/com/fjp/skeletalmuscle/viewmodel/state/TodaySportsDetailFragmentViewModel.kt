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
    DUMBBELL_AVG_ANGLE(4),//哑铃运动-平均角度
    All_CALORIES(5),//所有运动消耗的卡路里
}

enum class DateType(val value:String) {
    DAY("1"),//日
    WEEK("2"),//周
    MONTH("3"),//月
    YEAR("4"),//年
}