package com.fjp.skeletalmuscle.ui.setting.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.databinding.FragmentSetUserNameBinding
import com.fjp.skeletalmuscle.viewmodel.request.SaveUserInfoViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SetUserNameViewModel
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.isPhone

class SetUserNameAndPhoneFragment(val isUserName: Boolean) : BaseFragment<SetUserNameViewModel, FragmentSetUserNameBinding>() {
    val saveUserInfoViewModel: SaveUserInfoViewModel by viewModels()

    companion object {
        fun newInstance(isUserName: Boolean): SetUserNameAndPhoneFragment {

            return SetUserNameAndPhoneFragment(isUserName)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        if (isUserName) {
            mViewModel.data.set(App.userInfo.name)
        } else {
            mViewModel.data.set(App.userInfo.mobile)
        }

    }


    override fun createObserver() {
        super.createObserver()
        saveUserInfoViewModel.saveResult.observe(this) {
            parseState(it, {
                if (isUserName) {
                    App.userInfo.name = mViewModel.data.get().toString()
                    eventViewModel.updateUserNameEvent.value = mViewModel.data.get().toString()
                } else {
                    App.userInfo.mobile = mViewModel.data.get().toString()
                    eventViewModel.updatePhoneEvent.value = mViewModel.data.get().toString()
                }
                CacheUtil.setUser(App.userInfo)
                parentFragmentManager.popBackStack()

            }, {
                appContext.showToast(getString(R.string.request_failed))
            })
        }
    }

    inner class ProxyClick {
        fun clickUpdate() {
            val tempUserInfo = App.userInfo
            if (isUserName) {
                tempUserInfo.name = mViewModel.data.get().toString()
                saveUserInfoViewModel.saveInfoReq(tempUserInfo)
            } else {
                if (!mViewModel.data.get().toString().isPhone()) {
                    appContext.showToast(getString(R.string.login_input_success_phone))
                    return
                }
                tempUserInfo.mobile = mViewModel.data.get().toString()
                saveUserInfoViewModel.saveInfoReq(tempUserInfo)
            }


        }
    }


}