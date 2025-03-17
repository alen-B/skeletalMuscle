package com.fjp.skeletalmuscle.data.model.bean

import androidx.databinding.BaseObservable

/**
 *Author:Mr'x
 *Time:2024/11/22
 *Description:
 */
data class UserInfo(var auth_token: String, var birthday: String, var created_at: Int, var disease: List<Disease>, var height: Int, var id: Int, var last_login_time: Int, var mobile: String, var name: String, var password: String, var sex: String, var sport_day_nums: Int, var sport_time: Int, var sports: List<UserSports>, var status: Int, var updated_at: Int, var waistline: String, var weight: Int, var profile: String, var device_no: String = "", var latitude: String = "", var longitude: String = "", var address: String = "", var province: String = "", var city: String = "", var district: String = "") : BaseObservable()

data class Disease(var child_disease_name: List<String>, var disease_name: String)
data class UserSports(var child_sport_name: List<String>, var sport_name: String)