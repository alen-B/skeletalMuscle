package com.fjp.skeletalmuscle.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.result.AppVersion
import com.fjp.skeletalmuscle.data.model.bean.result.VersionData
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.requestNoCheck

class SystemSettingViewModel : SMBaseViewModel() {
    var appVersion = MutableLiveData<VersionData>()

    fun checkVersion() {
        requestNoCheck({ HttpRequestCoroutine.checkVersion() }, {
            //请求成功 自己拿到数据做业务需求操作
            if (it.status == "success") {
                //结果正确
                appVersion.value = VersionData("骨骼肌", arrayOf("1.增加支付功能","2.增加支付功能sdfasdf","3.增加支付功能sdfasdf"),"https://apk.poi234.com/shouyou/apk/shtszzioshfyyzy1843/12/shtszzioshfyyzy1843_12_1.apk",2,"1.0.3")
            }
        }, {

        })
    }
}