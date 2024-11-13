package com.fjp.skeletalmuscle.ui.highKnee

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DatetimeUtil
import com.fjp.skeletalmuscle.data.model.bean.HighKneeSports
import com.fjp.skeletalmuscle.databinding.ActivitySportsCompletedBinding
import com.fjp.skeletalmuscle.app.weight.pop.SharePop
import com.fjp.skeletalmuscle.data.model.bean.TodayhignKneeSports
import com.fjp.skeletalmuscle.viewmodel.state.SportsCompletedViewModel
import com.lxj.xpopup.XPopup

class SportsCompletedActivity : BaseActivity<SportsCompletedViewModel, ActivitySportsCompletedBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.sports_completed_title))
        val sportsCompleted = intent.getParcelableExtra<HighKneeSports>(Constants.INTENT_COMPLETED)
        sportsCompleted?.let {curSports->
            mDatabind.sportsTimeTv.text = String.format(getString(R.string.sports_completed_sports_time), App.sportsTime)
            mDatabind.countNumberTv.text = curSports.times.toString()
            mDatabind.heartRateNumberTv.text = "${curSports.minHeartRate}-${curSports.maxHeartRate}"
            mDatabind.heatNumberTv.text = curSports.calories.toString()
            mViewModel.score.set(curSports.score.toString())
            println(curSports.toString())
            mDatabind.timeTv.text = DatetimeUtil.formSportTime(curSports.time)
            var sports = CacheUtil.getSports()
            if (sports == null) {
                val map = mutableMapOf<String, HighKneeSports>()
                map[DatetimeUtil.getCurDate2Str()] = curSports
                sports = TodayhignKneeSports(sports = map)
                CacheUtil.setSports(sports)
            } else {
                val todaySports = sports.sports[DatetimeUtil.getCurDate2Str()]
                todaySports?.let {
                    it.times = curSports.times
                    it.time = curSports.time
                    it.score=(curSports.score+it.score)/2
                    it.warmupTime = curSports.warmupTime+it.warmupTime
                    it.fatBurningTime = curSports.fatBurningTime+it.fatBurningTime
                    it.cardioTime = curSports.cardioTime+it.cardioTime
                    it.breakTime = curSports.breakTime+it.breakTime
                    var maxHeartRate =curSports.maxHeartRate
                    if(curSports.maxHeartRate>maxHeartRate){
                        maxHeartRate = curSports.maxHeartRate
                    }
                    it.maxHeartRate = maxHeartRate
                    var minHeartRate =curSports.minHeartRate
                    if(curSports.minHeartRate<minHeartRate){
                        minHeartRate = curSports.minHeartRate
                    }
                    it.minHeartRate = minHeartRate
                }
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