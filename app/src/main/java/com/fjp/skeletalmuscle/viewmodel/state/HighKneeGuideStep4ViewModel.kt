package com.fjp.skeletalmuscle.viewmodel.state

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.clj.fastble.data.BleDevice
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.app.util.AppUtils
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.data.model.DeviceDetail
import com.fjp.skeletalmuscle.data.model.DeviceInfoRequest
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import com.fjp.skeletalmuscle.ui.deviceconnectguide.DeviceConnectGuideActivity
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class HighKneeGuideStep4ViewModel : SMBaseViewModel() {
    var saveLiveData = MutableLiveData<ResultState<String>>()
    fun saveDeivce(bleDevice: BleDevice) {
        val device = DeviceInfoRequest(
            DeviceConnectGuideActivity.address,
            DeviceConnectGuideActivity.city,
            DeviceConnectGuideActivity.district,
            Build.MODEL,
            SettingUtil.getDeviceId(appContext),
            DeviceConnectGuideActivity.province,
            if(AppUtils.isTablet(appContext)) "骨骼肌平板" else "骨骼肌手机" ,
            AppUtils.getAppVersionName(appContext),
            DeviceDetail(bleDevice.name,bleDevice.mac,"1.0")
        )
        request({
            HttpRequestCoroutine.saveDevice(device)
        }, saveLiveData, false)
    }
}