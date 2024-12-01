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
class SelectGenderViewModel : SMBaseViewModel() {
    val sex = ObservableField(appContext.getString(R.string.setting_sex_man))


}
