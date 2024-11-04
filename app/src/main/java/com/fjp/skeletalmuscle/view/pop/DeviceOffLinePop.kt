package com.fjp.skeletalmuscle.view.pop

import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.fjp.skeletalmuscle.R
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class DeviceOffLinePop(context: Context, val listener: Listener) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout

    interface Listener {
        fun reconnect(pop:DeviceOffLinePop)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_device_offline
    }

    override fun onCreate() {
        super.onCreate()

        val leftIv = findViewById<ImageView>(R.id.leftIv)
        val titleTv = findViewById<TextView>(R.id.titleTv)
        val braceletLL = findViewById<LinearLayoutCompat>(R.id.braceletLL)
        val braceletTv = findViewById<TextView>(R.id.braceletTv)
        val braceletIv = findViewById<ImageView>(R.id.braceletIv)
        val rightKneeLL = findViewById<LinearLayoutCompat>(R.id.rightKneeLL)
        val leftKneeTv = findViewById<TextView>(R.id.leftKneeTv)
        val leftKneeIv = findViewById<ImageView>(R.id.leftKneeIv)
        val leftKneeLL = findViewById<LinearLayoutCompat>(R.id.leftKneeLL)
        val rightKneeTv = findViewById<TextView>(R.id.rightKneeTv)
        val rightKneeIv = findViewById<ImageView>(R.id.rightKneeIv)
        findViewById<Button>(R.id.connectBtn).setOnClickListener {
            listener.reconnect(this)
        }
        blurLayout = findViewById<BlurLayout>(R.id.blurLayout)
        blurLayout.startBlur()
    }

    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}