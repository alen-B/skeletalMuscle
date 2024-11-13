package com.fjp.skeletalmuscle.ui.setting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentSystemSettingBinding
import com.fjp.skeletalmuscle.databinding.FragmentUserInfoBinding
import com.fjp.skeletalmuscle.viewmodel.state.SystemSettingViewModel
import com.fjp.skeletalmuscle.viewmodel.state.UserInfoViewModel

class SystemSettingFragment : BaseFragment<SystemSettingViewModel, FragmentSystemSettingBinding>() {

    companion object {
        fun newInstance() = SystemSettingFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
    }

    inner class ProxyClick{

    }
}