package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.SaveAssessmentRequest
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class SportsAssessmentViewModel : SMBaseViewModel() {
    val curTime = ObservableField("00:00")
    val curProgress = ObservableField("0")
    val sportsNumber = ObservableField("")
    val leftLegAngle = ObservableField("")
    val rightLegAngle = ObservableField("")
    val leftLegCount = ObservableField("0")
    val rightLegCount = ObservableField("0")

    var sportsCalorieResult = MutableLiveData<ResultState<String>>()

    //保存测评数据
    fun saveAssessment(saveAssessmentRequest: SaveAssessmentRequest) {
        request({
            HttpRequestCoroutine.saveAssessment(saveAssessmentRequest)
        }, sportsCalorieResult, true)

    }
}