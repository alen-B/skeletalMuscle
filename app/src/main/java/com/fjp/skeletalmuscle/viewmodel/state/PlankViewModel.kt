package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.FlatSupportRequest
import com.fjp.skeletalmuscle.data.model.bean.result.SavePlankResult
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class PlankViewModel : SMBaseViewModel() {
    val curTime = ObservableField("00:00")
    val curProgress = ObservableField("0")
    val heartRate = ObservableField("")
    val leftLegAngle = ObservableField("")
    val rightLegAngle = ObservableField("")
    val leftLegCount = ObservableField("0")
    val rightLegCount = ObservableField("0")
    val maxTime = App.sportsTime//单位分钟

    var plankLiveData = MutableLiveData<ResultState<SavePlankResult>>()
    fun saveflatSupport(request: FlatSupportRequest) {
        request({
            HttpRequestCoroutine.saveflatSupport(request)
        }, plankLiveData, true)

    }
}