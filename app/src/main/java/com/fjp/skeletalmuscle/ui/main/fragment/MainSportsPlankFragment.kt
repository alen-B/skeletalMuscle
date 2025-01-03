package com.fjp.skeletalmuscle.ui.main.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.databinding.FragmentMainSportsPlankBinding
import com.fjp.skeletalmuscle.ui.main.TodaySportsActivity
import com.fjp.skeletalmuscle.viewmodel.state.MainSportsPlankViewModel

class MainSportsPlankFragment : BaseFragment<MainSportsPlankViewModel, FragmentMainSportsPlankBinding>() {
    lateinit var todayDataResult: TodayDataResult

    companion object {
        fun newInstance(todayDataResult: TodayDataResult): MainSportsPlankFragment {
            val fragment = MainSportsPlankFragment()
            val bundle = Bundle()
            bundle.putParcelable(Constants.INTENT_KEY_TODAY_SPORTS_DATA, todayDataResult)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        todayDataResult = arguments?.get(Constants.INTENT_KEY_TODAY_SPORTS_DATA) as TodayDataResult
        mDatabind.viewModel = mViewModel
        if (todayDataResult.sport_flat_support.sport_time != 0L) {
            mDatabind.click = ProxyClick()
            mViewModel.curScore.set(todayDataResult.sport_flat_support.score.toString())
            mViewModel.sportsTime.set(DateTimeUtil.formSportTime(todayDataResult.sport_flat_support.sport_time))
            mViewModel.heartRate.set(todayDataResult.sport_flat_support.avg_rate_value.toString())
            mViewModel.heat.set((todayDataResult.sport_flat_support.sum_calorie / 1000).toString())
        }

    }


    inner class ProxyClick {
        fun clickTodaySports() {
            TodaySportsActivity.start(context!!, todayDataResult, SportsType.PLANK)
        }
    }
}