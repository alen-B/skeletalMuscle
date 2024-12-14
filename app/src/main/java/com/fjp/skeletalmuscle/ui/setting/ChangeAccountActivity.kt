package com.fjp.skeletalmuscle.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.dp
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.weight.recyclerview.SpaceItemDecoration
import com.fjp.skeletalmuscle.databinding.ActivityChangeAccountBinding
import com.fjp.skeletalmuscle.ui.login.LoginActivity
import com.fjp.skeletalmuscle.ui.setting.adapter.AccountAdapter
import com.fjp.skeletalmuscle.viewmodel.state.ChangeAccountViewModel

/**
 * 现在使用的是dialog没用activity
 */
class ChangeAccountActivity : BaseActivity<ChangeAccountViewModel, ActivityChangeAccountBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        val adapter = AccountAdapter(mViewModel.accounts) { account, _ ->
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_PHONE, account.phone)
            startActivity(intent)
        }
        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), adapter)
        mDatabind.recyclerView.addItemDecoration(SpaceItemDecoration(60.dp.toInt(), 0))
    }


    inner class ProxyClick {
        fun clickFinish() {
            finish()
        }
    }


}