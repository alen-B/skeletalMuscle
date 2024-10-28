package com.fjp.skeletalmuscle.ui.main

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityExercisePlanBinding
import com.fjp.skeletalmuscle.viewmodel.state.ExercisePlanViewModel
import me.hgj.jetpackmvvm.base.appContext

class ExercisePlanActivity : BaseActivity<ExercisePlanViewModel, ActivityExercisePlanBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.today_sports_title))
        mViewModel.leftImg.set(R.drawable.title_icon_sports_record)
        findViewById<TextView>(R.id.titleTv).setTextColor(ContextCompat.getColor(appContext, R.color.white))
        when (intent.getIntExtra(Constants.INTENT_SPORTS_TYPE, SportsType.DUMBBELL.type)) {
            SportsType.LEG_LIFT.type -> {
                mDatabind.device2Iv.setImageBitmap((ContextCompat.getDrawable(appContext, R.drawable.knee_left) as BitmapDrawable).bitmap)
                mDatabind.device3Iv.setImageBitmap((ContextCompat.getDrawable(appContext, R.drawable.knee_right) as BitmapDrawable).bitmap)
                mDatabind.exercisePlanSportsLegTimeCl.visibility = View.VISIBLE
                mDatabind.exercisePlanSportsLegAngleCl.visibility = View.VISIBLE
                mViewModel.sportsType.set(getString(R.string.today_sports_data_type1))
            }

            SportsType.DUMBBELL.type -> {
                mViewModel.sportsIcon.set(R.drawable.exercise_plan_dumbbell)
                mViewModel.sportsType.set(getString(R.string.today_sports_data_type2))
                mDatabind.exercisePlanSportsTimeCl.visibility = View.VISIBLE
                mDatabind.sportsWeightCl.visibility = View.VISIBLE
                mDatabind.sportsUpliftCl.visibility = View.VISIBLE
                mDatabind.sportsExpandChestCl.visibility = View.VISIBLE
            }

            else -> {
                mViewModel.sportsType.set(getString(R.string.today_sports_data_type3))
            }
        }
    }

    inner class ProxyClick {

        fun clickStartSports() {

        }

        fun clickFinish() {
            this@ExercisePlanActivity.finish()
        }

        fun clickSportsTimeSub() {
            if (mViewModel.sportsTime.get()!!.toInt() == 1) {
                return
            }
            mViewModel.sportsTime.set((mViewModel.sportsTime.get()!!.toInt() - 1).toString())
        }

        fun clickSportsTimeAdd() {
            mViewModel.sportsTime.set((mViewModel.sportsTime.get()!!.toInt() + 1).toString())
        }

        fun clickSportsWeightSub() {
            if (mViewModel.sportsWeight.get()!!.toInt() == 1) {
                return
            }
            mViewModel.sportsWeight.set((mViewModel.sportsWeight.get()!!.toInt() - 1).toString())
        }

        fun clickSportsWeightAdd() {
            mViewModel.sportsWeight.set((mViewModel.sportsWeight.get()!!.toInt() + 1).toString())
        }

        fun clickSportsUpliftSub() {
            if (mViewModel.sportsUplift.get()!!.toInt() == 1) {
                return
            }
            mViewModel.sportsUplift.set((mViewModel.sportsUplift.get()!!.toInt() - 1).toString())
        }

        fun clickSportsUpliftAdd() {
            mViewModel.sportsUplift.set((mViewModel.sportsUplift.get()!!.toInt() + 1).toString())
        }

        fun clickSportsExpandChestSub() {
            if (mViewModel.sportsExpandChest.get()!!.toInt() == 1) {
                return
            }
            mViewModel.sportsExpandChest.set((mViewModel.sportsExpandChest.get()!!.toInt() - 1).toString())
        }

        fun clickSportsExpandChestAdd() {
            mViewModel.sportsAngle.set((mViewModel.sportsAngle.get()!!.toInt() + 1).toString())
        }

        fun clickSportsAngleSub() {
            if (mViewModel.sportsAngle.get()!!.toInt() == 45) {
                return
            }
            mViewModel.sportsAngle.set((mViewModel.sportsAngle.get()!!.toInt() - 1).toString())
        }

        fun clickSportsAngleAdd() {
            if (mViewModel.sportsAngle.get()!!.toInt() == 120) {
                return
            }
            mViewModel.sportsAngle.set((mViewModel.sportsAngle.get()!!.toInt() + 1).toString())
        }


    }

}