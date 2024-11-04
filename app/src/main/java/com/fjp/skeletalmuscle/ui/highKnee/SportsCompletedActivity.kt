package com.fjp.skeletalmuscle.ui.highKnee

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.databinding.ActivitySportsCompletedBinding
import com.fjp.skeletalmuscle.view.pop.SharePop
import com.fjp.skeletalmuscle.viewmodel.state.SportsCompletedViewModel
import com.lxj.xpopup.XPopup

class SportsCompletedActivity : BaseActivity<SportsCompletedViewModel,ActivitySportsCompletedBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.sports_completed_title))
    }
    inner class ProxyClick{
        fun clickShare(){
            sharePop()
        }

        fun clickCompleted(){

        }
        fun clickFinish(){

        }
    }

    override fun onResume() {
        super.onResume()
        mDatabind.blurLayout.startBlur()
    }
    override fun onPause() {
        super.onPause()
        mDatabind.blurLayout.pauseBlur()
    }

    fun sharePop(){
        val sharePop = SharePop(this@SportsCompletedActivity,object: SharePop.Listener{
            override fun share(pop: SharePop) {
                pop.dismiss()
            }

        })
        val pop = XPopup.Builder(this@SportsCompletedActivity)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .isDestroyOnDismiss(true)
            .autoOpenSoftInput(false)
            .asCustom(sharePop)

        pop.show()
    }
}