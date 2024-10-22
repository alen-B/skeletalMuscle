package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class LoginViewModel : SMBaseViewModel() {
    val phone = ObservableField("")
    val verificationCode = ObservableField("")
    val verificationCodeisEnabled = ObservableField(true)
    val verificationCodeText = ObservableField(appContext.getString(R.string.login_get_verification_code))
    val agreement = ObservableField(false)

}