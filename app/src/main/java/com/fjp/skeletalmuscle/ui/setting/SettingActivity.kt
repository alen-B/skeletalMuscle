package com.fjp.skeletalmuscle.ui.setting

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.data.model.bean.Account
import com.fjp.skeletalmuscle.databinding.ActivitySettingBinding
import com.fjp.skeletalmuscle.ui.login.LoginActivity
import com.fjp.skeletalmuscle.ui.setting.fragment.DeviceFragment
import com.fjp.skeletalmuscle.ui.setting.fragment.ExportReportFragment
import com.fjp.skeletalmuscle.ui.setting.fragment.SystemSettingFragment
import com.fjp.skeletalmuscle.ui.setting.fragment.UserInfoFragment
import com.fjp.skeletalmuscle.viewmodel.state.SettingViewModel
import com.lxj.xpopup.XPopup

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


    override fun createObserver() {
        super.createObserver()
        eventViewModel.updateUserNameEvent.observeInActivity(this) {
            mViewModel.userInfo.set(App.userInfo)
            mViewModel.userInfo.get()?.notifyChange()
            CacheUtil.removeAccount(App.userInfo.mobile)
            val accounts = CacheUtil.getAccounts()
            accounts.add(Account(App.userInfo.name, App.userInfo.mobile, App.userInfo.profile))
            CacheUtil.setAccounts(accounts)
        }
        eventViewModel.updateAvatarEvent.observeInActivity(this){
            mViewModel.userInfo.set(App.userInfo)
            CacheUtil.removeAccount(App.userInfo.mobile)
            val accounts = CacheUtil.getAccounts()
            accounts.add(Account(App.userInfo.name, App.userInfo.mobile, App.userInfo.profile))
            CacheUtil.setAccounts(accounts)
        }
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
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun onResume() {
        super.onResume()
        //解决拍照后变成竖屏显示的问题
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        mDatabind.blurLayout.startBlur()
    }

    override fun onPause() {
        super.onPause()
        mDatabind.blurLayout.pauseBlur()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}