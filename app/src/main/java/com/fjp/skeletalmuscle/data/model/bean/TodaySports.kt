package com.fjp.skeletalmuscle.data.model.bean

/**
 *Author:Mr'x
 *Time:2024/10/24
 *Description:
 */
class TodaySports(val type:TodaySportsType) {

}
enum class TodaySportsType(val value:Int){
    HIGH_KNEE(0),//高抬腿
    DUMBBELL(1),//哑铃
    Plank(2),//平板支撑
    DURATION_OF_EXERCISE(3),//运动时长
    Heart_RATE(4),//心率区间
    MUSCLE_PROPORTION(5),//肌肉比例
    AEROBIC_ENERGY_CONSUMPTION(6),//有氧能耗
}