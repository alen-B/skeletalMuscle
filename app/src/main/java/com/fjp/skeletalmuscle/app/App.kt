package com.fjp.skeletalmuscle.app

import androidx.databinding.ObservableField
import cn.com.heaton.blelibrary.ble.Ble
import cn.com.heaton.blelibrary.ble.BleLog
import cn.com.heaton.blelibrary.ble.model.BleDevice
import com.fjp.skeletalmuscle.app.event.EventViewModel
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
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
        var userInfo = UserInfo("", "", 0, arrayListOf(), 0, 0, 0, "", "", "", "", 0, 0, arrayListOf(), 0, 0, "", 0,"")
        var sportsType = SportsType.HIGH_KNEE.type
        var sportsTime = 8
        var legAngle = 90
        lateinit var mBle: Ble<BleDevice>
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
        initBle()


    }

    private fun initBle() {
        mBle = Ble.options()//开启配置
            .setLogBleEnable(true)//设置是否输出打印蓝牙日志（非正式打包请设置为true，以便于调试）
            .setThrowBleException(true)//设置是否抛出蓝牙异常 （默认true）
            .setAutoConnect(true)//设置是否自动连接 （默认false）
            .setIgnoreRepeat(false)//设置是否过滤扫描到的设备(已扫描到的不会再次扫描)
            .setConnectTimeout(25 * 1000)//设置连接超时时长（默认10*1000 ms）
            .setMaxConnectNum(7)//最大连接数量
            .setScanPeriod(20 * 1000)//设置扫描时长（默认10*1000 ms）
            .create(appContext, object : Ble.InitCallback {
                override fun success() {
                    BleLog.d("application", "初始化成功")
                }

                override fun failed(failedCode: Int) {
                    BleLog.d("application", "初始化失败")
                }

            })
    }

}
