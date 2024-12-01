package com.fjp.skeletalmuscle.ui.login

import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.databinding.ActivityServiceAgreementBinding
import com.fjp.skeletalmuscle.viewmodel.state.ServiceAgreementViewModel

class ServiceAgreementActivity : BaseActivity<ServiceAgreementViewModel, ActivityServiceAgreementBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        val isServiceAgreemt = intent.getBooleanExtra(Constants.INTENT_IS_SERVICE_AGREEMENT, true)
        if (isServiceAgreemt) {
            mViewModel.title.set(getString(R.string.service_agreement_title))
            mDatabind.webview.loadUrl("file:///android_asset/serviceagreement.html")
        } else {
            mViewModel.title.set(getString(R.string.privacy_policy_title))
            mDatabind.webview.loadUrl("file:///android_asset/privacypolicy.html")
        }

    }

    inner class ProxyClick {
        fun clickFinish() {
            this@ServiceAgreementActivity.finish()
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