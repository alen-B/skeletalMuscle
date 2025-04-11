package com.fjp.skeletalmuscle.ui.assessment

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.util.Constants
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

        val weightAdapter = WaistlineAndWeightAdapter(mViewModel.weightDataArr as ArrayList<Int>, true, currWeightIndex, clickItem = { item, position ->
            currWeightIndex = position
        })
        mDatabind.weightRView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), weightAdapter)
        mDatabind.weightRView.smoothScrollToPosition(currWeightIndex + 3)
        val waistlineAdapter = WaistlineAndWeightAdapter(mViewModel.waistlineDataArr as ArrayList<Int>, false, currWaistLineIndex, clickItem = { item, position ->
//            currIndex = position
            currWaistLineIndex = position
        })
        mDatabind.waistlineRView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), waistlineAdapter)
        mDatabind.waistlineRView.smoothScrollToPosition(currWaistLineIndex + 3)
    }

    private fun initWeightData() {

        for (i in 0..70) {
            if ((i + 30) == App.userInfo.weight) {
                currWeightIndex = i
            }
            mViewModel.weightDataArr.add(i, i + 30)
        }
    }

    private fun initWaistLineData() {

        for (i in 0..80) {
            if ((i + 40) == App.userInfo.waistline.toInt()) {
                currWaistLineIndex = i
            }
            mViewModel.waistlineDataArr.add(i, i + 40)
        }
    }


    inner class ProxyClick {
        fun clickFinish() {
            finish()
        }

        fun clickComplete() {
            val intent = Intent(this@SelectedWaistlineAndWeightActivity, SportsAssessmentActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_WEIGHT, mViewModel.weightDataArr[currWeightIndex])
            intent.putExtra(Constants.INTENT_KEY_WAISTLINE, mViewModel.waistlineDataArr[currWaistLineIndex])
            startActivity(intent)
        }
    }

}