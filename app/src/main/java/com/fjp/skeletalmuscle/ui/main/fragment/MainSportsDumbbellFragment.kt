package com.fjp.skeletalmuscle.ui.main.fragment

import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.databinding.FragmentMainSportsDumbbellBinding
import com.fjp.skeletalmuscle.ui.main.TodaySportsActivity
import com.fjp.skeletalmuscle.viewmodel.state.MainSportsDumbbellViewModel

class MainSportsDumbbellFragment : BaseFragment<MainSportsDumbbellViewModel, FragmentMainSportsDumbbellBinding>() {
    lateinit var  todayDataResult: TodayDataResult
    companion object {
        fun newInstance(todayDataResult: TodayDataResult):MainSportsDumbbellFragment {
            val fragment = MainSportsDumbbellFragment()
            val bundle = Bundle()
            bundle.putParcelable(Constants.INTENT_KEY_TODAY_SPORTS_DATA, todayDataResult)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        todayDataResult = arguments?.get(Constants.INTENT_KEY_TODAY_SPORTS_DATA) as TodayDataResult
        mDatabind.viewModel = mViewModel
        if(todayDataResult.sport_dumbbell.sport_time!=0L){
            mDatabind.click = ProxyClick()
            mViewModel.curScore.set(todayDataResult.sport_dumbbell.score.toString())
            mViewModel.sportsTime.set(DateTimeUtil.formSportTime(todayDataResult.sport_dumbbell.sport_time))
            mViewModel.heartRate.set(todayDataResult.sport_dumbbell.avg_rate_value.toString())
            mViewModel.weight.set(todayDataResult.sport_dumbbell.weight.toString())
            mViewModel.heat.set((todayDataResult.sport_dumbbell.sum_calorie/1000).toString())
        }


    }


    inner class ProxyClick {
        fun clickTodaySports() {
            TodaySportsActivity.start(context!!, todayDataResult, SportsType.DUMBBELL)
        }
    }
}