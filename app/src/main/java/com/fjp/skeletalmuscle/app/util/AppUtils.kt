package com.fjp.skeletalmuscle.app.util

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration


/**
 *Author:Mr'x
 *Time:2024/12/7
 *Description:
 */
object AppUtils {

    fun getAppVersionName(context: Context): String {
        var versionName = ""
        try {
            // 获取PackageManager实例
            val packageManager = context.packageManager
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            versionName = packageInfo.versionName
            return versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    fun isTablet(context: Context): Boolean {
        return (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }
}