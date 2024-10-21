package com.fjp.skeletalmuscle.app.base

import androidx.databinding.ObservableField
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
open class SMBaseViewModel:BaseViewModel() {
    var title = ObservableField("")
    var showRightImg = ObservableField(false)
    var rightImg = ObservableField<Int>()
}