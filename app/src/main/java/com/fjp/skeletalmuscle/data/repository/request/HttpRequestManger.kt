package com.fjp.skeletalmuscle.data.repository.request

import com.fjp.skeletalmuscle.app.network.apiService
import com.fjp.skeletalmuscle.data.model.bean.ApiResponse
import com.fjp.skeletalmuscle.data.model.bean.DumbbellRequest
import com.fjp.skeletalmuscle.data.model.bean.FlatSupportRequest
import com.fjp.skeletalmuscle.data.model.bean.LiftLegRequest
import com.fjp.skeletalmuscle.data.model.bean.SaveAssessmentRequest
import com.fjp.skeletalmuscle.data.model.bean.SportTrendDumbbellUp
import com.fjp.skeletalmuscle.data.model.bean.SportTrendDummbbellCalorie
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
import com.fjp.skeletalmuscle.data.model.bean.result.LiftLegCalorieResult
import com.fjp.skeletalmuscle.data.model.bean.result.LiftLegHeartRateResult
import com.fjp.skeletalmuscle.data.model.bean.result.LiftLegTrendResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendCalorieResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendDumbbellExpandChestResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendDumbbellHeartRateResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendFlatSupportCalorieResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendFlatSupportHeartRateResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendLiftLegSportTimeResult
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * 作者　: hegaojian
 * 时间　: 2020/5/4
 * 描述　: 处理协程的请求类
 */

val HttpRequestCoroutine: HttpRequestManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpRequestManger()
}

class HttpRequestManger {

    /**
     * 注册并登陆
     */
    suspend fun login(mobile: String, code: String): ApiResponse<UserInfo> {
        return apiService.login(mobile, code)
    }


    /**
     * 获取验证码
     */
    suspend fun getCode(mobile: String): ApiResponse<String> {
        return apiService.getCode(mobile)
    }
    /**
     * 保存个人信息
     */
    suspend fun saveUserInfo(userInfo: UserInfo): ApiResponse<String> {
        return apiService.saveInfo(userInfo)
    }
    /**
     * 修改头像
     */
    suspend fun uploadImg(body: MultipartBody.Part): ApiResponse<String> {
        return apiService.uploadImg(body)
    }
    /**
     * 保存测评细信息
     *
     */
    suspend fun saveAssessment(saveAssessment: SaveAssessmentRequest): ApiResponse<String>{
        return apiService.saveAssessment(saveAssessment)
    }

    /**
     * 获取测评结果
     *
     */
    suspend fun getAssessment(year: String): ApiResponse<String>{
        return apiService.getAssessment(year)
    }

    /**
     * 高抬腿
     *
     */
    suspend fun saveLiftLeg( liftLegRequest: LiftLegRequest): ApiResponse<String>{
        return apiService.saveLiftLeg(liftLegRequest)
    }

    /**
     * 哑铃
     *
     */
    suspend fun saveDumbbell( dumbbellRequest: DumbbellRequest): ApiResponse<String>{
        return apiService.saveDumbbell(dumbbellRequest)
    }

    /**
     * 平板支撑
     *
     */
    suspend fun saveflatSupport( flatSupportRequest: FlatSupportRequest): ApiResponse<String>{
        return apiService.saveflatSupport(flatSupportRequest)
    }

    /**
     * 今日运动所有数据获取
     *time="2024-11-01"
     */
    suspend fun getTodayData(time: String): ApiResponse<TodayDataResult>{
        return apiService.getTodayData(time)
    }

    /**
     * 高抬腿-卡路里趋势图
     *
     */
    suspend fun getLiftLegCalorie( type: String): ApiResponse<LiftLegCalorieResult>{
        return apiService.getLiftLegCalorie(type)
    }

    /**
     * 高抬腿-心率趋势图
     *
     */
    suspend fun getLiftLegHeartRate( type: String): ApiResponse<LiftLegHeartRateResult>{
        return apiService.getLiftLegHeartRate(type)
    }

    /**
     * 高抬腿-抬腿趋势图
     *
     */
    suspend fun getSportTrendLiftLeg(type: String): ApiResponse<LiftLegTrendResult>{
        return apiService.getSportTrendLiftLeg(type)
    }

    /**
     *高抬腿-强度和时间
     *
     */
    suspend fun getSportTrendLiftLegSportTime(type: String): ApiResponse<SportTrendLiftLegSportTimeResult>{
        return apiService.getSportTrendLiftLegSportTime(type)
    }



    /**
     *卡路里趋势图
     *
     */
    suspend fun getSportTrendCalorie(type: String): ApiResponse<SportTrendCalorieResult>{
        return apiService.getSportTrendCalorie(type)
    }


    /**
     *哑铃-卡路里趋势图
     *
     */
    suspend fun getSportTrendDumbbellCalorie(type: String): ApiResponse<SportTrendDummbbellCalorie>{
        return apiService.getSportTrendDumbbellCalorie(type)
    }




    /**
     *哑铃-心率趋势图
     *
     */
    suspend fun getSportTrendDumbbellHeartRate(type: String): ApiResponse<SportTrendDumbbellHeartRateResult>{
        return apiService.getSportTrendDumbbellHeartRate(type)
    }




    /**
     *哑铃-上举运动趋势图
     *
     */
    suspend fun getSportTrendDumbbellUp(type: String): ApiResponse<SportTrendDumbbellUp>{
        return apiService.getSportTrendDumbbellUp(type)
    }





    /**
     *哑铃-扩胸运动趋势图
     *
     */
    suspend fun getSportTrendDumbbellExpandChest(type: String): ApiResponse<SportTrendDumbbellExpandChestResult>{
        return apiService.getSportTrendDumbbellExpandChest(type)
    }


    /**
     *平板支撑-卡路里趋势图
     *
     */
    suspend fun getSportTrendFlatSupportCalorie(type: String): ApiResponse<SportTrendFlatSupportCalorieResult>{
        return apiService.getSportTrendFlatSupportCalorie(type)
    }



    /**
     *平板支撑-心率趋势图
     *
     */
    suspend fun getSportTrendFlatSupportHeartRate(type: String): ApiResponse<SportTrendFlatSupportHeartRateResult>{
        return apiService.getSportTrendFlatSupportHeartRate(type)
    }



}