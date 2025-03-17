package com.fjp.skeletalmuscle.ui.deviceconnectguide

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DeviceType
import com.fjp.skeletalmuscle.app.util.SMBleManager
import com.fjp.skeletalmuscle.app.weight.pop.AssessmentDeviceOffLinePop
import com.fjp.skeletalmuscle.app.weight.pop.DumbbellDeviceOffLinePop
import com.fjp.skeletalmuscle.app.weight.pop.HighKneeDeviceOffLinePop
import com.fjp.skeletalmuscle.app.weight.pop.PlankDeviceOffLinePop
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityDeviceConnectGuideBinding
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep1Fragment
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep2Fragment
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep4Fragment
import com.fjp.skeletalmuscle.ui.deviceconnectguide.fragment.HighKneeGuideStep6Fragment
import com.fjp.skeletalmuscle.viewmodel.state.DeviceConnectViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

class DeviceConnectGuideActivity : BaseActivity<DeviceConnectViewModel, ActivityDeviceConnectGuideBinding>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DeviceConnectGuideActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var fragments = arrayListOf<Fragment>(HighKneeGuideStep1Fragment.newInstance(), HighKneeGuideStep2Fragment.newInstance(), HighKneeGuideStep4Fragment.newInstance(), HighKneeGuideStep6Fragment.newInstance())
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        if (App.sportsType == SportsType.ASSESSMENT) {
            fragments = arrayListOf(HighKneeGuideStep4Fragment.newInstance(), HighKneeGuideStep6Fragment.newInstance())
        } else if (App.sportsType == SportsType.PLANK) {
            fragments = arrayListOf(HighKneeGuideStep2Fragment.newInstance())
        }
        mDatabind.viewpager.init(supportFragmentManager, lifecycle, fragments, false)
    }

    fun setNextButtonEnable(enable: Boolean) {
        mViewModel.nextButtonIsEnable.set(enable)
    }

    fun setNextButtonText(text: String) {
        mViewModel.nextButtonText.set(text)
    }

    private fun connectedDevice(type: DeviceType) {
        if (type == DeviceType.GTS) {
            SMBleManager.connectedDevices[DeviceType.GTS]?.let { SMBleManager.subscribeToNotifications(it, Constants.GTS_UUID_SERVICE, Constants.GTS_UUID_CHARACTERISTIC_WRITE) }
        } else if (type == DeviceType.LEFT_LEG) {
            SMBleManager.connectedDevices[DeviceType.LEFT_LEG]?.let { SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_CHARACTERISTIC_WRITE) }
        } else if (type == DeviceType.RIGHT_LEG) {
            SMBleManager.connectedDevices[DeviceType.RIGHT_LEG]?.let { SMBleManager.subscribeToNotifications(it, Constants.LEG_UUID_SERVICE, Constants.LEG__UUID_CHARACTERISTIC_WRITE) }
        }
    }

    inner class ProxyClick {
        fun clickFinish() {
            this@DeviceConnectGuideActivity.finish()
        }

        fun clickNext() {
            if (mDatabind.viewpager.currentItem == mDatabind.viewpager.adapter!!.itemCount - 1) {
                when (App.sportsType) {
                    SportsType.HIGH_KNEE -> {
                        showHighKneeOffLinePop()
                    }

                    SportsType.DUMBBELL -> {
                        showDumbbellOffLinePop()
                    }

                    SportsType.PLANK -> {
                        showPlankOffLinePop()
                    }

                    SportsType.ASSESSMENT -> {
                        showAssessmentOffLinePop()
                    }

                    else -> {}
                }
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

        private fun showHighKneeOffLinePop() {
            val highKneeDeviceOffLinePop = HighKneeDeviceOffLinePop(this@DeviceConnectGuideActivity, object : HighKneeDeviceOffLinePop.Listener {
                override fun reconnect(type: DeviceType) {
                    connectedDevice(type)
                }

                override fun completed() {
                    finish()
                }
            })
            showPop(highKneeDeviceOffLinePop)
        }

    }

    private fun showPlankOffLinePop() {
        val plankDeviceOffLinePop = PlankDeviceOffLinePop(this@DeviceConnectGuideActivity, object : PlankDeviceOffLinePop.Listener {
            override fun reconnect(type: DeviceType) {
                connectedDevice(type)
            }

            override fun completed() {
                finish()
            }
        })
        showPop(plankDeviceOffLinePop)
    }

    private fun showDumbbellOffLinePop() {
        val dumbbellDeviceOffLinePop = DumbbellDeviceOffLinePop(this@DeviceConnectGuideActivity, object : DumbbellDeviceOffLinePop.Listener {
            override fun reconnect(type: DeviceType) {
                connectedDevice(type)
            }

            override fun completed() {
                finish()
            }
        })
        showPop(dumbbellDeviceOffLinePop)
    }

    private fun showAssessmentOffLinePop() {
        val assessmentDeviceOffLinePop = AssessmentDeviceOffLinePop(this@DeviceConnectGuideActivity, object : AssessmentDeviceOffLinePop.Listener {
            override fun reconnect(type: DeviceType) {
                connectedDevice(type)
            }

            override fun completed() {
                finish()
            }
        })
        showPop(assessmentDeviceOffLinePop)

    }

    private fun showPop(popView: BasePopupView) {
        val pop = XPopup.Builder(this@DeviceConnectGuideActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(popView)
        pop.show()
    }
}