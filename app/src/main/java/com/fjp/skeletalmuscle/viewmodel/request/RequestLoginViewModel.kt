package com.fjp.skeletalmuscle.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/11/22
 *Description:
 */
class RequestLoginViewModel : BaseViewModel() {
    var loginResult = MutableLiveData<ResultState<UserInfo>>()
    var code = MutableLiveData<ResultState<String>>()

    fun loginReq(mobile: String, code: String) {
        request({
            HttpRequestCoroutine.login(mobile, code)
        }, loginResult, true, appContext.getString(R.string.loading_login))

    }

    fun codeReq(mobile: String) {
        request({
            HttpRequestCoroutine.getCode(mobile)
        }, code, true)

    }
}