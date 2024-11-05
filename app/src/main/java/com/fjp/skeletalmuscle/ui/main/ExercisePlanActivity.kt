package com.fjp.skeletalmuscle.ui.main

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.clj.fastble.data.BleDevice
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.common.DeviceType
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityExercisePlanBinding
import com.fjp.skeletalmuscle.ui.deviceguidepage.HighKneeGuideActivity
import com.fjp.skeletalmuscle.ui.highKnee.HighKneeMainActivity
import com.fjp.skeletalmuscle.utils.SMBleManager
import com.fjp.skeletalmuscle.viewmodel.state.ExercisePlanViewModel
import me.hgj.jetpackmvvm.base.appContext

class ExercisePlanActivity : BaseActivity<ExercisePlanViewModel, ActivityExercisePlanBinding>(), SMBleManager.DeviceListener {

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.today_sports_title))
        mViewModel.leftImg.set(R.drawable.title_icon_sports_record)
        findViewById<TextView>(R.id.titleTv).setTextColor(ContextCompat.getColor(appContext, R.color.white))
        setSportsType()
        SMBleManager.addDeviceResultDataListener(this)

    }

    private fun setSportsType() {
        when (intent.getIntExtra(Constants.INTENT_SPORTS_TYPE, SportsType.DUMBBELL.type)) {
            SportsType.LEG_LIFT.type -> {
                mDatabind.device2Iv.setImageBitmap((ContextCompat.getDrawable(appContext, R.drawable.knee_left) as BitmapDrawable).bitmap)
                mDatabind.device3Iv.setImageBitmap((ContextCompat.getDrawable(appContext, R.drawable.knee_right) as BitmapDrawable).bitmap)
                mDatabind.exercisePlanSportsLegTimeCl.visibility = View.VISIBLE
                mDatabind.exercisePlanSportsLegAngleCl.visibility = View.VISIBLE
                mViewModel.sportsType.set(getString(R.string.today_sports_data_type1))
                mDatabind.device2Tv.text=getString(R.string.exercise_plan_left_knee)
                mDatabind.device3Tv.text=getString(R.string.exercise_plan_right_knee)
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


    // 在连接成功后调用这个方法订阅notify


    inner class ProxyClick {

        fun clickStartSports() {
            startActivity(Intent(this@ExercisePlanActivity, HighKneeMainActivity::class.java))
        }

        fun clickStartDeviceGuide(){
            startActivity(Intent(this@ExercisePlanActivity, HighKneeGuideActivity::class.java))
        }
        fun clickDevice1() {
            SMBleManager.scanDevices(DeviceType.GTS.value,DeviceType.GTS, object:SMBleManager.DeviceStatusListener{
                override fun disConnected() {
                }

                override fun connected() {
//                    SMBleManager.connectedDevices[DeviceType.GTS]?.let {
//                        SMBleManager.subscribeToNotifications(it,Constants.GTS_UUID_SERVICE,Constants.GTS_UUID_CHARACTERISTIC_WRITE)
//                    }
                    mDatabind.device1Iv.setBackgroundResource(R.drawable.bg_selected)
                }

            })
        }
        fun clickDevice2() {
            SMBleManager.scanDevices(DeviceType.LEFT_LEG.value,DeviceType.LEFT_LEG,object:SMBleManager.DeviceStatusListener{
                override fun disConnected() {
                }

                override fun connected() {
                    mDatabind.device2Iv.setBackgroundResource(R.drawable.bg_selected)
                }

            })
        }
        fun clickDevice3() {
            SMBleManager.scanDevices(DeviceType.RIGHT_LEG.value,DeviceType.RIGHT_LEG,object:SMBleManager.DeviceStatusListener{
                override fun disConnected() {
                }

                override fun connected() {
                    mDatabind.device3Iv.setBackgroundResource(R.drawable.bg_selected)
                }

            })
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


    override fun GTSDisConnected() {
        mDatabind.device1Iv.setBackgroundResource(R.drawable.bg_white_round_20)
    }

    override fun leftLegDisConnected() {
        mDatabind.device2Iv.setBackgroundResource(R.drawable.bg_white_round_20)
    }

    override fun rightLegDisConnected() {
        mDatabind.device3Iv.setBackgroundResource(R.drawable.bg_white_round_20)
    }

    override fun onLeftLegData(data: ByteArray) {
    }

    override fun onRightLegData(data: ByteArray) {
    }

    override fun onGTSData(data: ByteArray) {
    }

    override fun onDestroy() {
        super.onDestroy()
        SMBleManager.delDeviceResultDataListener(this)
    }

}