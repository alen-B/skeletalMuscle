package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.UserInfo

/**
 *Author:Mr'x
 *Time:2024/11/1
 *Description:
 */
class SettingViewModel:SMBaseViewModel() {
    val userInfo = ObservableField<UserInfo>()
}