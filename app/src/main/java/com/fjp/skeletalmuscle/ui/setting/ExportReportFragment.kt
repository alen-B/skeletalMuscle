package com.fjp.skeletalmuscle.ui.setting

import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.DatetimeUtil
import com.fjp.skeletalmuscle.databinding.FragmentExportReportBinding
import com.fjp.skeletalmuscle.viewmodel.state.ExportReportViewModel

class ExportReportFragment : BaseFragment<ExportReportViewModel,FragmentExportReportBinding>() {

    companion object {
        fun newInstance() = ExportReportFragment()
    }

    inner class ProxyClick{

        fun clickExport(){

        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.curWeekTv.text = DatetimeUtil.getCurWeek()
        mDatabind.curMonthTv.text = DatetimeUtil.getCurMonth()
        mDatabind.curWeekRB.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                mDatabind.customerTimeRB.isChecked = false
                mDatabind.curMonthRB.isChecked = false
            }
        }
        mDatabind.curMonthRB.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                mDatabind.customerTimeRB.isChecked = false
                mDatabind.curWeekRB.isChecked = false
            }
        }
        mDatabind.customerTimeRB.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                mDatabind.curMonthRB.isChecked = false
                mDatabind.curWeekRB.isChecked = false
            }
        }
    }

}