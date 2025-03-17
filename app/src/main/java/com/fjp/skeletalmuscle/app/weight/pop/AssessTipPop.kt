package com.fjp.skeletalmuscle.app.weight.pop

import android.content.Context
import android.content.Intent
import android.widget.Button
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.ui.assessment.SportsAssessmentResultActivity
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout

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
            context.startActivity(Intent(context, SportsAssessmentResultActivity::class.java))
        }
        findViewById<Button>(R.id.disAssessBtn).setOnClickListener { dismiss() }
    }

    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}