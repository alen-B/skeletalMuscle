package com.fjp.skeletalmuscle.app.base

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.ext.dismissLoadingExt
import com.fjp.skeletalmuscle.app.ext.showLoadingExt
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.weight.pop.ChangeAccountPop
import com.fjp.skeletalmuscle.app.weight.pop.VideoPop
import com.fjp.skeletalmuscle.data.model.bean.Account
import com.fjp.skeletalmuscle.ui.highKnee.HighKneeMainActivity
import com.fjp.skeletalmuscle.ui.login.LoginActivity
import com.fjp.skeletalmuscle.ui.setting.ChangeAccountActivity
import com.fjp.skeletalmuscle.ui.setting.SettingActivity
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.base.activity.BaseVmDbActivity
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.jessyan.autosize.AutoSizeCompat


abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {

    abstract override fun initView(savedInstanceState: Bundle?)
    override fun initTitleView() {
        findViewById<ImageView>(R.id.settingIv)?.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        findViewById<ImageView>(R.id.avatarIv)?.setOnClickListener {
            showChangeAccountPop()
        }
    }

    private fun showChangeAccountPop() {
        val changeAccountPop = ChangeAccountPop(this, object : ChangeAccountPop.Listener {

            override fun onclick(account: Account, pop: ChangeAccountPop) {
                val intent = Intent(this@BaseActivity, LoginActivity::class.java)
                intent.putExtra(Constants.INTENT_KEY_PHONE, account.phone)
                startActivity(intent)
            }


        })
        val pop = XPopup.Builder(this)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .isDestroyOnDismiss(true).autoOpenSoftInput(false)
            .asCustom(changeAccountPop)

        pop.show()

    }

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {}

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

   /* *//**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     */
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }
}