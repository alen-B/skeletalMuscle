package com.fjp.skeletalmuscle.ui.setting

import android.os.Bundle
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentSetUserNameBinding

class SetUserNameFragment : BaseFragment<com.fjp.skeletalmuscle.viewmodel.state.SetUserNameViewModel,FragmentSetUserNameBinding>() {

    companion object {
        fun newInstance() = SetUserNameFragment()
    }


   inner class ProxyClick{
       fun clickUpdate(){
           parentFragmentManager.popBackStack()
       }
   }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
    }

}