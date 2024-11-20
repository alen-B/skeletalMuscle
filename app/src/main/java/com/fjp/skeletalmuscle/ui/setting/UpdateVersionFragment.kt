package com.fjp.skeletalmuscle.ui.setting

import android.os.Bundle
import android.widget.CompoundButton
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.databinding.FragmentUpdateVersionBinding
import com.fjp.skeletalmuscle.viewmodel.state.UpdateVersionViewModel
import com.skeletalmuscle.appupdate.manager.HttpDownLoadManager

class UpdateVersionFragment : BaseFragment<UpdateVersionViewModel, FragmentUpdateVersionBinding>(), CompoundButton.OnCheckedChangeListener {

    companion object {
        fun newInstance() = UpdateVersionFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
       val isCheck = CacheUtil.getVoiceInteraction()
    }

    inner class ProxyClick{

        fun clickUpdate(){
//            HttpDownLoadManager()
        }
        fun clickDisUpdate(){
            parentFragmentManager.popBackStack()
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        CacheUtil.setVoiceInteraction(p1)
    }
}