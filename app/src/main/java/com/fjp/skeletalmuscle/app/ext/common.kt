package com.fjp.skeletalmuscle.app.ext

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.target.Target
import com.fjp.skeletalmuscle.R
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/20
 *Description:
 */
val Int.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

fun <T : View> T.withTrigger(delay: Long = 600): T {
    triggerDelay = delay
    return this
}


fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    block(it as T)
}


fun <T : View> T.clickWithTrigger(time: Long = 600, block: (T) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(it as T)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else -601
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else 600
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
        triggerLastTime = currentClickTime
    }
    return flag
}


interface OnLazyClickListener : View.OnClickListener {

    override fun onClick(v: View?) {
        if (v?.clickEnable() == true) {
            onLazyClick(v)
        }
    }

    fun onLazyClick(v: View)
}

fun loadImageWithCallback(imageView:ImageView,url: String, onSuccess: () -> Unit = {}) {
    val imageLoader = ImageLoader(appContext)

    // 创建 ImageRequest
    val request = ImageRequest.Builder(appContext).placeholder(R.drawable.avatar_default).data(url).allowHardware(false).target(object:Target{
        override fun onSuccess(result: Drawable) {
            super.onSuccess(result)
            imageView.setImageDrawable(result)
            onSuccess()
        }

    }).build()
    imageLoader.enqueue(request)
}

