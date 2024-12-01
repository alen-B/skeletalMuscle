package com.fjp.skeletalmuscle.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.databinding.ActivitySplashBinding
import com.fjp.skeletalmuscle.ui.login.LoginActivity
import com.fjp.skeletalmuscle.ui.main.MainActivity
import com.fjp.skeletalmuscle.viewmodel.state.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        lifecycleScope.launch {
            delay(2000)
            if (CacheUtil.isLogin() && CacheUtil.getUser()!=null) {
                App.userInfo=CacheUtil.getUser()!!
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        }

    }

}