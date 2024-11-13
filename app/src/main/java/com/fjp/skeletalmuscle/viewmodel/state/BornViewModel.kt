package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class BornViewModel : SMBaseViewModel() {
    val born = ObservableField("")
    val year = ObservableField("1978")
    val month = ObservableField("08")
    val day = ObservableField("08")

}