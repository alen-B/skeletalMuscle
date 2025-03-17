package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

/**
 *Author:Mr'x
 *Time:2024/11/20
 *Description:
 */
class UpdateVersionViewModel : SMBaseViewModel() {
    val versionTitle = ObservableField<String>()
    val content = ObservableField<String>("")


}