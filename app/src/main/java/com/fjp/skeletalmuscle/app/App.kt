package com.fjp.skeletalmuscle.app

//import cn.com.heaton.blelibrary.ble.Ble
//import cn.com.heaton.blelibrary.ble.BleLog
//import cn.com.heaton.blelibrary.ble.model.BleDevice
import com.fjp.skeletalmuscle.app.event.EventViewModel
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
import com.fjp.skeletalmuscle.data.model.bean.UserSports
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.BuildConfig
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.base.BaseApp
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.demo.app.event.AppViewModel
import me.hgj.jetpackmvvm.ext.util.jetpackMvvmLog


//Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
val appViewModel: AppViewModel by lazy { App.appViewModelInstance }

//Application全局的ViewModel，用于发送全局通知操作
val eventViewModel: EventViewModel by lazy { App.eventViewModelInstance }

class App : BaseApp() {

    companion object {
        lateinit var instance: App
        lateinit var eventViewModelInstance: EventViewModel
        lateinit var appViewModelInstance: AppViewModel
        var userInfo = UserInfo("", "", 0, arrayListOf(), 0, 0, 0, "", "", "", "", 0, 0, arrayListOf<UserSports>(), 0, 0, "", 0, "", "")
        var sportsType = SportsType.HIGH_KNEE
        var sportsTime = 2
        var upSportsTime = 1
        var expandSportsTime = 1
        var legAngle = 90
        var upliftAccount = 10
        var expandChestAccount = 10
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        instance = this
        eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)
        appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)

        // 初始化Bugly
        CrashReport.initCrashReport(applicationContext, "a87e844834", false)
        jetpackMvvmLog = BuildConfig.DEBUG


    }


}
