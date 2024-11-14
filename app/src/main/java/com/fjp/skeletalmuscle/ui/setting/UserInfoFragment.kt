package com.fjp.skeletalmuscle.ui.setting

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.DatetimeUtil
import com.fjp.skeletalmuscle.databinding.FragmentUserInfoBinding
import com.fjp.skeletalmuscle.viewmodel.state.UserInfoViewModel

class UserInfoFragment : BaseFragment<UserInfoViewModel,FragmentUserInfoBinding>() {

    companion object {
        fun newInstance() = UserInfoFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.userNameLayout.setValue(App.userInfo.name)
        mDatabind.mobileLayout.setValue(App.userInfo.phone)
        mDatabind.bornLayout.setValue(DatetimeUtil.formatDate(App.userInfo.born,DatetimeUtil.DATE_PATTERN))
        var sex="男"
        if(App.userInfo.sex==0){
            sex="女"
        }
        mDatabind.sexLayout.setValue(sex)
        mDatabind.weightLayout.setValue(App.userInfo.weight)
        mDatabind.heightLayout.setValue(App.userInfo.height)
        mDatabind.waistLinLayout.setValue(App.userInfo.waist_line)
    }


    inner class ProxyClick{
        fun clickUpdateUserName(){
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.rightFragment,SetUserNameFragment())
            transaction.addToBackStack("SetUserNameFragment")
            transaction.commit()
        }

    }
}