package com.fjp.skeletalmuscle.viewmodel.state

import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.Sports
import com.fjp.skeletalmuscle.data.model.bean.SportsChild
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class SuportsViewModel : SMBaseViewModel() {
    val dataArr = arrayListOf<Sports>()

    init {
        dataArr.add(Sports(appContext.getString(R.string.sports_type_no), arrayListOf(), true))
        dataArr.add(Sports("有氧运动", arrayListOf(SportsChild("跑步", false), SportsChild("游泳", false), SportsChild("骑自行车", false), SportsChild("跳绳", false), SportsChild("快走", false)), false))
        dataArr.add(Sports("力量型运动", arrayListOf(SportsChild("哑铃训练", false), SportsChild("杠铃训练", false), SportsChild("自重训练", false), SportsChild("阻力带训练", false), SportsChild("器械训练", false)), false))
        dataArr.add(Sports("柔韧性运动", arrayListOf(SportsChild("瑜伽", false), SportsChild("普拉提", false), SportsChild("拉伸", false), SportsChild("太极拳", false)), false))
        dataArr.add(Sports("球类运动", arrayListOf(SportsChild("篮球", false), SportsChild("足球", false), SportsChild("羽毛球", false), SportsChild("网球", false), SportsChild("乒乓球", false)), false))
    }

}