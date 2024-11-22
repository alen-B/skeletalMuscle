package com.fjp.skeletalmuscle.app.weight.pop

import android.app.Activity
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.dp
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.weight.recyclerview.SpaceItemDecoration
import com.fjp.skeletalmuscle.data.model.bean.Account
import com.fjp.skeletalmuscle.ui.setting.SettingActivity
import com.fjp.skeletalmuscle.ui.setting.adapter.AccountAdapter
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class NewVersionPop(val context: Activity, val listener: Listener) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout

    interface Listener {
        fun onClickUpdate(pop: NewVersionPop)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_new_version
    }

    override fun onCreate() {
        super.onCreate()

        findViewById<TextView>(R.id.versionTv)
        findViewById<TextView>(R.id.contentTv)
        findViewById<Button>(R.id.cancelBtn).setOnClickListener {
            dismiss()
        }
        findViewById<Button>(R.id.updateBtn).setOnClickListener {
            listener.onClickUpdate(this)
        }
        blurLayout = findViewById(R.id.blurLayout)
        blurLayout.startBlur()

        findViewById<ImageView>(R.id.settingIv)?.setOnClickListener {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}