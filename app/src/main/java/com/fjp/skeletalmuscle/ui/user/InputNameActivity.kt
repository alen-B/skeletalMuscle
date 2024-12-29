package com.fjp.skeletalmuscle.ui.user

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.databinding.ActivityInputNameBinding
import com.fjp.skeletalmuscle.viewmodel.state.InputNameViewModel
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest

class InputNameActivity : BaseActivity<InputNameViewModel, ActivityInputNameBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(resources.getString(R.string.input_name_title))
        mViewModel.curIndex.set("1")
        mViewModel.totalIndex.set("/10")
        mViewModel.showRightText.set(true)
        TencentLocationManager.setUserAgreePrivacy(true);
        val mLocationManager = TencentLocationManager.getInstance(this)
        val request = TencentLocationRequest.create()
        request.isGpsFirst = true
        mLocationManager.requestSingleFreshLocation(request, object : TencentLocationListener {
            override fun onLocationChanged(location: TencentLocation?, p1: Int, p2: String?) {
                location?.let {
                    App.userInfo.latitude = it.latitude.toString()
                    App.userInfo.longitude = it.longitude.toString()
                    App.userInfo.address = it.address+it.name
                    App.userInfo.province = it.province
                    App.userInfo.city = it.city
                    App.userInfo.district = it.district
                }
            }

            override fun onStatusUpdate(p0: String?, p1: Int, p2: String?) {

            }

        }, Looper.getMainLooper())
    }

    inner class ProxyClick {
        fun next() {
            if (mViewModel.name.get()!!.isEmpty()) {
                showToast(getString(R.string.input_name_tips))
                return
            }
            App.userInfo.name = mViewModel.name.get().toString()
            startActivity(Intent(this@InputNameActivity, SelectGenderActivity::class.java))

        }
    }

    override fun createObserver() {
        super.createObserver()
    }
}