package com.fjp.skeletalmuscle.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.databinding.ActivitySelectGenderBinding
import com.fjp.skeletalmuscle.viewmodel.state.SEX
import com.fjp.skeletalmuscle.viewmodel.state.SelectGenderViewModel

class SelectGenderActivity : BaseActivity<SelectGenderViewModel, ActivitySelectGenderBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(resources.getString(R.string.select_gender_title))
        mViewModel.curIndex.set("2")
        mViewModel.totalIndex.set("/10")
        mViewModel.showRightText.set(true)
    }

    inner class ProxyClick {
        fun next() {
            startActivity(Intent(this@SelectGenderActivity,BornActivity::class.java))

        }

        fun finish() {
            this@SelectGenderActivity.finish()

        }

        fun selectMan() {
            mViewModel.sex.set(SEX.MAN)
            mDatabind.manSelectedIv.visibility = View.VISIBLE
            mDatabind.man.background = AppCompatResources.getDrawable(this@SelectGenderActivity, R.drawable.bg_selected)
            mDatabind.mainTv.setTextColor(AppCompatResources.getColorStateList(this@SelectGenderActivity,R.color.white))

            mDatabind.womanSelectedIv.visibility = View.GONE
            mDatabind.woman.background = AppCompatResources.getDrawable(this@SelectGenderActivity, R.drawable.bg_normal)
            mDatabind.womanTv.setTextColor(AppCompatResources.getColorStateList(this@SelectGenderActivity,R.color.color_1c1c1c))
        }

        fun selectWoMan() {
            mViewModel.sex.set(SEX.WOMAN)
            mDatabind.womanSelectedIv.visibility = View.VISIBLE
            mDatabind.woman.background = AppCompatResources.getDrawable(this@SelectGenderActivity, R.drawable.bg_selected)
            mDatabind.womanTv.setTextColor(AppCompatResources.getColorStateList(this@SelectGenderActivity,R.color.white))
            mDatabind.manSelectedIv.visibility = View.GONE
            mDatabind.man.background = AppCompatResources.getDrawable(this@SelectGenderActivity, R.drawable.bg_normal)
            mDatabind.mainTv.setTextColor(AppCompatResources.getColorStateList(this@SelectGenderActivity,R.color.color_1c1c1c))

        }
    }

}