package com.fjp.skeletalmuscle.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityTodaySelectSportsBinding
import com.fjp.skeletalmuscle.utils.AnimUtil
import com.fjp.skeletalmuscle.utils.PermissionUtils
import com.fjp.skeletalmuscle.utils.PermissionUtils.hasPermission
import com.fjp.skeletalmuscle.utils.PermissionUtils.requestPermission
import com.fjp.skeletalmuscle.viewmodel.state.TodaySelectSuportsViewModel
import me.hgj.jetpackmvvm.base.appContext


class TodaySelectSportsActivity :BaseActivity<TodaySelectSuportsViewModel,ActivityTodaySelectSportsBinding>(){
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.today_sports_title))
        mViewModel.leftImg.set(R.drawable.title_icon_sports_record)
        findViewById<TextView>(R.id.titleTv).setTextColor(ContextCompat.getColor(appContext,R.color.white))
    }

    inner class ProxyClick{
        fun clickFinish(){
            this@TodaySelectSportsActivity.finish()
        }

        fun clickStartSports(){
            if(mViewModel.sportsType.get()==null){
                showToast(getString(R.string.today_sports_start))
                return
            }
            checkBluetoothPermission()
        }
        fun clickHeightLeg(){
            if(mDatabind.legDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.legDetailCl,mDatabind.legIv)
            }else{
                mViewModel.sportsType.set(SportsType.LEG_LIFT)
                rotateAndSwitchViews(mDatabind.legIv,mDatabind.legDetailCl)
            }

            if(mDatabind.dumbbellCl.isVisible) {
                rotateAndSwitchViews(mDatabind.dumbbellCl, mDatabind.dumbbellIv)
            }

            if(mDatabind.pushUpDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.pushUpDetailCl,mDatabind.plankIv)
            }
        }
        fun clickDumbbell(){
            if(mDatabind.dumbbellCl.isVisible){
                rotateAndSwitchViews(mDatabind.dumbbellCl,mDatabind.dumbbellIv)
            }else{
                mViewModel.sportsType.set(SportsType.DUMBBELL)
                rotateAndSwitchViews(mDatabind.dumbbellIv,mDatabind.dumbbellCl)
            }
            if(mDatabind.legDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.legDetailCl,mDatabind.legIv)
            }
            if(mDatabind.pushUpDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.pushUpDetailCl,mDatabind.plankIv)
            }
        }
        fun clickPushUp(){
            if(mDatabind.pushUpDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.pushUpDetailCl,mDatabind.plankIv)
            }else{
                mViewModel.sportsType.set(SportsType.PUSH_UP)
                rotateAndSwitchViews(mDatabind.plankIv,mDatabind.pushUpDetailCl)
            }
            if(mDatabind.legDetailCl.isVisible){
                rotateAndSwitchViews(mDatabind.legDetailCl,mDatabind.legIv)
            }
            if(mDatabind.dumbbellCl.isVisible){
                rotateAndSwitchViews(mDatabind.dumbbellCl,mDatabind.dumbbellIv)
            }
        }
    }


    private fun checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val scanPermission = hasPermission(this, android.Manifest.permission.BLUETOOTH_SCAN);
            val advertisePermission = hasPermission(this, android.Manifest.permission.BLUETOOTH_ADVERTISE);
            val connectPermission = hasPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT);

            if (!scanPermission || !advertisePermission || !connectPermission) {
                // 有一个或多个权限未授予，需要申请权限
                PermissionUtils.requestPermission(this, arrayOf(
                    android.Manifest.permission.BLUETOOTH_SCAN,
                    android.Manifest.permission.BLUETOOTH_ADVERTISE,
                    android.Manifest.permission.BLUETOOTH_CONNECT,
                ), 1001);
            } else {
                startExericisePlanActivity()
            }
        } else {
            // 处理 Android 12 之前的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermissions()
            } else {
                // Android 版本低于 M，
                startExericisePlanActivity()
            }
        }
    }

    fun startExericisePlanActivity(){
        val intent = Intent(this@TodaySelectSportsActivity,ExercisePlanActivity::class.java)
        intent.putExtra(Constants.INTENT_SPORTS_TYPE,mViewModel.sportsType.get()?.type)
        startActivity(intent)
    }
    fun checkLocationPermissions() {
        // 检查权限状态
        val locationPermission = hasPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val accessPermission = hasPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        if (!locationPermission || !accessPermission) {
            // 有一个或多个权限未授予，需要申请权限
            requestPermission(this, arrayOf<String?>(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 1002)
        } else {
            // 权限已授予
            startExericisePlanActivity()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            // 处理蓝牙权限请求结果
            if (PermissionUtils.verifyPermissions(grantResults)) {
                // 权限已授予
                startExericisePlanActivity()
            } else {
                // 用户拒绝了权限请求，可以进行相应的处理
            }
        } else if (requestCode == 1002) {
            // 处理位置权限请求结果
            if (PermissionUtils.verifyPermissions(grantResults)) {
                // 权限已授予，跳转到蓝牙页面
                startExericisePlanActivity()
            } else {
                // 用户拒绝了权限请求，可以进行相应的处理
                showPermissionDeniedDialog()
            }
        }
    }
    private fun showPermissionDeniedDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.request_bluetooth_permission_reject)).setPositiveButton(getString(R.string.request_bluetooth_permission_go_setting), DialogInterface.OnClickListener { dialog, id -> openAppSettings() }).setNegativeButton(getString(R.string.request_bluetooth_permission_cancel), DialogInterface.OnClickListener { dialog, id ->
            // 关闭应用或其他操作
        })
        builder.create().show()
    }

    // 打开应用程序设置页面
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
        startActivity(intent)
    }


    private fun rotateAndSwitchViews( iv: View, cl: View) {
        AnimUtil.flipAnimatorYViewShow(iv,cl,500)
    }

}