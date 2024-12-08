package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.result.AssessmentHistoryResult
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/11/30
 *Description:
 */
class SportsAssessmentsHistoryViewModel : SMBaseViewModel() {
    val calendarTitle = ObservableField<String>("")

    var assessmentHistoryResult = MutableLiveData<ResultState<AssessmentHistoryResult>>()

    //获取测评数据
    fun getAssessment(year: String) {
        request({
            HttpRequestCoroutine.getAssessment(year)
        }, assessmentHistoryResult, true)

    }
}