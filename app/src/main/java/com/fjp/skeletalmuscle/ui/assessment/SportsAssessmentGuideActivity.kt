package com.fjp.skeletalmuscle.ui.assessment

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.app.weight.recyclerview.SpaceItemDecoration
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivitySportsAssessmentGuideBinding
import com.fjp.skeletalmuscle.ui.assessment.adapter.AssessmentTypeAdapter
import com.fjp.skeletalmuscle.ui.deviceconnectguide.DeviceConnectGuideActivity
import com.fjp.skeletalmuscle.viewmodel.state.SportsAssessmentTypeSelectedViewModel

class SportsAssessmentGuideActivity : BaseActivity<SportsAssessmentTypeSelectedViewModel, ActivitySportsAssessmentGuideBinding>() {
    var currIndex = 0

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()
        mDatabind.viewModel = mViewModel
        mViewModel.title.set("接下来我将引导您完成测试")
        val singleSelectAdapter = AssessmentTypeAdapter(mViewModel.dataArr as ArrayList<String>)
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mDatabind.recyclerView.layoutManager = layoutManager
        mDatabind.recyclerView.adapter = singleSelectAdapter
        mDatabind.recyclerView.addItemDecoration(SpaceItemDecoration(16, 0))

    }


    inner class ProxyClick {

        fun clickFinish() {
            finish()
        }

        fun clickStart() {
            App.sportsType = SportsType.ASSESSMENT
            if (SMBleManager.highKneeLeftDeviceIsConnected() && SMBleManager.highKneeRightDeviceIsConnected()) {
                val intent = Intent(this@SportsAssessmentGuideActivity, SelectedWaistlineAndWeightActivity::class.java)
                startActivity(intent)
            } else {
                App.sportsType = SportsType.ASSESSMENT
                DeviceConnectGuideActivity.start(this@SportsAssessmentGuideActivity)
            }


        }
    }
}