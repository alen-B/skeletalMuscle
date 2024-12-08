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
        mViewModel.curScore.set(todayDataResult.sport_lift_leg.score.toString())
        mViewModel.sportsTime.set(DateTimeUtil.formSportTime(todayDataResult.sport_lift_leg.left_times))
        mViewModel.heartRate.set("${todayDataResult.sport_lift_leg.min_rate_value}-${todayDataResult.sport_lift_leg.max_rate_value}")
        mViewModel.endurance.set((todayDataResult.sport_lift_leg.sum_calorie / 1000).toString())
        mViewModel.heat.set(todayDataResult.sport_lift_leg.cardiorespiratory_endurance.toString())
        mDatabind.click = ProxyClick()
        mDatabind.exerciseIntensityLayout.setValue(todayDataResult.sport_lift_leg.warm_up_activation, todayDataResult.sport_lift_leg.efficient_grease_burning, todayDataResult.sport_lift_leg.heart_lung_enhancement, todayDataResult.sport_lift_leg.extreme_breakthrough)
    }


    inner class ProxyClick {
        fun clickTodaySports() {
            TodaySportsActivity.start(context!!, todayDataResult, SportsType.HIGH_KNEE)
        }
    }
}