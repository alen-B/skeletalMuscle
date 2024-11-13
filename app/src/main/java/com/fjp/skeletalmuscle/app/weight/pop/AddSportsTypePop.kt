package com.fjp.skeletalmuscle.app.weight.pop

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import com.fjp.skeletalmuscle.R
import com.lxj.xpopup.core.CenterPopupView

/**
 *Author:Mr'x
 *Time:2024/10/22
 *Description:
 */
class AddSportsTypePop(context: Context, val listener: InputPasswordListener) : CenterPopupView(context) {
    interface InputPasswordListener {
        fun input(name: String)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_add_sports
    }

    override fun onCreate() {
        super.onCreate()
        val sportsNameEt = findViewById<EditText>(R.id.sportsNameEt)
        findViewById<TextView>(R.id.sureTv).setOnClickListener {
            listener.input(sportsNameEt.text.toString())
            dismiss()
        }
    }
}