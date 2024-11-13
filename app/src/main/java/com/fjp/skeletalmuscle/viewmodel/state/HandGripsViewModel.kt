package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/28
 *Description:
 */
class HandGripsViewModel : SMBaseViewModel() {
    val nextButtonIsEnable = ObservableField(true)
    val nextButtonText = ObservableField(appContext.getString(R.string.next_step))
}