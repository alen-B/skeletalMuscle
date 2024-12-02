package com.fjp.skeletalmuscle.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.LiftLegRequest
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/12/2
 *Description:
 */
class RequestMainViewModel : BaseViewModel() {
    var mainLiveData = MutableLiveData<ResultState<TodayDataResult>>()
    fun getTodayData() {
        request({
            HttpRequestCoroutine.getTodayData(DateTimeUtil.formatDate(System.currentTimeMillis(),DateTimeUtil.DATE_PATTERN))
        }, mainLiveData, true)

    }
}