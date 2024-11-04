package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class HighKneeViewModel:SMBaseViewModel() {
    val curTime = ObservableField("00:00")
    val curProgress = ObservableField("0")
    val heartRate = ObservableField("")
    val maxTime = 1000
}