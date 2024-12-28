package com.fjp.skeletalmuscle.app.base

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
open class SMBaseViewModel : BaseViewModel() {
    var title = ObservableField("")
    var smAvatar = ObservableField(App.userInfo.profile)
    var showRightImg = ObservableField(false)
    var leftImg = ObservableField(R.drawable.title_left_default_icon)
    var rightImg = ObservableField<Int>()
    var showRightText = ObservableField(false)
    var showSetting = ObservableField(false)
    var showAssess = ObservableField(false)
    var curIndex = ObservableField("1")
    var totalIndex = ObservableField("9")
}