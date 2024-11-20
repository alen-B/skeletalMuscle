package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.UpdateVersion

/**
 *Author:Mr'x
 *Time:2024/11/20
 *Description:
 */
class UpdateVersionViewModel:SMBaseViewModel() {
    val updateVersion= ObservableField<UpdateVersion>()
    val size= ObservableField<String>()
    init {
        updateVersion.set(UpdateVersion("骨骼肌V2.5",10,"1.优化首页\n 2.修复了部分bug\n 3.解决了用户打开骨骼肌产品闪退问题"))
        size.set("400MB")
    }

}