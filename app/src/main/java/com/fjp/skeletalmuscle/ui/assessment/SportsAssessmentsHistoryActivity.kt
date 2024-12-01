package com.fjp.skeletalmuscle.ui.assessment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.databinding.ActivitySportsAssessmentsHistoryBinding
import com.fjp.skeletalmuscle.ui.assessment.adapter.SportsAssessmentsHistoryAdapter
import com.fjp.skeletalmuscle.ui.assessment.fragment.AssessmentResultFragment
import com.fjp.skeletalmuscle.viewmodel.state.SportsAssessmentsHistoryViewModel

class SportsAssessmentsHistoryActivity : BaseActivity<SportsAssessmentsHistoryViewModel, ActivitySportsAssessmentsHistoryBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.sports_assessment_history_title))
        val fragments = arrayListOf<Fragment>(AssessmentResultFragment.newInstance(1), AssessmentResultFragment.newInstance(2), AssessmentResultFragment.newInstance(3), AssessmentResultFragment.newInstance(4), AssessmentResultFragment.newInstance(5), AssessmentResultFragment.newInstance(6), AssessmentResultFragment.newInstance(7), AssessmentResultFragment.newInstance(8), AssessmentResultFragment.newInstance(9), AssessmentResultFragment.newInstance(10), AssessmentResultFragment.newInstance(11), AssessmentResultFragment.newInstance(12))
        mDatabind.viewpager.adapter = SportsAssessmentsHistoryAdapter(supportFragmentManager, fragments)
        mDatabind.tabLayout.setViewPager(mDatabind.viewpager)
    }

    inner class ProxyClick {

        fun clickPreYear() {

        }

        fun clickNextYear() {

        }
    }

}