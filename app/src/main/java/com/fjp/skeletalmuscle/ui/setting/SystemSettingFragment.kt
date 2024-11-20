package com.fjp.skeletalmuscle.ui.setting

import android.os.Bundle
import android.widget.CompoundButton
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.databinding.FragmentSystemSettingBinding
import com.fjp.skeletalmuscle.viewmodel.state.SystemSettingViewModel

class SystemSettingFragment : BaseFragment<SystemSettingViewModel, FragmentSystemSettingBinding>(), CompoundButton.OnCheckedChangeListener {

    companion object {
        fun newInstance() = SystemSettingFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.voiceInteractionLayout.showSwitch(this)
       val isCheck = CacheUtil.getVoiceInteraction()
        mDatabind.voiceInteractionLayout.setSwitchStatus(isCheck)
    }

    inner class ProxyClick{
        fun clickUpdateVersion(){
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.rightFragment, UpdateVersionFragment.newInstance())
            transaction.addToBackStack("UpdateVersionFragment")
            transaction.commit()
        }

    }
    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        CacheUtil.setVoiceInteraction(p1)
    }
}