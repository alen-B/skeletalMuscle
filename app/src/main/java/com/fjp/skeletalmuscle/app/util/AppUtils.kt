package com.fjp.skeletalmuscle.app.util

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap


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

fun <T> debounce(
    delay: Long,
    coroutineScope: CoroutineScope = GlobalScope,
    action: (T) -> Unit
): (T) -> Unit {
    val debounceMap = ConcurrentHashMap<Int, Job>()
    return { param: T ->
        val currentKey = param.hashCode()
        debounceMap[currentKey]?.cancel()
        debounceMap[currentKey] = coroutineScope.launch {
            delay(delay)
            action(param)
            debounceMap.remove(currentKey)
        }
    }
}