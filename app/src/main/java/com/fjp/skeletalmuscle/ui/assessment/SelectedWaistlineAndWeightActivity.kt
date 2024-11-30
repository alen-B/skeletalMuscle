package com.fjp.skeletalmuscle.ui.assessment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.databinding.ActivitySelectedWaistlineAndWeightBinding
import com.fjp.skeletalmuscle.ui.assessment.adapter.WaistlineAndWeightAdapter
import com.fjp.skeletalmuscle.viewmodel.state.SelectedWaistlineAndWeightViewModel

class SelectedWaistlineAndWeightActivity : BaseActivity<SelectedWaistlineAndWeightViewModel, ActivitySelectedWaistlineAndWeightBinding>() {
    var currWeightIndex = 0
    var currWaistLineIndex = 0
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()
        mDatabind.viewModel = mViewModel
        initWeightData()
        initWaistLineData()
        mViewModel.title.set(getString(R.string.selected_waistline_weight_title))

        val weightAdapter = WaistlineAndWeightAdapter(mViewModel.weightDataArr as ArrayList<String>, currWeightIndex, clickItem = { item, position ->
            currWeightIndex = position
        })
        mDatabind.weightRView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), weightAdapter)

        val waistlineAdapter = WaistlineAndWeightAdapter(mViewModel.waistlineDataArr as ArrayList<String>, currWaistLineIndex, clickItem = { item, position ->
//            currIndex = position
            currWaistLineIndex = position
        })
        mDatabind.waistlineRView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), waistlineAdapter)
    }

    private fun initWeightData() {

        for (i in 0..70) {
            if ((i + 30) === App.userInfo.weight.toInt()) {
                currWeightIndex = i
            }
            mViewModel.weightDataArr.add(i, "${i + 30}kg")
        }
    }

    private fun initWaistLineData() {

        for (i in 0..50) {
            mViewModel.waistlineDataArr.add(i, "${i + 70}cm")
        }
    }


    inner class ProxyClick {

        fun clickFinish() {
            finish()
        }

        fun clickComplete() {

        }
    }

}