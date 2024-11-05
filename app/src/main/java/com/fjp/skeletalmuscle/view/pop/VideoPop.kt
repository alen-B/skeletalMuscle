package com.fjp.skeletalmuscle.view.pop

import android.content.Context
import android.net.Uri
import android.widget.Button
import android.widget.VideoView
import com.fjp.skeletalmuscle.R
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class VideoPop(context: Context, val listener: Listener) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout

    interface Listener {
        fun jump(pop:VideoPop)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_video
    }

    override fun onCreate() {
        super.onCreate()

        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoUri = Uri.parse("android.resource://" +context.packageName + "/" + R.raw.tcsp)
       videoView.setVideoURI(videoUri)
        videoView.start()
        videoView.setOnCompletionListener { // 当视频播放完成时关闭Dialog并跳转到新的Activity
            listener.jump(this)
        }
        findViewById<Button>(R.id.nextBtn).setOnClickListener {
            listener.jump(this)
        }
        blurLayout = findViewById(R.id.blurLayout)
        blurLayout.startBlur()
    }

    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}