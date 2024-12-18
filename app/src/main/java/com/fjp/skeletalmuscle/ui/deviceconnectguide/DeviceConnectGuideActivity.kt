package com.fjp.skeletalmuscle.ui.deviceconnectguide

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.app.weight.pop.DeviceOffLinePop
import com.fjp.skeletalmuscle.app.weight.pop.VideoPop
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityDeviceConnectGuideBinding
import com.fjp.skeletalmuscle.ui.assessment.SelectedWaistlineAndWeightActivity
import com.fjp.skeletalmuscle.ui.assessment.SportsAssessmentActivity
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep1Fragment
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep2Fragment
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep4Fragment
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep6Fragment
import com.fjp.skeletalmuscle.ui.sports.DumbbellMainActivity
import com.fjp.skeletalmuscle.ui.sports.HighKneeMainActivity
import com.fjp.skeletalmuscle.ui.sports.PlankActivity
import com.fjp.skeletalmuscle.viewmodel.state.DeviceConnectViewModel
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.util.get

class DeviceConnectGuideActivity : BaseActivity<DeviceConnectViewModel, ActivityDeviceConnectGuideBinding>() {
    var psortsType: Int = Constants.CONNECT_DEVICE_TYPE_EXERCISE

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
        psortsType = intent.get(Constants.INTENT_KEY_CONNECT_DEVICE_TYPE, Constants.CONNECT_DEVICE_TYPE_EXERCISE)!!
        if (psortsType != Constants.CONNECT_DEVICE_TYPE_EXERCISE) {
            fragments = arrayListOf(HighKneeGuideStep4Fragment.newInstance(), HighKneeGuideStep6Fragment.newInstance())
        }
        if(App.sportsType == SportsType.PLANK){
            fragments= arrayListOf(HighKneeGuideStep2Fragment.newInstance())
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
                showOffLinePop()
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

    fun showOffLinePop() {
        var type = SportsType.HIGH_KNEE
        if(psortsType== Constants.CONNECT_DEVICE_TYPE_ASSESSMENT){
            type =SportsType.ASSESSMENT
        }else{
            type = App.sportsType
        }
        val deviceOffLinePop = DeviceOffLinePop(this@DeviceConnectGuideActivity,type ,object : DeviceOffLinePop.Listener {
            override fun reconnect(type: DeviceType) {
                if (type == DeviceType.GTS) {
                    SMBleManager.connectedDevices[DeviceType.GTS]?.let { SMBleManager.subscribeToNotifications(it, Constants.GTS_UUID_SERVICE, Constants.GTS_UUID_CHARACTERISTIC_WRITE) }
                } else if (type == DeviceType.LEFT_LEG) {
                    SMBleManager.connectedDevices[DeviceType.LEFT_LEG]?.let { SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_CHARACTERISTIC_WRITE) }
                } else if (type == DeviceType.RIGHT_LEG) {
                    SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]?.let { SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_CHARACTERISTIC_WRITE) }
                }

            }

            override fun completed() {
                finish()
            }
        })
        val pop = XPopup.Builder(this@DeviceConnectGuideActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(deviceOffLinePop)
        pop.show()
    }

}