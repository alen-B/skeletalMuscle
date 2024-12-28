package com.fjp.skeletalmuscle.app.weight.pop

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.data.model.bean.result.VersionData
import com.fjp.skeletalmuscle.ui.setting.SettingActivity
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout
import me.hgj.jetpackmvvm.ext.view.visible

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class NewVersionPop(val context: Activity, val listener: Listener,val version:VersionData) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout
    private lateinit var progressBar: ProgressBar

    interface Listener {
        fun onClickUpdate(pop: NewVersionPop)
        fun onClickNotUpdate()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_new_version
    }

    override fun onCreate() {
        super.onCreate()

        findViewById<TextView>(R.id.versionTv).text = "v${version.version}"
        var content = ""
        version.content.forEach {
            content+=it+"\n"
        }
        findViewById<TextView>(R.id.contentTv).text = content
        findViewById<Button>(R.id.cancelBtn).setOnClickListener {
            listener.onClickNotUpdate()
            dismiss()
        }
        findViewById<Button>(R.id.updateBtn).setOnClickListener {
            listener.onClickUpdate(this)
        }
        blurLayout = findViewById(R.id.blurLayout)
        progressBar = findViewById(R.id.progressBar)
        blurLayout.startBlur()

    }

    fun setProgress(progress:Int){
        if(!progressBar.isVisible){
            progressBar.visibility=View.VISIBLE
        }
        progressBar.progress = progress
    }
    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}