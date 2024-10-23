package com.fjp.skeletalmuscle.data.model.bean

/**
 *Author:Mr'x
 *Time:2024/10/22
 *Description:
 */
class MainSports(val type:SportsType,val rate:String)

enum class SportsType(val type:Int){
    LEG_LIFT(0),DUMBBELL(1),PUSH_UP(2)
}