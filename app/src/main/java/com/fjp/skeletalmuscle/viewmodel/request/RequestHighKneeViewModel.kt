package com.fjp.skeletalmuscle.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.data.model.bean.LiftLegRequest
import com.fjp.skeletalmuscle.data.model.bean.result.SaveLiftLegResult
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/12/2
 *Description:
 */
class RequestHighKneeViewModel : BaseViewModel() {
    var liftLegLiveData = MutableLiveData<ResultState<SaveLiftLegResult>>()
    fun saveLiftLeg(liftLegRequest: LiftLegRequest) {
        request({
            HttpRequestCoroutine.saveLiftLeg(liftLegRequest)
        }, liftLegLiveData, true)

    }
}