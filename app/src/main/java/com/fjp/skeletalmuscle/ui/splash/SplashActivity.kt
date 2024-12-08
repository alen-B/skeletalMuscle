package com.fjp.skeletalmuscle.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.AppUtils
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.weight.pop.NewVersionPop
import com.fjp.skeletalmuscle.data.model.bean.result.VersionData
import com.fjp.skeletalmuscle.databinding.ActivitySplashBinding
import com.fjp.skeletalmuscle.ui.login.LoginActivity
import com.fjp.skeletalmuscle.ui.main.MainActivity
import com.fjp.skeletalmuscle.ui.setting.fragment.UpdateVersionFragment
import com.fjp.skeletalmuscle.viewmodel.state.SplashViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SystemSettingViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.skeletalmuscle.appupdate.listener.OnDownloadListener
import com.skeletalmuscle.appupdate.manager.HttpDownLoadManager
import com.skeletalmuscle.appupdate.utils.ApkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>(), OnDownloadListener {
    val systemSettingViewModel: SystemSettingViewModel by viewModels()
    private var newVersion = false
    private var apkFile: File? = null
    lateinit var pop: BasePopupView
    private lateinit var newVersionPop:NewVersionPop
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        systemSettingViewModel.checkVersion()
        lifecycleScope.launch {
            delay(2000)
            doNext()

        }

    }

    fun doNext(){
        if(!newVersion){
            if (CacheUtil.isLogin() && CacheUtil.getUser() != null) {
                App.userInfo = CacheUtil.getUser()!!
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        }
    }

    override fun createObserver() {
        super.createObserver()
        systemSettingViewModel.appVersion.observe(this){
            if (it.version != AppUtils.getAppVersionName(this)) {
                newVersion=true
                showNewVersionPop()
            }
        }
    }

    private fun showNewVersionPop() {
        newVersionPop = NewVersionPop(this, object : NewVersionPop.Listener {


            override fun onClickUpdate(pop: NewVersionPop) {
                externalCacheDir?.let {
                    HttpDownLoadManager(it.path).download(systemSettingViewModel.appVersion.value!!.download_url, "gugeji.apk", this@SplashActivity)
                }
            }

            override fun onClickNotUpdate() {
                doNext()
            }


        },systemSettingViewModel.appVersion.value!! )
        pop = XPopup.Builder(this).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(newVersionPop)

        pop.show()

    }

    override fun start() {
    }

    override fun onDownLoading(max: Int, progress: Int) {
        val curr = (progress / max.toDouble() * 100.0).toInt()
        runOnUiThread {
            newVersionPop.setProgress(curr)
        }
    }

    override fun cancel() {
    }

    override fun done(apk: File) {
        runOnUiThread {
            pop.dismiss()
            if (!packageManager.canRequestPackageInstalls()) {
                this@SplashActivity.apkFile = apk
                ApkUtils.startPackageInstallActivity(this@SplashActivity)
            }else{
                ApkUtils.installAPK(this@SplashActivity, apk)
            }


        }
    }

    override fun error(e: Exception) {
        runOnUiThread {
            pop.dismiss()
            e.printStackTrace()
            showToast("更新失败！")
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        if (requestCode == ApkUtils.GET_UNKNOWN_APP_SOURCES) {
            apkFile?.let { ApkUtils.installAPK(this@SplashActivity, it) }
            finish()
        }
    }
}