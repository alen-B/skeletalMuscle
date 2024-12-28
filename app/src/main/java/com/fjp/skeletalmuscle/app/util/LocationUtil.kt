package com.fjp.skeletalmuscle.app.util

import android.content.Context
import android.location.LocationManager


/**
 *Author:Mr'x
 *Time:2024/12/28
 *Description:
 */
object  LocationUtil {
    fun isLocServiceEnable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }

}