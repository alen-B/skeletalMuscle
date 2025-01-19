package com.fjp.skeletalmuscle.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
import com.fjp.skeletalmuscle.data.model.bean.result.ExportData
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class ExportReportViewModel : SMBaseViewModel() {

    var exportDataLiveData = MutableLiveData<ResultState<TodayDataResult>>()
    fun getExportData(time:String,endTime: String) {
        request({
            HttpRequestCoroutine.getTodayData(time,endTime)
        }, exportDataLiveData,true,"正在获取PDF数据...")

    }
}