package com.fjp.skeletalmuscle.ui.main.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.databinding.FragmentMainSportsHighKneeBinding
import com.fjp.skeletalmuscle.ui.main.TodaySportsActivity
import com.fjp.skeletalmuscle.viewmodel.state.MainSportsHighKneeViewModel

class MainSportsHighKneeFragment(val todayDataResult: TodayDataResult) : BaseFragment<MainSportsHighKneeViewModel, FragmentMainSportsHighKneeBinding>() {

    companion object {
        fun newInstance(todayDataResult: TodayDataResult) = MainSportsHighKneeFragment(todayDataResult)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        todayDataResult.sport_lift_leg?.let {
            mDatabind.click = ProxyClick()
            mViewModel.curScore.set(it.score.toString())
            mViewModel.sportsTime.set(DateTimeUtil.formSportTime(it.left_times))
            mViewModel.heartRate.set("${it.min_rate_value}-${it.max_rate_value}")
            mViewModel.heat.set((it.sum_calorie / 1000).toString())
            mViewModel.endurance.set(it.cardiorespiratory_endurance.toString())
            mDatabind.exerciseIntensityLayout.setValue(it.warm_up_activation, it.efficient_grease_burning, it.heart_lung_enhancement, it.extreme_breakthrough)
        }

        if(todayDataResult.sport_lift_leg==null){
            mDatabind.exerciseIntensityLayout.setValue(1, 0, 0, 0)
        }

    }


    inner class ProxyClick {
        fun clickTodaySports() {
            TodaySportsActivity.start(context!!, todayDataResult, SportsType.HIGH_KNEE)
        }
    }
}