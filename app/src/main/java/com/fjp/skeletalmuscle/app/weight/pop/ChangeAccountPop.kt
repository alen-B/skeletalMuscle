package com.fjp.skeletalmuscle.app.weight.pop

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.dp
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.weight.recyclerview.SpaceItemDecoration
import com.fjp.skeletalmuscle.data.model.bean.Account
import com.fjp.skeletalmuscle.ui.setting.SettingActivity
import com.fjp.skeletalmuscle.ui.setting.adapter.AccountAdapter
import com.lxj.xpopup.impl.FullScreenPopupView
import io.alterac.blurkit.BlurLayout

/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class ChangeAccountPop(val context: Activity, val listener: Listener) : FullScreenPopupView(context) {
    private lateinit var blurLayout: BlurLayout
    private lateinit var backIv: BlurLayout

    interface Listener {
        fun onclick(account: Account, pop: ChangeAccountPop)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_change_account
    }

    override fun onCreate() {
        super.onCreate()
        val accounts = mutableListOf<Account>()
        accounts.add(Account("冯大爷", "15699999999", ""))
        accounts.add(Account("张大妈", "15688888888", ""))
        accounts.add(Account("李大妈", "15666666666", ""))
        accounts.add(Account("添加账号", "", ""))
        val adapter = AccountAdapter(accounts) { account, _ ->
            listener.onclick(account, this)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context, RecyclerView.HORIZONTAL, false), adapter)
        recyclerView.addItemDecoration(SpaceItemDecoration(60.dp.toInt(), 0))
        findViewById<ImageView>(R.id.backIv).setOnClickListener {
            dismiss()
        }
        blurLayout = findViewById(R.id.blurLayout)
        blurLayout.startBlur()

        findViewById<ImageView>(R.id.settingIv)?.setOnClickListener {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        blurLayout.pauseBlur()
    }
}