package com.fjp.skeletalmuscle.app.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import me.hgj.jetpackmvvm.base.appContext


/**
 *Author:Mr'x
 *Time:2024/11/20
 *Description:
 */
/**
 * 软键盘工具类
 */
object KeyboardUtils {
    /**
     * 显示软键盘
     *
     * @param editText
     */
    fun showSoftInput(editText: EditText?) {
        if (editText == null) return
        val imm = appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        imm.showSoftInput(editText, 0)
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    fun hideSoftInput(view: View?) {
        if (view == null) return
        val inputMethodManager = appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 隐藏软键盘
     *
     * @param editText
     */
    fun hideSoftInput(editText: EditText?) {
        if (editText == null) return
        val inputMethodManager = appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
        editText.clearFocus()
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    fun hideSoftInput(activity: Activity) {
        val window = activity.window
        var view = window.currentFocus
        if (view == null) {
            view = window.decorView
        }
        hideSoftInput(view)
    }

    /**
     * 软键盘切换
     */
    fun toggleSoftInput() {
        val imm = appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
        imm.toggleSoftInput(0, 0)
    }
}
