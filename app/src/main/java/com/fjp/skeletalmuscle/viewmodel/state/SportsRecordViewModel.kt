package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.SportsRecord


class SportsRecordViewModel : SMBaseViewModel() {
    val legSportsDate = arrayListOf(SportsRecord("运动时长", "0", "分钟"), SportsRecord("消耗卡路里", "0", "千卡"), SportsRecord("运动量", "0", "°"), SportsRecord("抬腿平均角度", "0", "°"), SportsRecord("心肺耐力", "0", ""))
    var dumbbellSportsDate = arrayListOf(SportsRecord("运动时长", "0", "分钟"), SportsRecord("哑铃重量", "0", "kg"), SportsRecord("上举次数", "0", "次"), SportsRecord("扩胸次数", "0", "次"), SportsRecord("消耗卡路里", "0", "千卡"))
    var plankSportsDate = arrayListOf(SportsRecord("运动时长", "0", "分钟"), SportsRecord("消耗卡路里", "0", "千卡"))
    val calendarTitle = ObservableField("")
    val legSportsTime = ObservableField("")
    val dumbbellSportsTime = ObservableField("")
    val plankSportsTime = ObservableField("")


}