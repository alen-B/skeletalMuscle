package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.UpdateVersion
import com.fjp.skeletalmuscle.data.model.bean.result.AppVersion
import com.fjp.skeletalmuscle.data.model.bean.result.VersionData
import com.fjp.skeletalmuscle.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.ext.requestNoCheck

/**
 *Author:Mr'x
 *Time:2024/11/20
 *Description:
 */
class UpdateVersionViewModel : SMBaseViewModel() {
    val versionTitle = ObservableField<String>()
    val content = ObservableField<String>("")


}