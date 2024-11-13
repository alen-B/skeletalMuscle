package com.fjp.skeletalmuscle.data.model.bean

/**
 *Author:Mr'x
 *Time:2024/10/22
 *Description:
 */
class MainSports(val type:SportsType,val sports:HighKneeSports)

enum class SportsType(val type:Int){
    HIGH_KNEE(0),DUMBBELL(1),PLANK(2),HAND_GRIPS(3)
}