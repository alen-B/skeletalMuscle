package com.fjp.skeletalmuscle.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.data.model.bean.result.HeartRateResult
import com.fjp.skeletalmuscle.data.model.bean.result.LiftLegTrendResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendCalorieResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendDumbbellExpandChestAndUpResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendLiftLegSportTimeResult
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Author:Mr'x
 *Time:2024/12/4
 *Description:
 */
class RequestTodaySportsDetailFragmentViewModel : BaseViewModel() {
    var sportsCalorieResult = MutableLiveData<ResultState<SportTrendCalorieResult>>()
    var heartRateResult = MutableLiveData<ResultState<HeartRateResult>>()
    var liftLegTrendResult = MutableLiveData<ResultState<LiftLegTrendResult>>()
    var strongTimeResult = MutableLiveData<ResultState<SportTrendLiftLegSportTimeResult>>()
    var dumbbellExpandChestResult = MutableLiveData<ResultState<SportTrendDumbbellExpandChestAndUpResult>>()
    var dumbbellUpResult = MutableLiveData<ResultState<SportTrendDumbbellExpandChestAndUpResult>>()

    //所有运动的卡路里
    fun getSportTrendCalorie(type: String) {
        request({
            HttpRequestCoroutine.getSportTrendCalorie(type)
        }, sportsCalorieResult, true)

    }

    //高抬腿卡路里
    fun getLiftLegCalorie(type: String) {
        request({
            HttpRequestCoroutine.getLiftLegCalorie(type)
        }, sportsCalorieResult, true)

    }

    //哑铃的卡路里
    fun getSportTrendDumbbellCalorie(type: String) {
        request({
            HttpRequestCoroutine.getSportTrendDumbbellCalorie(type)
        }, sportsCalorieResult, true)

    }

    //平板支撑的卡路里
    fun getSportTrendFlatSupportCalorie(type: String) {
        request({
            HttpRequestCoroutine.getSportTrendFlatSupportCalorie(type)
        }, sportsCalorieResult, true)

    }


    //高抬腿-心率趋势图
    fun getLiftLegHeartRate(type: String) {
        request({
            HttpRequestCoroutine.getLiftLegHeartRate(type)
        }, heartRateResult, true)

    }

    //哑铃-心率趋势图
    fun getSportTrendDumbbellHeartRate(type: String) {
        request({
            HttpRequestCoroutine.getSportTrendDumbbellHeartRate(type)
        }, heartRateResult, true)

    }

    //平板支-心率趋势图
    fun getSportTrendFlatSupportHeartRate(type: String) {
        request({
            HttpRequestCoroutine.getSportTrendFlatSupportHeartRate(type)
        }, heartRateResult, true)

    }


    // 高抬腿-抬腿趋势图
    fun getSportTrendLiftLeg(type: String) {
        request({
            HttpRequestCoroutine.getSportTrendLiftLeg(type)
        }, liftLegTrendResult, true)

    }

    // 高抬腿-强度和时间
    fun getSportTrendLiftLegSportTime(type: String) {
        request({
            HttpRequestCoroutine.getSportTrendLiftLegSportTime(type)
        }, strongTimeResult, true)

    }

    // 哑铃-上举运动趋势图
    fun getSportTrendDumbbellUp(type: String) {
        request({
            HttpRequestCoroutine.getSportTrendDumbbellUp(type)
        }, dumbbellUpResult, true)

    }
    // 哑铃-扩胸运动趋势图
    fun getSportTrendDumbbellExpandChest(type: String) {
        request({
            HttpRequestCoroutine.getSportTrendDumbbellExpandChest(type)
        }, dumbbellExpandChestResult, true)

    }
}