package com.fjp.skeletalmuscle.app.weight.pop

import android.content.Context
import android.widget.TextView
import com.fjp.skeletalmuscle.R
import com.lxj.xpopup.core.BottomPopupView
import io.alterac.blurkit.BlurLayout


/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class TakePhotoPop(context: Context, val listener: Listener) : BottomPopupView(context) {
    companion object {
        const val ALBUM = 0
        const val TAKE_PHOTO = 1
        const val CANCEL = 2
    }

    private lateinit var blurLayout: BlurLayout
    var curIndex = 0

    interface Listener {
        fun onclickItem(index: Int, pop: TakePhotoPop)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_take_photo
    }

    override fun onCreate() {
        super.onCreate()
        val albumTv = findViewById<TextView>(R.id.albumTv)
        val takePhotoTv = findViewById<TextView>(R.id.takePhotoTv)
        val cancelTv = findViewById<TextView>(R.id.cancelTv)
        albumTv.setOnClickListener {
            listener.onclickItem(ALBUM, this)
        }
        takePhotoTv.setOnClickListener {
            listener.onclickItem(TAKE_PHOTO, this)
        }
        cancelTv.setOnClickListener {
            listener.onclickItem(CANCEL, this)
        }
    }
}