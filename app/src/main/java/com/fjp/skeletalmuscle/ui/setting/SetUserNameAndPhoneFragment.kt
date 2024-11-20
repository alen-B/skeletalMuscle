package com.fjp.skeletalmuscle.ui.setting

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.databinding.FragmentSetUserNameBinding
import com.fjp.skeletalmuscle.viewmodel.state.SetUserNameViewModel
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.util.isPhone

class SetUserNameAndPhoneFragment(val isUserName:Boolean) : BaseFragment<SetUserNameViewModel,FragmentSetUserNameBinding>() {

    companion object {
        fun newInstance(isUserName:Boolean): SetUserNameAndPhoneFragment {

            return SetUserNameAndPhoneFragment(isUserName)
        }
    }


   inner class ProxyClick{
       fun clickUpdate(){
           if(isUserName){
               App.userInfo.name = mViewModel.data.get().toString()
               parentFragmentManager.popBackStack()
               eventViewModel.updateUserNameEvent.value=mViewModel.data.get().toString()
           }else{
               if(!mViewModel.data.get().toString().isPhone()){
                   appContext.showToast(getString(R.string.login_input_success_phone))
                   return
               }
               App.userInfo.phone = mViewModel.data.get().toString()
               eventViewModel.updatePhoneEvent.value=mViewModel.data.get().toString()
               parentFragmentManager.popBackStack()
           }


       }
   }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        if(isUserName){
            mViewModel.data.set( App.userInfo.name)
        }else{
            mViewModel.data.set( App.userInfo.phone)
        }

    }

}