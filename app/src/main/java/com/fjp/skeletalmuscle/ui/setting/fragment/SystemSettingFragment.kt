package com.fjp.skeletalmuscle.ui.setting.fragment

import android.os.Bundle
import android.widget.CompoundButton
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.AppUtils
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.data.model.bean.result.VersionData
import com.fjp.skeletalmuscle.databinding.FragmentSystemSettingBinding
import com.fjp.skeletalmuscle.viewmodel.state.SystemSettingViewModel

class SystemSettingFragment : BaseFragment<SystemSettingViewModel, FragmentSystemSettingBinding>(), CompoundButton.OnCheckedChangeListener {
    var versionData: VersionData? = null

    companion object {
        fun newInstance() = SystemSettingFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.checkVersion()
        mDatabind.voiceInteractionLayout.showSwitch(this)
        val isCheck = CacheUtil.getVoiceInteraction()
        mDatabind.voiceInteractionLayout.setSwitchStatus(isCheck)
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.appVersion.observe(this) {
            versionData = it
            val newVersion = it.version.replace(".", "").toInt()
        }
    }

    inner class ProxyClick {
        fun clickUpdateVersion() {
            versionData?.let {
                if (it.version != AppUtils.getAppVersionName(context!!)) {
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.add(R.id.rightFragment, UpdateVersionFragment.newInstance(versionData!!))
                    transaction.addToBackStack("UpdateVersionFragment")
                    transaction.commit()
                }
            }
        }

    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        CacheUtil.setVoiceInteraction(p1)
    }
}