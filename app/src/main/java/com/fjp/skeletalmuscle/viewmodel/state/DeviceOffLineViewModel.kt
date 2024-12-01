package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class DeviceOffLineViewModel : SMBaseViewModel() {
    val braceletTitle = ObservableField(appContext.getString(R.string.device_off_line_bracelet_connected))
    val leftKneeTitle = ObservableField(appContext.getString(R.string.device_off_line_left_knee_connected))
    val rightKneeTitle = ObservableField(appContext.getString(R.string.device_off_line_right_knee_connected))
}