package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.result.CalendarResult
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/10/23
 *Description:
 */
class CalendarViewModel : SMBaseViewModel() {
    val calendarTitle = ObservableField("")
    var response = MutableLiveData<ResultState<ArrayList<CalendarResult>>>()

    fun calendar(month: String) {
        request({
            HttpRequestCoroutine.calendar(month)
        }, response)

    }


}