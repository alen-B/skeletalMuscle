package com.fjp.skeletalmuscle.ui.user

import android.content.Intent
import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.databinding.ActivityInputNameBinding
import com.fjp.skeletalmuscle.viewmodel.state.InputNameViewModel

class InputNameActivity  : BaseActivity<InputNameViewModel, ActivityInputNameBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(resources.getString(R.string.input_name_title))
        mViewModel.curIndex.set("1")
        mViewModel.totalIndex.set("/10")
        mViewModel.showRightText.set(true)
    }

    inner class ProxyClick {
        fun next(){
            if(mViewModel.name.get()!!.isEmpty()){
                showToast(getString(R.string.input_name_tips))
                return
            }
            App.userInfo?.name = mViewModel.name.get().toString()
            startActivity(Intent(this@InputNameActivity,SelectGenderActivity::class.java))

        }
    }

    override fun createObserver() {
        super.createObserver()
    }
}