package com.fjp.skeletalmuscle.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.SaveAssessmentRequest
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/11/28
 *Description:
 */
class SportsAssessmentResultViewModel : SMBaseViewModel() {
    var sportsCalorieResult = MutableLiveData<ResultState<String>>()

    //获取测评数据
    fun getAssessment(year: String) {
//        request({
//            HttpRequestCoroutine.getAssessment(year)
//        }, sportsCalorieResult, true)

    }
}