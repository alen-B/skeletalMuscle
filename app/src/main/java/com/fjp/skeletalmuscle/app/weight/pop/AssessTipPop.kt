package com.fjp.skeletalmuscle.app.weight.pop

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.ui.assessment.SportsAssessmentResultActivity
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class AssessTipPop(context: Context) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout
    override fun getImplLayoutId(): Int {
        return R.layout.pop_assess_tip
    }

    override fun onCreate() {
        super.onCreate()
        findViewById<Button>(R.id.assessBtn).setOnClickListener {
            dismiss()
            context.startActivity(Intent(context,SportsAssessmentResultActivity::class.java))
        }
        findViewById<Button>(R.id.disAssessBtn).setOnClickListener { dismiss() }
    }

    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}