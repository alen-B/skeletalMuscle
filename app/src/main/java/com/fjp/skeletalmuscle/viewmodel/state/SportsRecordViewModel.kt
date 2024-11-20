package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.SportsRecord


class SportsRecordViewModel : SMBaseViewModel() {
    val legSportsDate = arrayListOf(
        SportsRecord("运动时长","28","分钟"),
        SportsRecord("消耗卡路里","328","千卡"),
        SportsRecord("左腿平均角度","92","°"),
        SportsRecord("右腿平均角度","88","°"),
        SportsRecord("运动总量","128","次"),
        SportsRecord("心肺耐力","0.32",""))
    val dumbbellSportsDate = arrayListOf(
        SportsRecord("运动时长","28","分钟"),
        SportsRecord("哑铃重量","5","kg"),
        SportsRecord("上举次数","20","次"),
        SportsRecord("扩胸次数","25","次"),
        SportsRecord("消耗卡路里","320","千卡"))
    val plankSportsDate = arrayListOf(
        SportsRecord("运动时长","12","分钟"),
        SportsRecord("消耗卡路里","120","千卡"))
    val calendarTitle = ObservableField("")
    val legSportsTime = ObservableField("10月23日 18:30")
    val dumbbellSportsTime = ObservableField("10月23日 18:30")
    val plankSportsTime = ObservableField("10月23日 18:30")


}