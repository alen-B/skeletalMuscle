package com.fjp.skeletalmuscle.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.databinding.ActivitySettingBinding
import com.fjp.skeletalmuscle.viewmodel.state.SettingViewModel

class SettingActivity : BaseActivity<SettingViewModel, ActivitySettingBinding>() {
    private val USERINFO = 0
    private val DEVICE = 1
    private val SYSTEMS_ETTING = 2
    private val EXPORT_REPORT = 3
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.setting_title))
        mViewModel.userInfo.set(App.userInfo)
    }


    inner class ProxyClick {

        fun clickUserInfo() {
            mDatabind.userinfoTv.isEnabled = false
            mDatabind.deviceLinkTv.isEnabled = true
            mDatabind.sysSettingTv.isEnabled = true
            mDatabind.exportReportTv.isEnabled = true
            changeFragment(USERINFO)
        }

        fun clickDeviceLink() {
            mDatabind.userinfoTv.isEnabled = true
            mDatabind.deviceLinkTv.isEnabled = false
            mDatabind.sysSettingTv.isEnabled = true
            mDatabind.exportReportTv.isEnabled = true
            changeFragment(DEVICE)
        }

        fun clickSysSetting() {
            mDatabind.userinfoTv.isEnabled = true
            mDatabind.deviceLinkTv.isEnabled = true
            mDatabind.sysSettingTv.isEnabled = false
            mDatabind.exportReportTv.isEnabled = true
            changeFragment(SYSTEMS_ETTING)
        }

        fun clickExportReport() {
            mDatabind.userinfoTv.isEnabled = true
            mDatabind.deviceLinkTv.isEnabled = true
            mDatabind.sysSettingTv.isEnabled = true
            mDatabind.exportReportTv.isEnabled = false
            changeFragment(EXPORT_REPORT)
        }

        fun clickFinish() {
            this@SettingActivity.finish()
        }
    }

    private fun changeFragment(exportReport: Int) {
        val fragment = when (exportReport) {
            USERINFO -> UserInfoFragment.newInstance()
            DEVICE -> DeviceFragment.newInstance()
            SYSTEMS_ETTING -> SystemSettingFragment.newInstance()
            else -> ExportReportFragment.newInstance()
        }
        try {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.rightFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }catch (e:Exception){
            println(e.message)
        }


    }

    override fun onResume() {
        super.onResume()
        mDatabind.blurLayout.startBlur()
    }

    override fun onPause() {
        super.onPause()
        mDatabind.blurLayout.pauseBlur()
    }

}