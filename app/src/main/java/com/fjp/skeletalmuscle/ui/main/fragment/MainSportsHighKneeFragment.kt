package com.fjp.skeletalmuscle.ui.main.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.databinding.FragmentMainSportsHighKneeBinding
import com.fjp.skeletalmuscle.ui.main.TodaySportsActivity
import com.fjp.skeletalmuscle.viewmodel.state.MainSportsHighKneeViewModel

class MainSportsHighKneeFragment : BaseFragment<MainSportsHighKneeViewModel, FragmentMainSportsHighKneeBinding>() {
    lateinit var todayDataResult: TodayDataResult

    companion object {
        fun newInstance(todayDataResult: TodayDataResult): MainSportsHighKneeFragment {
            val fragment = MainSportsHighKneeFragment()
            val bundle = Bundle()
            bundle.putParcelable(Constants.INTENT_KEY_TODAY_SPORTS_DATA, todayDataResult)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        todayDataResult = arguments?.get(Constants.INTENT_KEY_TODAY_SPORTS_DATA) as TodayDataResult
        mDatabind.viewModel = mViewModel
        if (todayDataResult.sport_lift_leg.sport_time != 0L) {
            mDatabind.click = ProxyClick()
            mViewModel.curScore.set(todayDataResult.sport_lift_leg.score.toString())
            mViewModel.sportsTime.set(DateTimeUtil.formSportTime(todayDataResult.sport_lift_leg.sport_time))
            mViewModel.heartRate.set("${todayDataResult.sport_lift_leg.min_rate_value}-${todayDataResult.sport_lift_leg.max_rate_value}")
            mViewModel.heat.set((todayDataResult.sport_lift_leg.sum_calorie / 1000).toString())
            mViewModel.endurance.set(todayDataResult.sport_lift_leg.cardiorespiratory_endurance.toString())
            mDatabind.exerciseIntensityLayout.setValue(todayDataResult.sport_lift_leg.warm_up_activation, todayDataResult.sport_lift_leg.efficient_grease_burning, todayDataResult.sport_lift_leg.heart_lung_enhancement, todayDataResult.sport_lift_leg.extreme_breakthrough)
        } else {
            mDatabind.exerciseIntensityLayout.setValue(1, 0, 0, 0)
        }

    }


    inner class ProxyClick {
        fun clickTodaySports() {
            TodaySportsActivity.start(context!!, todayDataResult, SportsType.HIGH_KNEE)
        }
    }
}