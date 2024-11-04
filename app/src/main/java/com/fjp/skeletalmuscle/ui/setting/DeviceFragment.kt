package com.fjp.skeletalmuscle.ui.setting

import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentSettingDeviceBinding
import com.fjp.skeletalmuscle.databinding.FragmentUserInfoBinding
import com.fjp.skeletalmuscle.viewmodel.state.DeviceViewModel
import com.fjp.skeletalmuscle.viewmodel.state.UserInfoViewModel

class DeviceFragment : BaseFragment<DeviceViewModel,FragmentSettingDeviceBinding>() {

    companion object {
        fun newInstance() = DeviceFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
    }


    inner class ProxyClick{

    }
}