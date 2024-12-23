package com.fjp.skeletalmuscle.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.result.ExportData
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class ExportReportViewModel : SMBaseViewModel() {

    var exportLiveData = MutableLiveData<ResultState<ExportData>>()
    fun getTodayData(startTime: Long,endTime: Long) {
        request({
            HttpRequestCoroutine.getExportData(startTime,endTime)
        }, exportLiveData)

    }
}