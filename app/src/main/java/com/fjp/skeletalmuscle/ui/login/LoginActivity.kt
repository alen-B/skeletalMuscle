package com.fjp.skeletalmuscle.ui.login

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.data.model.bean.Account
import com.fjp.skeletalmuscle.databinding.ActivityLoginBinding
import com.fjp.skeletalmuscle.ui.main.MainActivity
import com.fjp.skeletalmuscle.ui.user.InputNameActivity
import com.fjp.skeletalmuscle.viewmodel.request.RequestLoginViewModel
import com.fjp.skeletalmuscle.viewmodel.state.LoginViewModel
import com.jay.phone_text_watcher.PhoneTextWatcher
import com.jay.phone_text_watcher.TextChangeCallback
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.isPhone


class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {
    private val reqeustLoginViewModel: RequestLoginViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewModel = mViewModel
        val phone = intent.getStringExtra(Constants.INTENT_KEY_PHONE)


        mDatabind.click = ProxyClick()
        mViewModel.title.set(resources.getString(R.string.login_title))
        mViewModel.showRightImg.set(true)
        mViewModel.rightImg.set(R.drawable.setting_app_icon)
        val phoneTextWatcherSpace = PhoneTextWatcher()
        mDatabind.phoneEt.addTextChangedListener(phoneTextWatcherSpace)
        // 设置格式化输入的回调
        phoneTextWatcherSpace.setTextChangedCallback(object : TextChangeCallback() {
            override fun afterTextChanged(s: String?, isPhoneNumberValid: Boolean) {
                mViewModel.mobile.set(s)
            }
        })

        if (phone != null) {
            mViewModel.mobile.set(phone)
            mDatabind.phoneEt.setText(phone)
        }
        initAgreement()

    }

    private fun initAgreement() {
        val spannableStringBuilder = SpannableStringBuilder(getString(R.string.login_agreement))
        val agreementClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val intent = Intent(this@LoginActivity, ServiceAgreementActivity::class.java)
                intent.putExtra(Constants.INTENT_IS_SERVICE_AGREEMENT, true)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(appContext, R.color.color_blue)
            }
        }
        val privacyClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val intent = Intent(this@LoginActivity, ServiceAgreementActivity::class.java)
                intent.putExtra(Constants.INTENT_IS_SERVICE_AGREEMENT, false)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(appContext, R.color.color_blue)
            }
        }
        mDatabind.agreementTv.movementMethod = LinkMovementMethod.getInstance()
        mDatabind.agreementTv.highlightColor = Color.TRANSPARENT
        spannableStringBuilder.setSpan(agreementClickableSpan, 6, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringBuilder.setSpan(privacyClickableSpan, 16, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mDatabind.agreementTv.text = spannableStringBuilder
    }

    override fun createObserver() {
        super.createObserver()
        reqeustLoginViewModel.loginResult.observe(this) { resultState ->
            parseState(resultState, {
                CacheUtil.removeAccount(it.mobile)
                val accounts = CacheUtil.getAccounts()
                accounts.add(0, Account(it.name, it.mobile, it.profile))
                CacheUtil.setAccounts(accounts)

                if (it.name.isNullOrEmpty()) {//创建时间是0表示没有填写过个人信息
                    val intent = Intent(this@LoginActivity, InputNameActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    CacheUtil.setUser(it)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                App.userInfo = it

                finish()
            }, {
                showToast(getString(R.string.login_login_failed))
            })

        }
        reqeustLoginViewModel.code.observe(this) { resultState ->
            parseState(resultState, {
                println(it)
            }, {
                showToast(getString(R.string.login_get_verification_code_failed))
            })

        }
    }

    inner class ProxyClick {
        fun login() {

            when {
                (!mViewModel.mobile.get().isPhone()) -> showToast(getString(R.string.login_input_success_phone))
                mViewModel.verificationCode.get()!!.isEmpty() -> showToast(getString(R.string.login_input_code))
                (!mViewModel.agreement.get()!!) -> {
                    showToast(getString(R.string.login_agreement_no_checked))
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(mDatabind.agreementTv)
                }

                else -> {
                    checkPermissions()
                }
            }

        }

        fun requestVerificationCode() {
            if (!mViewModel.mobile.get().isPhone()) {
                showToast(getString(R.string.login_input_success_phone))
                return
            }
            reqeustLoginViewModel.codeReq(mViewModel.mobile.get()!!)
            startCountDown()
        }

        private fun startCountDown() {
            mViewModel.verificationCodeisEnabled.set(false)
            mDatabind.countDownTv.setTextColor(ContextCompat.getColor(appContext, R.color.color_331c1c1c))
            object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    mViewModel.verificationCodeText.set(String.format(getString(R.string.login_count_down), millisUntilFinished / 1000))
                }

                override fun onFinish() {
                    mViewModel.verificationCodeText.set(appContext.getString(R.string.login_get_verification_code))
                    mDatabind.countDownTv.setTextColor(ContextCompat.getColor(appContext, R.color.colorAccent))
                    mViewModel.verificationCodeisEnabled.set(false)
                }
            }.start()
        }

        var onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            mViewModel.agreement.set(isChecked)
        }
    }

    private val REQUEST_LOCATION_PERMISSION = 1

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            // 权限已授予，执行获取位置信息的操作
            reqeustLoginViewModel.loginReq(mViewModel.mobile.get()!!, mViewModel.verificationCode.get()!!)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已授予，执行获取位置信息的操作
                reqeustLoginViewModel.loginReq(mViewModel.mobile.get()!!, mViewModel.verificationCode.get()!!)
            } else {
                // 权限被拒绝
                Toast.makeText(this, "位置权限被拒绝", Toast.LENGTH_SHORT).show()
            }
        }
    }
}