package com.fjp.skeletalmuscle.app.util

import android.content.Context
import android.content.pm.PackageManager


/**
 *Author:Mr'x
 *Time:2024/12/7
 *Description:
 */
object  AppUtils {

    fun getAppVersionName(context: Context): String {
        var versionName =""
        try {
            // 获取PackageManager实例
            val packageManager = context.packageManager
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }
}