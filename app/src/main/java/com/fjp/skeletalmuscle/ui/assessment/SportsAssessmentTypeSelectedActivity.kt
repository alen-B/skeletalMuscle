package com.fjp.skeletalmuscle.ui.assessment

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.databinding.ActivitySportsAssessmentTypeSelectedBinding
import com.fjp.skeletalmuscle.ui.assessment.adapter.AssessmentTypeAdapter
import com.fjp.skeletalmuscle.viewmodel.state.SportsAssessmentTypeSelectedViewModel

class SportsAssessmentTypeSelectedActivity : BaseActivity<SportsAssessmentTypeSelectedViewModel, ActivitySportsAssessmentTypeSelectedBinding>() {
    var currIndex = 0

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()
        mDatabind.viewModel = mViewModel
        val singleSelectAdapter = AssessmentTypeAdapter(mViewModel.dataArr as ArrayList<String>, 0, clickItem = { item, position ->
            currIndex = position
        })
        val laoutManager = GridLayoutManager(this, 2)
        mDatabind.recyclerView.layoutManager = laoutManager
        mDatabind.recyclerView.adapter = singleSelectAdapter

    }


    inner class ProxyClick {

        fun clickFinish() {
            finish()
        }

        fun clickStart() {

        }
    }
}