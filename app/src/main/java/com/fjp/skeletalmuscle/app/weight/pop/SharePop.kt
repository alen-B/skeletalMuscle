package com.fjp.skeletalmuscle.app.weight.pop

import android.content.Context
import android.widget.Button
import androidx.appcompat.widget.LinearLayoutCompat
import com.fjp.skeletalmuscle.R
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class SharePop(context: Context, val listener: Listener) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout

    interface Listener {
        fun share(pop: SharePop)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_share
    }

    override fun onCreate() {
        super.onCreate()

        val friendsLL = findViewById<LinearLayoutCompat>(R.id.friendsLL)
        val circleFriendsLL = findViewById<LinearLayoutCompat>(R.id.circleFriendsLL)
        val localSaveLL = findViewById<LinearLayoutCompat>(R.id.localSaveLL)
        findViewById<Button>(R.id.shareBtn).setOnClickListener {
            listener.share(this)
        }
        blurLayout = findViewById<BlurLayout>(R.id.blurLayout)
        blurLayout.startBlur()
    }

    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}