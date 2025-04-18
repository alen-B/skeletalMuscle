package com.fjp.skeletalmuscle.app.network

import android.content.Intent
import android.util.Log
import com.fjp.skeletalmuscle.data.model.bean.ApiResponse
import com.fjp.skeletalmuscle.ui.login.LoginActivity
import com.google.gson.Gson
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.util.toJson
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * 作者　: hegaojian
 * 时间　: 2022/1/13
 * 描述　: token过期拦截器
 */
class TokenOutInterceptor : Interceptor {

    val gson: Gson by lazy { Gson() }

    @kotlin.jvm.Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("===request:", chain.request().toJson())
        val response = chain.proceed(chain.request())
        return if (response.body() != null && response.body()!!.contentType() != null) {
            val mediaType = response.body()!!.contentType()
            val string = response.body()!!.string()
            Log.d("===response:", string)
            val responseBody = ResponseBody.create(mediaType, string)
            try {
                val apiResponse = gson.fromJson(string, ApiResponse::class.java)
                //判断逻辑 模拟一下
                if (apiResponse.code == 400) {
                    //如果是普通的activity话 可以直接跳转，如果是navigation中的fragment，可以发送通知跳转
                    appContext.startActivity(Intent(appContext, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    })

                }
            } catch (e: Exception) {
                println("Gson 解析异常")
            }

            response.newBuilder().body(responseBody).build()
        } else {
            response
        }
    }
}