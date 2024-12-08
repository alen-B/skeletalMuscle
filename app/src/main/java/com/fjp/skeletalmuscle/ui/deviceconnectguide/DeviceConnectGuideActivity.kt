package com.fjp.skeletalmuscle.ui.deviceconnectguide

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.weight.pop.VideoPop
import com.fjp.skeletalmuscle.databinding.ActivityDeviceConnectGuideBinding
import com.fjp.skeletalmuscle.ui.assessment.SelectedWaistlineAndWeightActivity
import com.fjp.skeletalmuscle.ui.assessment.SportsAssessmentActivity
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep1Fragment
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep2Fragment
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep4Fragment
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep6Fragment
import com.fjp.skeletalmuscle.ui.sports.HighKneeMainActivity
import com.fjp.skeletalmuscle.viewmodel.state.DeviceConnectViewModel
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.util.get

class DeviceConnectGuideActivity : BaseActivity<DeviceConnectViewModel, ActivityDeviceConnectGuideBinding>() {
    var type: Int = Constants.CONNECT_DEVICE_TYPE_EXERCISE

    companion object {
        fun start(context: Context, type: Int) {
            val intent = Intent(context, DeviceConnectGuideActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_CONNECT_DEVICE_TYPE, type)
            context.startActivity(intent)
        }
    }

    private var fragments = arrayListOf<Fragment>(HighKneeGuideStep1Fragment.newInstance(), HighKneeGuideStep2Fragment.newInstance(), HighKneeGuideStep4Fragment.newInstance(), HighKneeGuideStep6Fragment.newInstance())
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        type = intent.get(Constants.INTENT_KEY_CONNECT_DEVICE_TYPE, Constants.CONNECT_DEVICE_TYPE_EXERCISE)!!
        if (type != Constants.CONNECT_DEVICE_TYPE_EXERCISE) {
            fragments = arrayListOf<Fragment>(HighKneeGuideStep4Fragment.newInstance(), HighKneeGuideStep6Fragment.newInstance())
        }
        mDatabind.viewpager.init(supportFragmentManager, lifecycle, fragments, false)
    }

    fun setNextButtonEnable(enable: Boolean) {
        mViewModel.nextButtonIsEnable.set(enable)
    }

    fun setNextButtonText(text: String) {
        mViewModel.nextButtonText.set(text)
    }

    inner class ProxyClick {
        fun clickFinish() {
            this@DeviceConnectGuideActivity.finish()
        }

        fun clickNext() {
            if (mDatabind.viewpager.currentItem == mDatabind.viewpager.adapter!!.itemCount - 1) {
                if (type == Constants.CONNECT_DEVICE_TYPE_EXERCISE) {
                    showVideoPop()
                } else {
                    startActivity(Intent(this@DeviceConnectGuideActivity, SelectedWaistlineAndWeightActivity::class.java))
                    finish()
                }

                return
            }
            mDatabind.viewpager.setCurrentItem(mDatabind.viewpager.currentItem + 1, true)
        }

        fun clickPre() {
            if (mDatabind.viewpager.currentItem == 0) {
                finish()
                return
            }
            mDatabind.viewpager.setCurrentItem(mDatabind.viewpager.currentItem - 1, true)
        }

    }

    fun showVideoPop() {
        val videoPop = VideoPop(this@DeviceConnectGuideActivity, object : VideoPop.Listener {
            override fun jump(pop: VideoPop) {
                val intent = Intent(this@DeviceConnectGuideActivity, HighKneeMainActivity::class.java)
                startActivity(intent)
                eventViewModel.startSports.postValue(true)

                finish()
                pop.dismiss()
            }


        })
        val pop = XPopup.Builder(this@DeviceConnectGuideActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(videoPop)

        pop.show()
    }
}