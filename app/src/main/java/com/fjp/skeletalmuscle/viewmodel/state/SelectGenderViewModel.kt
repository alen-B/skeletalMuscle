package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class SelectGenderViewModel : SMBaseViewModel() {
    val sex = ObservableField<SEX>(SEX.MAN)


}

enum class SEX(val value:Int){
    WOMAN(0),MAN(1)
}