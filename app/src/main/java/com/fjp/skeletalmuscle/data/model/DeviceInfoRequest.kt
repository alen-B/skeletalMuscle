package com.fjp.skeletalmuscle.data.model

/**
 *Author:Mr'x
 *Time:2025/3/29
 *Description:
 */
data class DeviceInfoRequest(var address: String, var city: String, var district: String, var name: String, var no: String, var province: String, var type: String, var version: String, var device_detail: DeviceDetail)

data class DeviceDetail(var name: String, var no: String, var version: String)