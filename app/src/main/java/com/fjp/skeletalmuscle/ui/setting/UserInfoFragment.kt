package com.fjp.skeletalmuscle.ui.setting

import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentUserInfoBinding
import com.fjp.skeletalmuscle.viewmodel.state.UserInfoViewModel

class UserInfoFragment : BaseFragment<UserInfoViewModel,FragmentUserInfoBinding>() {

    companion object {
        fun newInstance() = UserInfoFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
    }


    inner class ProxyClick{

    }
}