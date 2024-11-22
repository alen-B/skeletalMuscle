package com.fjp.skeletalmuscle.data.model.bean

/**
 *Author:Mr'x
 *Time:2024/11/22
 *Description:
 */
data class LoginResponse(
    val auth_token: String,
    val birthday: String,
    val created_at: Int,
    val disease: List<Disease>,
    val height: Int,
    val id: Int,
    val last_login_time: Int,
    val mobile: String,
    val name: String,
    val password: String,
    val sex: String,
    val sport_day_nums: Int,
    val sport_time: Int,
    val sports: List<String>,
    val status: Int,
    val updated_at: Int,
    val waistline: String,
    val weight: Int
)

data class Disease(
    val child_disease_name: List<String>,
    val disease_name: String
)