package com.fjp.skeletalmuscle.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.ext.requestNoCheck
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.MultipartBody

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
class RequestUserInfoViewModel : SMBaseViewModel() {
    var avatar = MutableLiveData<String>()
    var updateImageFailed = MutableLiveData<Boolean>()
    var saveResult = MutableLiveData<ResultState<String>>()
    fun updateAvatar(body: MultipartBody.Part) {
        requestNoCheck({ HttpRequestCoroutine.uploadImg(body) }, {
            //请求成功 自己拿到数据做业务需求操作
            if (it.status == "success") {
                //结果正确
                avatar.value = it.data
            } else {
                updateImageFailed.value = true
            }
        }, {
            updateImageFailed.value = true
        }, true, "正在上传头像...")
    }

    fun saveInfoReq(userInfo: UserInfo) {
        request({
            HttpRequestCoroutine.saveUserInfo(userInfo)
        }, saveResult, true)
    }
}
