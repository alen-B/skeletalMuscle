package com.fjp.skeletalmuscle.ui.main.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.databinding.FragmentMainSportsDumbbellBinding
import com.fjp.skeletalmuscle.ui.main.TodaySportsActivity
import com.fjp.skeletalmuscle.viewmodel.state.MainSportsDumbbellViewModel

class MainSportsDumbbellFragment(val todayDataResult: TodayDataResult) : BaseFragment<MainSportsDumbbellViewModel, FragmentMainSportsDumbbellBinding>() {

    companion object {
        fun newInstance(todayDataResult: TodayDataResult) = MainSportsDumbbellFragment(todayDataResult)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.curScore.set(todayDataResult.sport_dumbbell.score.toString())
        mViewModel.sportsTime.set(DateTimeUtil.formSportTime(todayDataResult.sport_dumbbell.end_time - todayDataResult.sport_dumbbell.start_time))
        mViewModel.heartRate.set(todayDataResult.sport_dumbbell.avg_rate_value.toString())
        mViewModel.weight.set(todayDataResult.sport_dumbbell.weight.toString())
        mViewModel.heat.set(todayDataResult.sport_dumbbell.sum_calorie.toString())
    }


    inner class ProxyClick {
        fun clickTodaySports() {
            TodaySportsActivity.start(context!!, todayDataResult, SportsType.DUMBBELL)
        }
    }
}