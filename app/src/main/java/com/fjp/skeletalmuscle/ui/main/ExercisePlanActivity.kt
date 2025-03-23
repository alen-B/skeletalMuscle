package com.fjp.skeletalmuscle.ui.main

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.app.weight.pop.VideoPop
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityExercisePlanBinding
import com.fjp.skeletalmuscle.ui.deviceconnectguide.DeviceConnectGuideActivity
import com.fjp.skeletalmuscle.ui.sports.DumbbellMainActivity
import com.fjp.skeletalmuscle.ui.sports.HighKneeMainActivity
import com.fjp.skeletalmuscle.ui.sports.PlankActivity
import com.fjp.skeletalmuscle.viewmodel.state.ExercisePlanViewModel
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.base.appContext

class ExercisePlanActivity : BaseActivity<ExercisePlanViewModel, ActivityExercisePlanBinding>(), SMBleManager.DeviceListener {
    private val SPORTS_TIME_MIN_LEG_KNEE = 2
    private val SPORTS_TIME_MIN_DUMBBELL = 2
    private val SPORTS_TIME_MIN_PLANK = 1
    private val SPORTS_TIME_MAX_LEG_KNEE = 60
    private val SPORTS_TIME_MAX_DUMBBELL = 60
    private val SPORTS_TIME_MAX_PLANK = 30
    private val WEIGHT_MAX = 5
    private val WEIGHT_MIN = 1

