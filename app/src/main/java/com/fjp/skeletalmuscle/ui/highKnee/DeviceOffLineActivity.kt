package com.fjp.skeletalmuscle.ui.highKnee

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.databinding.ActivityDeviceOffLineBinding
import com.fjp.skeletalmuscle.viewmodel.state.DeviceOffLineViewModel

class DeviceOffLineActivity : BaseActivity<DeviceOffLineViewModel,ActivityDeviceOffLineBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.today_sports_title))
        mViewModel.leftImg.set(R.drawable.off_line)
    }

    inner class ProxyClick{
        fun clickReconnect(){

        }
    }

}