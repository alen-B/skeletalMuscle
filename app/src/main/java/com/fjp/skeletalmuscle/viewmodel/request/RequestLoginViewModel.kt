package com.fjp.skeletalmuscle.viewmodel.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.network.apiService
import com.fjp.skeletalmuscle.data.model.bean.LoginResponse
import kotlinx.coroutines.launch
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
    var loginResult = MutableLiveData<ResultState<LoginResponse>>()

    fun loginReq(mobile: String, code: String) {
        request({
            apiService.login(mobile, code)
        }, loginResult, true, appContext.getString(R.string.loading_login))

    }
}