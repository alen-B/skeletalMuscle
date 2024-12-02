package com.fjp.skeletalmuscle.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.result.SportLiftLeg
import com.fjp.skeletalmuscle.databinding.FragmentMainSportsHighKneeBinding
import com.fjp.skeletalmuscle.ui.main.TodaySportsActivity
import com.fjp.skeletalmuscle.viewmodel.state.MainSportsHighKneeViewModel

class MainSportsHighKneeFragment(val sportLiftLeg: SportLiftLeg) : BaseFragment<MainSportsHighKneeViewModel, FragmentMainSportsHighKneeBinding>() {

    companion object {
        fun newInstance(sportLiftLeg: SportLiftLeg) = MainSportsHighKneeFragment(sportLiftLeg)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.curScore.set(sportLiftLeg.score.toString())
        mViewModel.sportsTime.set(DateTimeUtil.formSportTime(sportLiftLeg.left_times))
        mViewModel.heartRate.set("${sportLiftLeg.min_rate_value}-${sportLiftLeg.max_rate_value}")
        mViewModel.endurance.set((sportLiftLeg.sum_calorie/1000).toString())
        mViewModel.heat.set(sportLiftLeg.cardiorespiratory_endurance.toString())
        mDatabind.click = ProxyClick()
        mDatabind.exerciseIntensityLayout.setValue(sportLiftLeg.warm_up_activation, sportLiftLeg.efficient_grease_burning, sportLiftLeg.heart_lung_enhancement, sportLiftLeg.extreme_breakthrough)
    }


    inner class ProxyClick {
        fun clickTodaySports() {
            startActivity(Intent(activity, TodaySportsActivity::class.java))
        }
    }
}