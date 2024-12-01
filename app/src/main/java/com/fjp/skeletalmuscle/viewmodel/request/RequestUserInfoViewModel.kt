package com.fjp.skeletalmuscle.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.MultipartBody

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
class RequestUserInfoViewModel : SMBaseViewModel() {
    var avatar = MutableLiveData<ResultState<String>>()
    fun updateAvatar(body: MultipartBody.Part) {
        request({
            HttpRequestCoroutine.uploadImg(body)
        }, avatar, true)
    }
}
