package com.fjp.skeletalmuscle.app.weight.loadCallBack

import com.fjp.skeletalmuscle.R
import com.kingja.loadsir.callback.Callback


class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }

}