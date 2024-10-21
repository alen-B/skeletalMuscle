package com.fjp.skeletalmuscle.app.weight.loadCallBack


import com.fjp.skeletalmuscle.R
import com.kingja.loadsir.callback.Callback


class EmptyCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }

}