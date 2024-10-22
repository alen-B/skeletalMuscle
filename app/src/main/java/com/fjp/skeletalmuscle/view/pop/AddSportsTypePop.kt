package com.fjp.skeletalmuscle.view.pop

import android.content.Context
import android.os.Build
import android.text.InputType
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.TextView
import com.fjp.skeletalmuscle.R
import com.lxj.xpopup.core.CenterPopupView
import me.hgj.jetpackmvvm.base.appContext

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