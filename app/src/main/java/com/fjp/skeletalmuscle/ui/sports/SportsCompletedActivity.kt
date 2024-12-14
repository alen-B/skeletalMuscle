package com.fjp.skeletalmuscle.ui.sports

import android.os.Bundle
import android.view.View
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.weight.pop.SharePop
import com.fjp.skeletalmuscle.data.model.bean.HighKneeSports
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.TodayhignKneeSports
import com.fjp.skeletalmuscle.databinding.ActivitySportsCompletedBinding
import com.fjp.skeletalmuscle.viewmodel.state.SportsCompletedViewModel
import com.lxj.xpopup.XPopup

class SportsCompletedActivity : BaseActivity<SportsCompletedViewModel, ActivitySportsCompletedBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.sports_completed_title))
        val sportsCompleted = intent.getParcelableExtra<HighKneeSports>(Constants.INTENT_COMPLETED)
        sportsCompleted?.let { curSports ->
            mDatabind.exerciseIntensityLayout.setValue(curSports.warmupTime, curSports.fatBurningTime, curSports.cardioTime, curSports.breakTime)

            mDatabind.countNumberTv.text = curSports.times.toString()
            mDatabind.heartRateNumberTv.text = "${curSports.minHeartRate}-${curSports.maxHeartRate}"
            mDatabind.heatNumberTv.text = curSports.calories.toString()
            mViewModel.score.set(curSports.score.toString())
            println(curSports.toString())
            mDatabind.timeTv.text = DateTimeUtil.formSportTime(curSports.time)
            if(curSports.type == SportsType.HIGH_KNEE.type){
                mDatabind.sportsTimeTv.text = String.format(getString(R.string.sports_completed_sports_time), App.sportsTime)
            }else if(curSports.type == SportsType.PLANK.type){
                mDatabind.countCl.visibility = View.GONE
                mDatabind.sportsTimeTv.text = String.format(getString(R.string.sports_completed_sports_time_plank), App.sportsTime)
            }else if(curSports.type == SportsType.DUMBBELL.type){
                mDatabind.sportsTimeTv.text = String.format(getString(R.string.sports_completed_sports_time_dumbbell), App.sportsTime)
            }

        }

    }

    inner class ProxyClick {
        fun clickShare() {
            sharePop()
        }

        fun clickCompleted() {
            finish()

        }

        fun clickFinish() {
            finish()

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

    fun sharePop() {
        val sharePop = SharePop(this@SportsCompletedActivity, object : SharePop.Listener {
            override fun share(pop: SharePop) {
                pop.dismiss()
            }

        })
        val pop = XPopup.Builder(this@SportsCompletedActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(sharePop)

        pop.show()
    }
}