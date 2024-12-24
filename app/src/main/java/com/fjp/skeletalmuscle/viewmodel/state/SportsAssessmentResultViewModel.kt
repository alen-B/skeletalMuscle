package com.fjp.skeletalmuscle.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.SaveAssessmentRequest
import com.fjp.skeletalmuscle.data.model.bean.result.AssessmentHistoryData
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/11/28
 *Description:
 */
class SportsAssessmentResultViewModel : SMBaseViewModel() {
    var sportsCalorieResult = MutableLiveData<ResultState<AssessmentHistoryData?>>()

    //获取最后一次测评数据
    fun getLastTest() {
        request({
            HttpRequestCoroutine.getLatestTest()
        }, sportsCalorieResult)

    }
}