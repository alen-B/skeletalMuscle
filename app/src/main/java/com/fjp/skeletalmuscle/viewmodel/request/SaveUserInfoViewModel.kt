package com.fjp.skeletalmuscle.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
class SaveUserInfoViewModel : SMBaseViewModel() {
    var saveResult = MutableLiveData<ResultState<String>>()


    fun saveInfoReq(userInfo: UserInfo) {
        request({
            HttpRequestCoroutine.saveUserInfo(userInfo)
        }, saveResult, true)
    }
}