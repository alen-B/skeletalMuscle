package com.fjp.skeletalmuscle.ui.user

import android.content.Intent
import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.databinding.ActivityBornBinding
import com.fjp.skeletalmuscle.viewmodel.state.BornViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectType


class BornActivity : BaseActivity<BornViewModel, ActivityBornBinding>() {
    override fun initView(savedInstanceState: Bundle?)  {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(resources.getString(R.string.born_title))
        mViewModel.curIndex.set("3")
        mViewModel.totalIndex.set("/10")
        mViewModel.showRightText.set(true)
    }

    inner class ProxyClick {
        fun next() {
           val intent =  Intent(this@BornActivity,SingleSelectActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.HEIGHT.type)
            startActivity(intent)
        }

        fun finish() {
            this@BornActivity.finish()

        }


    }

    override fun createObserver() {
        super.createObserver()
        App.eventViewModelInstance.finish.observeInActivity(this) {
            finish()
        }
    }
}