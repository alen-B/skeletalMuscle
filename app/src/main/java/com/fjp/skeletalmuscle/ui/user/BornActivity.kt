package com.fjp.skeletalmuscle.ui.user

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.databinding.ActivityBornBinding
import com.fjp.skeletalmuscle.viewmodel.state.BornViewModel


class BornActivity : BaseActivity<BornViewModel, ActivityBornBinding>() {
    override fun initView(savedInstanceState: Bundle?)  {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(resources.getString(R.string.born_title))
        mViewModel.curIndex.set("3")
        mViewModel.totalIndex.set("/9")
        mViewModel.showRightText.set(true)
    }

    inner class ProxyClick {
        fun next() {

        }

        fun finish() {
            this@BornActivity.finish()

        }


    }

}