package com.fjp.skeletalmuscle.app.event

import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData

/**
 * 作者　: hegaojian
 * 时间　: 2019/5/2
 * 描述　:APP全局的ViewModel，可以在这里发送全局通知替代EventBus，LiveDataBus等
 */
class EventViewModel : BaseViewModel() {


    //添加TODO通知
    val todoEvent = EventLiveData<Boolean>()

    //开始运动，关闭之前的选择运动和运动数据设置activity
    val  startSports= EventLiveData<Boolean>()

    val  updatePhoneEvent= EventLiveData<String>()
    val  updateUserNameEvent= EventLiveData<String>()

}