package com.fjp.skeletalmuscle.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.data.model.bean.SportsType
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
            if(mViewModel.sportsType.get()==null){
                showToast(getString(R.string.today_sports_start))
                return
            }
            val intent = Intent(this@TodaySelectSportsActivity,ExercisePlanActivity::class.java)
            intent.putExtra(Constants.INTENT_SPORTS_TYPE,mViewModel.sportsType.get()?.type)
            startActivity(intent)
        }
        fun clickHeightLeg(){
            if(mDatabind.legDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.legDetailCl,mDatabind.legIv)
            }else{
                mViewModel.sportsType.set(SportsType.LEG_LIFT)
                rotateAndSwitchViews(mDatabind.legIv,mDatabind.legDetailCl)
            }

            if(mDatabind.dumbbellCl.isVisible) {
                rotateAndSwitchViews(mDatabind.dumbbellCl, mDatabind.dumbbellIv)
            }

            if(mDatabind.pushUpDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.pushUpDetailCl,mDatabind.plankIv)
            }
        }
        fun clickDumbbell(){
            if(mDatabind.dumbbellCl.isVisible){
                rotateAndSwitchViews(mDatabind.dumbbellCl,mDatabind.dumbbellIv)
            }else{
                mViewModel.sportsType.set(SportsType.DUMBBELL)
                rotateAndSwitchViews(mDatabind.dumbbellIv,mDatabind.dumbbellCl)
            }
            if(mDatabind.legDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.legDetailCl,mDatabind.legIv)
            }
            if(mDatabind.pushUpDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.pushUpDetailCl,mDatabind.plankIv)
            }
        }
        fun clickPushUp(){
            if(mDatabind.pushUpDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.pushUpDetailCl,mDatabind.plankIv)
            }else{
                mViewModel.sportsType.set(SportsType.PUSH_UP)
                rotateAndSwitchViews(mDatabind.plankIv,mDatabind.pushUpDetailCl)
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