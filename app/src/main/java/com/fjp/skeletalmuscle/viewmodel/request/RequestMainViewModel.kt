package com.fjp.skeletalmuscle.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
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
    var userInfoResult = MutableLiveData<ResultState<UserInfo>>()
    fun getTodayData(time:String) {
        request({
            HttpRequestCoroutine.getTodayData(time)
        }, mainLiveData)

    }
    fun getUserInfo() {
        request({
            HttpRequestCoroutine.getUserInfo()
        }, userInfoResult)

    }
}