package com.fjp.skeletalmuscle.ui.user

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.LocationUtil
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.app.weight.pop.AddSportsTypePop
import com.fjp.skeletalmuscle.data.model.bean.Account
import com.fjp.skeletalmuscle.data.model.bean.Sports
import com.fjp.skeletalmuscle.data.model.bean.SportsChild
import com.fjp.skeletalmuscle.data.model.bean.UserSports
import com.fjp.skeletalmuscle.databinding.ActivitySuportsBinding
import com.fjp.skeletalmuscle.ui.main.MainActivity
import com.fjp.skeletalmuscle.ui.user.adapter.SportsChildTypeAdapter
import com.fjp.skeletalmuscle.ui.user.adapter.SportsTypeAdapter
import com.fjp.skeletalmuscle.viewmodel.request.SaveUserInfoViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectType
import com.fjp.skeletalmuscle.viewmodel.state.SuportsViewModel
import com.lxj.xpopup.XPopup
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest
import me.hgj.jetpackmvvm.ext.parseState


class SportsActivity : BaseActivity<SuportsViewModel, ActivitySuportsBinding>() {
    private val saveUserInfoViewModel: SaveUserInfoViewModel by viewModels()
    lateinit var sportsAdapter: SportsTypeAdapter
    lateinit var sportsChildAdapter: SportsChildTypeAdapter
    var curItem: Sports? = null
    var locationManager: LocationManager? = null
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.sports_type_title))
        mDatabind.click = ProxyClick()
        mViewModel.curIndex.set("8")
        mViewModel.totalIndex.set("/10")
        mViewModel.showRightText.set(true)
        sportsAdapter = SportsTypeAdapter(mViewModel.dataArr, clickItem = { item ->

            curItem = item
            sportsChildAdapter = SportsChildTypeAdapter(if (curItem!!.isSelected) curItem!!.child else arrayListOf(), clickItem = { item ->
            })
            mDatabind.recyclerChildView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), sportsChildAdapter)
        })

        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), sportsAdapter)
    }

    inner class ProxyClick {
        fun next() {
            val copyDataArr = mutableListOf<Sports>()
            for ((index, sports) in mViewModel.dataArr.withIndex()) {
                copyDataArr.add(Sports(mViewModel.dataArr[index].name, mViewModel.dataArr[index].child, mViewModel.dataArr[index].isSelected))
            }
            val selectedSports = copyDataArr.filter { it.isSelected }
            selectedSports.forEach {
                it.child = it.child.filter { child -> child.isSelected } as ArrayList<SportsChild>
            }
            val userSports = arrayListOf<UserSports>()
            selectedSports.forEach {
                userSports.add(UserSports(it.child.map { it.name }, it.name))
            }
            for ((index, _) in userSports.withIndex()) {
                if (userSports.get(index).sport_name != "无" && userSports.get(index).child_sport_name.isEmpty()) {
                    showToast("请选择对应运动第二分类")
                    return
                }
            }
            App.userInfo.sports = userSports
            mViewModel.dataArr.forEach {
                if (it.name == getString(R.string.sports_type_no) && it.isSelected) {
                    CacheUtil.removeAccount(App.userInfo.mobile)
                    val accounts = CacheUtil.getAccounts()
                    accounts.add(Account(App.userInfo.name, App.userInfo.mobile, App.userInfo.profile))
                    CacheUtil.setAccounts(accounts)
                    CacheUtil.setUser(App.userInfo)
                    App.userInfo.device_no = SettingUtil.getDeviceId(this@SportsActivity)
                    saveUserInfoViewModel.saveInfoReq(App.userInfo)
                    return
                }
            }


            val intent = Intent(this@SportsActivity, SingleSelectActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.DAY_ONE_WEEK.type)
            startActivity(intent)
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
                    sportsAdapter.addData(0, Sports(name, arrayListOf(), false))
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