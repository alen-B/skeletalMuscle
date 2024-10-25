package com.fjp.skeletalmuscle.ui.main

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.databinding.ActivityTodaySelectSportsBinding
import com.fjp.skeletalmuscle.utils.AnimUtil
import com.fjp.skeletalmuscle.viewmodel.state.TodaySelectSuportsViewModel
import me.hgj.jetpackmvvm.base.appContext


class TodaySelectSportsActivity :BaseActivity<TodaySelectSuportsViewModel,ActivityTodaySelectSportsBinding>(){
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.today_sports_title))
        mViewModel.leftImg.set(R.drawable.title_icon_sports_record)
        findViewById<TextView>(R.id.titleTv).setTextColor(ContextCompat.getColor(appContext,R.color.white))
    }

    inner class ProxyClick{
        fun clickFinish(){
            this@TodaySelectSportsActivity.finish()
        }

        fun clickStartSports(){
            showToast("开启运动")
        }
        fun clickHeightLeg(){
            if(mDatabind.legDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.legDetailCl,mDatabind.legIv)
            }else{
                rotateAndSwitchViews(mDatabind.legIv,mDatabind.legDetailCl)
            }

            if(mDatabind.dumbbellCl.isVisible) {
                rotateAndSwitchViews(mDatabind.dumbbellCl, mDatabind.dumbbellIv)
            }

            if(mDatabind.plankDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.plankDetailCl,mDatabind.plankIv)
            }
        }
        fun clickDumbbell(){
            if(mDatabind.dumbbellCl.isVisible){
                rotateAndSwitchViews(mDatabind.dumbbellCl,mDatabind.dumbbellIv)
            }else{
                rotateAndSwitchViews(mDatabind.dumbbellIv,mDatabind.dumbbellCl)
            }
            if(mDatabind.legDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.legDetailCl,mDatabind.legIv)
            }
            if(mDatabind.plankDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.plankDetailCl,mDatabind.plankIv)
            }
        }
        fun clickPlank(){
            if(mDatabind.plankDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.plankDetailCl,mDatabind.plankIv)
            }else{
                rotateAndSwitchViews(mDatabind.plankIv,mDatabind.plankDetailCl)
            }
            if(mDatabind.legDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.legDetailCl,mDatabind.legIv)
            }
            if(mDatabind.dumbbellCl.isVisible){
                rotateAndSwitchViews(mDatabind.dumbbellCl,mDatabind.dumbbellIv)
            }
        }
    }

    private fun rotateAndSwitchViews( iv: View, cl: View) {
        AnimUtil.flipAnimatorYViewShow(iv,cl,500)
    }

}