    private val GROUP_MAX = 30
    private val GROUP_MIN = 2

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.exercise_plan_title))
        mViewModel.leftImg.set(R.drawable.title_icon_sports_record)
        findViewById<TextView>(R.id.titleTv).setTextColor(ContextCompat.getColor(appContext, R.color.white))
        SMBleManager.addDeviceResultDataListener(this)
        App.sportsTime = mViewModel.sportsTime.get()!!.toInt()
        App.expandChestAccount = mViewModel.sportsUplift.get()!!.toInt()
        App.upliftAccount = mViewModel.sportsExpandChest.get()!!.toInt()
    }

    override fun createObserver() {
        super.createObserver()
        eventViewModel.startSports.observeInActivity(this) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        setSportsType()
    }

    private fun setSportsType() {
        when (App.sportsType) {
            SportsType.HIGH_KNEE -> {

                mDatabind.device2Iv.setImageBitmap((ContextCompat.getDrawable(appContext, R.drawable.knee_left) as BitmapDrawable).bitmap)
                mDatabind.device3Iv.setImageBitmap((ContextCompat.getDrawable(appContext, R.drawable.knee_right) as BitmapDrawable).bitmap)
                mDatabind.exercisePlanSportsLegTimeCl.visibility = View.VISIBLE
                mDatabind.exercisePlanSportsLegAngleCl.visibility = View.VISIBLE
                mViewModel.sportsType.set(getString(R.string.today_sports_data_type1))
                mDatabind.device2Tv.text = getString(R.string.exercise_plan_left_knee)
                mDatabind.device3Tv.text = getString(R.string.exercise_plan_right_knee)
                if (SMBleManager.connectedDevices[DeviceType.GTS] != null && SMBleManager.connectedDevices[DeviceType.LEFT_LEG] != null && SMBleManager.connectedDevices[DeviceType.RIGHT_LEG] != null) {
                    mDatabind.shareBtn.isEnabled = true
                    mDatabind.deviceLinkTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.exercise_device_linked), null, null, null)
                }
            }

            SportsType.DUMBBELL -> {
//                mViewModel.sportsIcon.set(R.drawable.exercise_plan_dumbbell)
                mViewModel.sportsType.set(getString(R.string.today_sports_data_type2))
                mDatabind.exercisePlanSportsTimeCl.visibility = View.VISIBLE
                mDatabind.sportsWeightCl.visibility = View.VISIBLE
                mDatabind.sportsUpliftCl.visibility = View.VISIBLE
                mDatabind.sportsExpandChestCl.visibility = View.VISIBLE
                if (SMBleManager.connectedDevices[DeviceType.GTS] != null && SMBleManager.connectedDevices[DeviceType.LEFT_LEG] != null && SMBleManager.connectedDevices[DeviceType.RIGHT_LEG] != null) {
                    mDatabind.shareBtn.isEnabled = true
                    mDatabind.deviceLinkTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.exercise_device_linked), null, null, null)
                }
            }

            SportsType.PLANK -> {
                mDatabind.device2Iv.visibility = View.GONE
                mDatabind.device3Iv.visibility = View.GONE
                mDatabind.device2Tv.visibility = View.GONE
                mDatabind.device3Tv.visibility = View.GONE
                mDatabind.exercisePlanSportsLegTimeCl.visibility = View.VISIBLE
                mViewModel.sportsType.set(getString(R.string.today_sports_data_type3))
                if (SMBleManager.connectedDevices[DeviceType.GTS] != null) {
                    mDatabind.shareBtn.isEnabled = true
                    mDatabind.deviceLinkTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.exercise_device_linked), null, null, null)
                }
            }

            else -> {}
        }
    }


    // 在连接成功后调用这个方法订阅notify


    inner class ProxyClick {

        fun clickStartSports() {
            showVideoPop()
//            if (App.sportsType == SportsType.HIGH_KNEE) {
//                val intent = Intent(this@ExercisePlanActivity, HighKneeMainActivity::class.java)
//                startActivity(intent)
//
//            } else if (App.sportsType == SportsType.DUMBBELL) {
//                val intent = Intent(this@ExercisePlanActivity, DumbbellMainActivity::class.java)
//                startActivity(intent)
//            } else if (App.sportsType == SportsType.PLANK) {
//                val intent = Intent(this@ExercisePlanActivity, PlankActivity::class.java)
//                startActivity(intent)
//            }
//            eventViewModel.startSports.postValue(true)
        }

        fun clickStartDeviceGuide() {
            DeviceConnectGuideActivity.start(this@ExercisePlanActivity)
        }

        fun clickFinish() {
            this@ExercisePlanActivity.finish()
        }

        fun clickSportsTimeSub() {
            if (App.sportsType == SportsType.HIGH_KNEE && mViewModel.sportsTime.get()!!.toInt() == SPORTS_TIME_MIN_LEG_KNEE) {
                return
            }
            if (App.sportsType == SportsType.DUMBBELL && mViewModel.sportsTime.get()!!.toInt() == SPORTS_TIME_MIN_DUMBBELL) {
                return
            }
            if (App.sportsType == SportsType.PLANK && mViewModel.sportsTime.get()!!.toInt() == SPORTS_TIME_MIN_PLANK) {
                return
            }

            mViewModel.sportsTime.set((mViewModel.sportsTime.get()!!.toInt() - 1).toString())
            App.sportsTime = mViewModel.sportsTime.get()!!.toInt()
        }

        fun clickSportsTimeAdd() {
            if (App.sportsType == SportsType.HIGH_KNEE && mViewModel.sportsTime.get()!!.toInt() == SPORTS_TIME_MAX_LEG_KNEE) {
                return
            }
            if (App.sportsType == SportsType.DUMBBELL && mViewModel.sportsTime.get()!!.toInt() == SPORTS_TIME_MAX_DUMBBELL) {
                return
            }
            if (App.sportsType == SportsType.PLANK && mViewModel.sportsTime.get()!!.toInt() == SPORTS_TIME_MAX_PLANK) {
                return
            }
            mViewModel.sportsTime.set((mViewModel.sportsTime.get()!!.toInt() + 1).toString())
            App.sportsTime = mViewModel.sportsTime.get()!!.toInt()
        }

        fun clickSportsWeightSub() {
            if (mViewModel.sportsWeight.get()!!.toInt() == 1) {
                return
            }
            mViewModel.sportsWeight.set((mViewModel.sportsWeight.get()!!.toInt() - 1).toString())
        }

        fun clickSportsWeightAdd() {
            if (mViewModel.sportsWeight.get()!!.toInt() == 5) {
                return
            }
            mViewModel.sportsWeight.set((mViewModel.sportsWeight.get()!!.toInt() + 1).toString())
        }

        fun clickSportsUpliftSub() {
            if (mViewModel.sportsUplift.get()!!.toInt() == GROUP_MIN) {
                return
            }
            mViewModel.sportsUplift.set((mViewModel.sportsUplift.get()!!.toInt() - 1).toString())
            App.upliftAccount =  mViewModel.sportsUplift.get()!!.toInt()
        }

        fun clickSportsUpliftAdd() {
            if (mViewModel.sportsUplift.get()!!.toInt() == GROUP_MAX) {
                return
            }
            mViewModel.sportsUplift.set((mViewModel.sportsUplift.get()!!.toInt() + 1).toString())
            App.upliftAccount =  mViewModel.sportsUplift.get()!!.toInt()
        }

        fun clickSportsExpandChestSub() {
            if (mViewModel.sportsExpandChest.get()!!.toInt() == GROUP_MIN) {
                return
            }

            mViewModel.sportsExpandChest.set((mViewModel.sportsExpandChest.get()!!.toInt() - 1).toString())
            App.expandChestAccount =  mViewModel.sportsExpandChest.get()!!.toInt()
        }

        fun clickSportsExpandChestAdd() {
            if (mViewModel.sportsExpandChest.get()!!.toInt() == GROUP_MAX) {
                return
            }
            mViewModel.sportsExpandChest.set((mViewModel.sportsExpandChest.get()!!.toInt() + 1).toString())
            App.expandChestAccount =  mViewModel.sportsExpandChest.get()!!.toInt()
        }

        fun clickSportsAngleSub() {
            if (mViewModel.sportsAngle.get()!!.toInt() == 45) {
                return
            }
            mViewModel.sportsAngle.set((mViewModel.sportsAngle.get()!!.toInt() - 1).toString())
            App.legAngle = mViewModel.sportsAngle.get()!!.toInt()
        }

        fun clickSportsAngleAdd() {
            if (mViewModel.sportsAngle.get()!!.toInt() == 120) {
                return
            }
            mViewModel.sportsAngle.set((mViewModel.sportsAngle.get()!!.toInt() + 1).toString())
            App.legAngle = mViewModel.sportsAngle.get()!!.toInt()
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

    override fun leftHandGripsConnected() {
        mDatabind.device1Iv.setBackgroundResource(R.drawable.bg_white_round_20)
    }

    override fun rightHandGripsConnected() {
        mDatabind.device1Iv.setBackgroundResource(R.drawable.bg_white_round_20)
    }

    override fun onLeftDeviceData(data: ByteArray) {
    }

    override fun onRightDeviceData(data: ByteArray) {
    }

    override fun onGTSData(data: ByteArray) {
    }

    override fun onLeftHandGripsData(data: ByteArray) {
    }

    override fun onRightHandGripsData(data: ByteArray) {
    }

    fun showVideoPop() {
        val videoPop = VideoPop(this@ExercisePlanActivity, object : VideoPop.Listener {
            override fun jump(pop: VideoPop) {
                finish()
                if (App.sportsType == SportsType.HIGH_KNEE) {
                    startActivity(Intent(this@ExercisePlanActivity, HighKneeMainActivity::class.java))
                } else if (App.sportsType == SportsType.DUMBBELL) {
                    startActivity(Intent(this@ExercisePlanActivity, DumbbellMainActivity::class.java))
                } else if (App.sportsType == SportsType.PLANK) {
                    startActivity(Intent(this@ExercisePlanActivity, PlankActivity::class.java))
                }
                eventViewModel.startSports.postValue(true)
                pop.dismiss()
                finish()
            }


        })
        val pop = XPopup.Builder(this@ExercisePlanActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(videoPop)

        pop.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        SMBleManager.delDeviceResultDataListener(this)
    }


}