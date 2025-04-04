package com.fjp.skeletalmuscle.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clj.fastble.BleManager
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.ext.dp
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.PermissionUtils
import com.fjp.skeletalmuscle.app.util.PermissionUtils.hasPermission
import com.fjp.skeletalmuscle.app.util.PermissionUtils.requestPermission
import com.fjp.skeletalmuscle.app.weight.recyclerview.SpaceItemDecoration
import com.fjp.skeletalmuscle.data.model.bean.TodaySportsType
import com.fjp.skeletalmuscle.databinding.ActivityTodaySelectSportsBinding
import com.fjp.skeletalmuscle.ui.user.adapter.TodaySportsTypeAdapter
import com.fjp.skeletalmuscle.viewmodel.state.TodaySelectSportsViewModel
import me.hgj.jetpackmvvm.base.appContext


class TodaySelectSportsActivity : BaseActivity<TodaySelectSportsViewModel, ActivityTodaySelectSportsBinding>(), TodaySportsTypeAdapter.SelectedSports {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.today_sports_title))
        mViewModel.leftImg.set(R.drawable.title_icon_sports_record)
        findViewById<TextView>(R.id.titleTv).setTextColor(ContextCompat.getColor(appContext, R.color.white))
        val todaySportsTypeAdapter = TodaySportsTypeAdapter(mViewModel.sportsType.get()!!, 0, this)
        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), todaySportsTypeAdapter)
        mDatabind.recyclerView.addItemDecoration(SpaceItemDecoration(12.dp.toInt(), 0))
    }

    override fun createObserver() {
        super.createObserver()
        eventViewModel.startSports.observeInActivity(this) {
            finish()
        }
    }

    inner class ProxyClick {
        fun clickFinish() {
            this@TodaySelectSportsActivity.finish()
        }

        fun clickStartSports() {
            if (mViewModel.sportsType.get() == null) {
                showToast(getString(R.string.today_sports_start))
                return
            }
            checkBluetoothPermission()
        }


    }


    private fun checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val bluetoothPermission = hasPermission(this, android.Manifest.permission.BLUETOOTH)
            val coarseLocationPermission = hasPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            val accessLocationPermission = hasPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            val scanPermission = hasPermission(this, android.Manifest.permission.BLUETOOTH_SCAN)
            val advertisePermission = hasPermission(this, android.Manifest.permission.BLUETOOTH_ADVERTISE)
            val connectPermission = hasPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)

            if (!bluetoothPermission || !coarseLocationPermission || !accessLocationPermission || !scanPermission || !advertisePermission || !connectPermission) {
                // 有一个或多个权限未授予，需要申请权限
                PermissionUtils.requestPermission(this, arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.BLUETOOTH_SCAN,
                    android.Manifest.permission.BLUETOOTH_ADVERTISE,
                    android.Manifest.permission.BLUETOOTH_CONNECT,
                ), 1001)
            } else {
                BleManager.getInstance().enableBluetooth()
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

    fun startExericisePlanActivity() {
//        if(mViewModel.curSports.type ==SportsType.DUMBBELL ){
//            showToast("正在开发中...")
//            return
//        }
        val intent = Intent(this@TodaySelectSportsActivity, ExercisePlanActivity::class.java)
        App.sportsType = mViewModel.curSports.type
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
            BleManager.getInstance().enableBluetooth()
            // 处理蓝牙权限请求结果
            if (PermissionUtils.verifyPermissions(grantResults)) {
                // 权限已授予
                startExericisePlanActivity()
            } else {
                // 用户拒绝了权限请求，可以进行相应的处理
                showPermissionDeniedDialog()
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
            openAppSettings()
        })
        builder.create().show()
    }

    // 打开应用程序设置页面
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
        startActivity(intent)
    }

    override fun onSelected(sportsType: TodaySportsType) {
        mViewModel.curSports = sportsType
    }


}