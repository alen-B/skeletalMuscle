package com.fjp.skeletalmuscle.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.app.weight.pop.AddSportsTypePop
import com.fjp.skeletalmuscle.data.model.bean.Account
import com.fjp.skeletalmuscle.data.model.bean.Sports
import com.fjp.skeletalmuscle.databinding.ActivitySuportsBinding
import com.fjp.skeletalmuscle.ui.main.MainActivity
import com.fjp.skeletalmuscle.ui.user.adapter.SportsChildTypeAdapter
import com.fjp.skeletalmuscle.ui.user.adapter.SportsTypeAdapter
import com.fjp.skeletalmuscle.viewmodel.request.SaveUserInfoViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectType
import com.fjp.skeletalmuscle.viewmodel.state.SuportsViewModel
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.parseState

class SportsActivity : BaseActivity<SuportsViewModel, ActivitySuportsBinding>() {
    private val saveUserInfoViewModel: SaveUserInfoViewModel by viewModels()
    lateinit var sportsAdapter: SportsTypeAdapter
    lateinit var sportsChildAdapter: SportsChildTypeAdapter
    var curItem: Sports?=null
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.sports_type_title))
        mDatabind.click = ProxyClick()
        mViewModel.curIndex.set("8")
        mViewModel.totalIndex.set("/10")
        mViewModel.showRightText.set(true)
        sportsAdapter = SportsTypeAdapter(mViewModel.dataArr, clickItem = { item ->
            curItem = item
            sportsChildAdapter = SportsChildTypeAdapter(curItem!!.child, clickItem = { item ->
            })
            mDatabind.recyclerChildView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), sportsChildAdapter)
        })

        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), sportsAdapter)
    }

    inner class ProxyClick {
        fun next() {
            if (curItem?.name === appContext.getString(R.string.sports_type_no)) {
                CacheUtil.removeAccount(App.userInfo.mobile)
                val accounts = CacheUtil.getAccounts()
                accounts.add(Account(App.userInfo.name, App.userInfo.mobile, App.userInfo.profile))
                CacheUtil.setAccounts(accounts)
                App.userInfo.device_no = SettingUtil.getDeviceId(this@SportsActivity)
                saveUserInfoViewModel.saveInfoReq(App.userInfo)

            } else {
//                App.userInfo.sports = arrayListOf(curItem!!.name)
                val intent = Intent(this@SportsActivity, SingleSelectActivity::class.java)
                intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.DAY_ONE_WEEK.type)
                startActivity(intent)
            }
        }

        fun finish() {
            this@SportsActivity.finish()
        }

        fun addSportsType() {
            val pop = XPopup.Builder(this@SportsActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(true).asCustom(AddSportsTypePop(this@SportsActivity, object : AddSportsTypePop.InputPasswordListener {
                override fun input(name: String) {
                    if (name.isEmpty()) {
                        showToast("请输入类型名称")
                        return
                    }
                    sportsAdapter.addData(0, Sports(name, arrayListOf(),true))
                    mDatabind.recyclerView.smoothScrollToPosition(0)
                }

            }))

            pop.show()
        }
    }

    override fun createObserver() {
        super.createObserver()
        saveUserInfoViewModel.saveResult.observe(this) {
            parseState(it, {
                val intent = Intent(this@SportsActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }, {
                showToast(getString(R.string.request_failed))
            })

        }
    }
}