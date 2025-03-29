package com.fjp.skeletalmuscle.app.network

import com.fjp.skeletalmuscle.data.model.DeviceInfoRequest
import com.fjp.skeletalmuscle.data.model.bean.ApiResponse
import com.fjp.skeletalmuscle.data.model.bean.DumbbellRequest
import com.fjp.skeletalmuscle.data.model.bean.FlatSupportRequest
import com.fjp.skeletalmuscle.data.model.bean.LiftLegRequest
import com.fjp.skeletalmuscle.data.model.bean.SaveAssessmentRequest
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
import com.fjp.skeletalmuscle.data.model.bean.result.AppVersion
import com.fjp.skeletalmuscle.data.model.bean.result.AssessmentHistoryData
import com.fjp.skeletalmuscle.data.model.bean.result.CalendarResult
import com.fjp.skeletalmuscle.data.model.bean.result.ExportData
import com.fjp.skeletalmuscle.data.model.bean.result.HeartRateResult
import com.fjp.skeletalmuscle.data.model.bean.result.LiftLegTrendResult
import com.fjp.skeletalmuscle.data.model.bean.result.SaveDumbbellResult
import com.fjp.skeletalmuscle.data.model.bean.result.SaveLiftLegResult
import com.fjp.skeletalmuscle.data.model.bean.result.SavePlankResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendCalorieResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendDumbbellExpandChestAndUpResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendLiftLegSportTimeResult
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.data.model.bean.result.UpdateImageResult
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 网络API
 */
interface ApiService {

    companion object {
        const val SERVER_URL = "http://8.141.25.141:8003/"
        const val SERVER_URL1 = "https://wanandroid.com/"
    }

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("mobile") mobile: String, @Field("code") code: String): ApiResponse<UserInfo>

    /**
     * 获取个人信息
     */
    @GET("/user/get_info")
    suspend fun getUserInfo(): ApiResponse<UserInfo>

    /**
     * 获取验证码
     */
    @FormUrlEncoded
    @POST("get_code")
    suspend fun getCode(@Field("mobile") mobile: String): ApiResponse<String>

    /**
     * 保存用户信息
     */
    @POST("user/save_info")
    suspend fun saveInfo(@Body userInfo: UserInfo): ApiResponse<String>

    /**
     * 保存测评细信息
     *
     */
    @POST("assess/save")
    suspend fun saveAssessment(@Body saveAssessment: SaveAssessmentRequest): ApiResponse<String>

    /**
     * 获取测评结果
     *
     */
    @GET("assess/get_test")
    suspend fun getAssessment(@Query("year") year: String): ApiResponse<ArrayList<AssessmentHistoryData>>

    /**
     * 高抬腿
     *
     */
    @POST("sport/lift_leg")
    suspend fun saveLiftLeg(@Body liftLegRequest: LiftLegRequest): ApiResponse<SaveLiftLegResult>

    /**
     * 哑铃
     *
     */
    @POST("sport/dumbbell")
    suspend fun saveDumbbell(@Body dumbbellRequest: DumbbellRequest): ApiResponse<SaveDumbbellResult>
    /**
     * 保存设备信息
     *
     */
    @POST("save_device")
    suspend fun saveDevice(@Body deviceinfo: DeviceInfoRequest): ApiResponse<String>

    /**
     * 平板支撑
     *
     */
    @POST("sport/flat_support")
    suspend fun saveflatSupport(@Body flatSupportRequest: FlatSupportRequest): ApiResponse<SavePlankResult>

    /**
     * 今日运动所有数据获取
     *time="2024-11-01"
     */
    @GET("sport/get_today_data")
    suspend fun getTodayData(@Query("time") time: String, @Query("end_time") endTime: String): ApiResponse<TodayDataResult>

    /**
     * 高抬腿-卡路里趋势图
     *
     */
    @GET("sport_trend/lift_leg_calorie")
    suspend fun getLiftLegCalorie(@Query("type") type: String): ApiResponse<SportTrendCalorieResult>

    /**
     * 高抬腿-心率趋势图
     *
     */
    @GET("sport_trend/lift_leg_heart_rate")
    suspend fun getLiftLegHeartRate(@Query("type") type: String): ApiResponse<HeartRateResult>

    /**
     * 高抬腿-抬腿趋势图
     *
     */
    @GET("sport_trend/lift_leg")
    suspend fun getSportTrendLiftLeg(@Query("type") type: String): ApiResponse<LiftLegTrendResult>

    /**
     *高抬腿-强度和时间
     *
     */
    @GET("sport_trend/lift_leg_sport_time")
    suspend fun getSportTrendLiftLegSportTime(@Query("type") type: String): ApiResponse<SportTrendLiftLegSportTimeResult>


    /**
     *卡路里趋势图
     *
     */
    @GET("sport_trend/calorie")
    suspend fun getSportTrendCalorie(@Query("type") type: String): ApiResponse<SportTrendCalorieResult>


    /**
     *哑铃-卡路里趋势图
     *
     */
    @GET("sport_trend/dumbbell_calorie")
    suspend fun getSportTrendDumbbellCalorie(@Query("type") type: String): ApiResponse<SportTrendCalorieResult>


    /**
     *哑铃-心率趋势图
     *
     */
    @GET("sport_trend/dumbbell_heart_rate")
    suspend fun getSportTrendDumbbellHeartRate(@Query("type") type: String): ApiResponse<HeartRateResult>


    /**
     *哑铃-上举运动趋势图
     *
     */
    @GET("sport_trend/dumbbell_up")
    suspend fun getSportTrendDumbbellUp(@Query("type") type: String): ApiResponse<SportTrendDumbbellExpandChestAndUpResult>


    /**
     *哑铃-扩胸运动趋势图
     *
     */
    @GET("sport_trend/dumbbell_expand_chest")
    suspend fun getSportTrendDumbbellExpandChest(@Query("type") type: String): ApiResponse<SportTrendDumbbellExpandChestAndUpResult>


    /**
     *平板支撑-卡路里趋势图
     *
     */
    @GET("sport_trend/flat_support_calorie")
    suspend fun getSportTrendFlatSupportCalorie(@Query("type") type: String): ApiResponse<SportTrendCalorieResult>


    /**
     *平板支撑-心率趋势图
     *
     */
    @GET("sport_trend/flat_support_heart_rate")
    suspend fun getSportTrendFlatSupportHeartRate(@Query("type") type: String): ApiResponse<HeartRateResult>

    /**
     *日历
     *
     */
    @GET("sport_trend/calendar")
    suspend fun calendar(@Query("month") month: String): ApiResponse<ArrayList<CalendarResult>>

    /**
     *获取最后一次的测评结果
     *
     */
    @GET("assess/get_latest_test")
    suspend fun getLatestTest(): ApiResponse<AssessmentHistoryData?>

    /**
     *数据导出
     *
     */
    @GET("/sport/get_data")
    suspend fun getExportData(@Query("start_time") startTime: Long, @Query("end_time") endTime: Long): ApiResponse<ExportData>


    @Multipart
    @POST("http://8.141.25.141:8000/upload")
    suspend fun uploadImg(@Part body: MultipartBody.Part): UpdateImageResult

    @GET("http://8.141.25.141:8003/check/version/gugeji")
    suspend fun checkVersion(): AppVersion


}