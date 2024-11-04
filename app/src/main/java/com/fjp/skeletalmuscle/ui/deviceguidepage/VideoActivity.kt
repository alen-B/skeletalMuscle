package com.fjp.skeletalmuscle.ui.deviceguidepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.databinding.ActivityVideoBinding
import com.fjp.skeletalmuscle.viewmodel.state.VideoViewModel

class VideoActivity : BaseActivity<VideoViewModel,ActivityVideoBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
    }


    inner class ProxyClick{
        fun clickJump(){
            this@VideoActivity.finish()
        }
    }
}