package com.fjp.skeletalmuscle.ui.setting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentExportReportBinding
import com.fjp.skeletalmuscle.viewmodel.state.ExportReportViewModel

class ExportReportFragment : BaseFragment<ExportReportViewModel,FragmentExportReportBinding>() {

    companion object {
        fun newInstance() = ExportReportFragment()
    }

    inner class ProxyClick{

    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
    }

}