package com.fjp.skeletalmuscle.ui.setting.fragment

import android.os.Bundle
import android.widget.CompoundButton
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.data.model.bean.result.VersionData
import com.fjp.skeletalmuscle.databinding.FragmentUpdateVersionBinding
import com.fjp.skeletalmuscle.viewmodel.state.UpdateVersionViewModel

class UpdateVersionFragment(val versionData: VersionData) : BaseFragment<UpdateVersionViewModel, FragmentUpdateVersionBinding>(), CompoundButton.OnCheckedChangeListener {

    companion object {
        fun newInstance(versionData: VersionData) = UpdateVersionFragment(versionData)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.versionTitle.set("骨骼肌V${versionData.version}")
        versionData.content.forEach {
            mViewModel.content.set(mViewModel.content.get()+it+'\n')
        }

        val isCheck = CacheUtil.getVoiceInteraction()

    }


    inner class ProxyClick {

        fun clickUpdate() {
//            HttpDownLoadManager()
        }

        fun clickDisUpdate() {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        CacheUtil.setVoiceInteraction(p1)
    }
}