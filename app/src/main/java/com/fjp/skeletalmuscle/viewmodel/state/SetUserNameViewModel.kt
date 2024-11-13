package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

class SetUserNameViewModel : SMBaseViewModel() {
    val userName = ObservableField("")
}