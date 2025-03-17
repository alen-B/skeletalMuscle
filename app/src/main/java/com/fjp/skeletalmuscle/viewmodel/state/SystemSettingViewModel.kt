package com.fjp.skeletalmuscle.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.result.VersionData
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.requestNoCheck

class SystemSettingViewModel : SMBaseViewModel() {
    var appVersion = MutableLiveData<VersionData>()

    fun checkVersion() {
        requestNoCheck({ HttpRequestCoroutine.checkVersion() }, {
            //请求成功 自己拿到数据做业务需求操作
            if (it.code == 200) {
                //结果正确
                appVersion.value = it.data
            }
        }, {

        })
    }
}