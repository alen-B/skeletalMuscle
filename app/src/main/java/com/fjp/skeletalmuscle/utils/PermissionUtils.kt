package com.fjp.skeletalmuscle.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/**
 *Author:Mr'x
 *Time:2024/11/4
 *Description:
 */
object PermissionUtils {
    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity, permission: Array<String?>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permission, requestCode)
    }

    fun verifyPermissions(grantResults: IntArray): Boolean {
        grantResults.forEach { item ->
            if (item != 0) {
                return false
            }
        }
        return true
    }
}
