package com.fjp.skeletalmuscle.data.repository.request

import com.fjp.skeletalmuscle.app.network.apiService
import com.fjp.skeletalmuscle.data.model.bean.ApiResponse
import com.fjp.skeletalmuscle.data.model.bean.LoginResponse
import com.fjp.skeletalmuscle.data.model.bean.UserInfo

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
     * 获取首页文章数据
     */
//    suspend fun getHomeData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
//        //同时异步请求2个接口，请求完成后合并数据
//        return withContext(Dispatchers.IO) {
//            val listData = async { apiService.getAritrilList(pageNo) }
//            //如果App配置打开了首页请求置顶文章，且是第一页
//            if (CacheUtil.isNeedTop() && pageNo == 0) {
//                val topData = async { apiService.getTopAritrilList() }
//                listData.await().data.datas.addAll(0, topData.await().data)
//                listData.await()
//            } else {
//                listData.await()
//            }
//        }
//    }
//
    /**
     * 注册并登陆
     */
    suspend fun login(mobile: String, code: String): ApiResponse<LoginResponse> {
            return apiService.login(mobile, code)
    }
//
//    /**
//     * 获取项目标题数据
//     */
//    suspend fun getProjectData(
//        pageNo: Int,
//        cid: Int = 0,
//        isNew: Boolean = false
//    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
//        return if (isNew) {
//            apiService.getProjecNewData(pageNo)
//        } else {
//            apiService.getProjecDataByType(pageNo, cid)
//        }
//    }
}