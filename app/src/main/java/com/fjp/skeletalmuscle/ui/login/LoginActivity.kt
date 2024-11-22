package com.fjp.skeletalmuscle.ui.login

import android.content.Intent
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
import androidx.core.content.ContextCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.databinding.ActivityLoginBinding
import com.fjp.skeletalmuscle.ui.user.InputNameActivity
import com.fjp.skeletalmuscle.viewmodel.state.LoginViewModel
import com.jay.phone_text_watcher.PhoneTextWatcher
import com.jay.phone_text_watcher.TextChangeCallback
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.util.isPhone


class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewModel = mViewModel
        mViewModel.phone.set(intent.getStringExtra(Constants.INTENT_KEY_PHONE))
        mDatabind.click = ProxyClick()
        mViewModel.title.set(resources.getString(R.string.login_title))
        mViewModel.showRightImg.set(true)
        mViewModel.rightImg.set(R.drawable.login_title_right_icon)
        val phoneTextWatcherSpace = PhoneTextWatcher()
        mDatabind.phoneEt.addTextChangedListener(phoneTextWatcherSpace)
        // 设置格式化输入的回调
        phoneTextWatcherSpace.setTextChangedCallback(object : TextChangeCallback() {
            override fun afterTextChanged(s: String?, isPhoneNumberValid: Boolean) {
                mViewModel.phone.set(s)
            }
        })

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
                ds.color = ContextCompat.getColor(appContext, R.color.color_1c1c1c)
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
                ds.color = ContextCompat.getColor(appContext, R.color.color_1c1c1c)
            }
        }
        mDatabind.agreementTv.movementMethod = LinkMovementMethod.getInstance()
        mDatabind.agreementTv.highlightColor = Color.TRANSPARENT
        spannableStringBuilder.setSpan(agreementClickableSpan, 6, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringBuilder.setSpan(privacyClickableSpan, 17, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mDatabind.agreementTv.text = spannableStringBuilder
    }

    inner class ProxyClick {
        fun login() {
            when {
                (!mViewModel.phone.get().isPhone()) -> showToast(getString(R.string.login_input_success_phone))
                mViewModel.verificationCode.get()!!.isEmpty() -> showToast(getString(R.string.login_input_code))
                (!mViewModel.agreement.get()!!) -> {
                    showToast(getString(R.string.login_agreement_no_checked))
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(mDatabind.agreementTv)
                }

                else -> {
                    val intent = Intent(this@LoginActivity, InputNameActivity::class.java)
                    intent.flags =Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    App.userInfo?.phone = mViewModel.phone.get()!!
                    finish()
                }
            }

        }

        fun requestVerificationCode() {
            if (!mViewModel.phone.get().isPhone()) {
                showToast(getString(R.string.login_input_success_phone))
                return
            }
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

}