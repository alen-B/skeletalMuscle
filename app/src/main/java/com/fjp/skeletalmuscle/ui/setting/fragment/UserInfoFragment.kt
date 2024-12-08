package com.fjp.skeletalmuscle.ui.setting.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.util.PermissionUtils
import com.fjp.skeletalmuscle.app.weight.pop.SingleSelectedPop
import com.fjp.skeletalmuscle.app.weight.pop.TakePhotoPop
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
import com.fjp.skeletalmuscle.databinding.FragmentUserInfoBinding
import com.fjp.skeletalmuscle.ui.login.LoginActivity
import com.fjp.skeletalmuscle.viewmodel.request.RequestUserInfoViewModel
import com.fjp.skeletalmuscle.viewmodel.request.SaveUserInfoViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectType
import com.fjp.skeletalmuscle.viewmodel.state.UserInfoViewModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.NumberUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class UserInfoFragment : BaseFragment<UserInfoViewModel, FragmentUserInfoBinding>() {
    val saveUserInfoViewModel: SaveUserInfoViewModel by viewModels()
    val requestUserInfoViewModel: RequestUserInfoViewModel by viewModels()
    var tempUserInfo: UserInfo = App.userInfo
    val takePhotoCode = 200
    val takeAlbumCode = 201

    companion object {
        fun newInstance() = UserInfoFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        App.userInfo?.let {
            mDatabind.userNameLayout.setValue(it.name)
            mDatabind.phoneLayout.setValue(it.mobile)
            mDatabind.bornLayout.setValue(it.birthday)

            mDatabind.sexLayout.setValue(it.sex)
            mDatabind.weightLayout.setValue(it.weight.toString())
            mDatabind.heightLayout.setValue(it.height.toString())
            mDatabind.waistLinLayout.setValue(it.waistline)
            mDatabind.avatarLayout.setAvatarIv(it.profile)
        }

    }

    override fun createObserver() {
        super.createObserver()
        eventViewModel.updateUserNameEvent.observeInFragment(this) {
            mDatabind.userNameLayout.setValue(it)
        }
        eventViewModel.updatePhoneEvent.observeInFragment(this) {
            mDatabind.phoneLayout.setValue(it)
        }
        saveUserInfoViewModel.saveResult.observe(this) {
            parseState(it, {
                App.userInfo = tempUserInfo
                mDatabind.heightLayout.setValue(App.userInfo.height.toString())
                mDatabind.weightLayout.setValue(App.userInfo.weight.toString())
                mDatabind.waistLinLayout.setValue(App.userInfo.waistline)
                mDatabind.sexLayout.setValue(App.userInfo.sex)

                CacheUtil.setUser(App.userInfo)
            }, {
                appContext.showToast(getString(R.string.request_failed))
            })
        }

        requestUserInfoViewModel.avatar.observe(this) {
            appContext.showToast("更新成功")
            mDatabind.avatarLayout.setAvatarIv(App.userInfo.profile)
            App.userInfo.profile = it
            CacheUtil.setUser(App.userInfo)
        }

        requestUserInfoViewModel.updateImageFailed.observe(this) {
            appContext.showToast("更新失败")
        }
    }

    inner class ProxyClick {
        fun clickUpdateAvatar() {
            showTakePhotoPop()
        }

        fun clickUpdateUserName() {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.rightFragment, SetUserNameAndPhoneFragment(true))
            transaction.addToBackStack("SetUserNameFragment")
            transaction.commit()
        }

        fun clickUpdatePhone() {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.rightFragment, SetUserNameAndPhoneFragment(false))
            transaction.addToBackStack("SetUserNameAndPhoneFragment")
            transaction.commit()
        }

        fun clickUpdateBorn() {
            if (!this@UserInfoFragment::pvTime.isInitialized) {
                initTimePickerrDialog()
            }
            pvTime.show()
        }

        fun clickUpdateHeight() {
            showSingleSelectedDialog(SingleSelectType.HEIGHT)
        }

        fun clickUpdateWeight() {
            showSingleSelectedDialog(SingleSelectType.WEIGHT)
        }

        fun clickUpdateWaistLine() {
            showSingleSelectedDialog(SingleSelectType.WAIST_LINE)
        }

        fun clickUpdateSex() {
            showSingleSelectedDialog(SingleSelectType.SEX)
        }
        fun clickExit() {
            val pop = XPopup.Builder(context).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asConfirm(getString(R.string.setting_exit), getString(R.string.setting_exit_content), {
                CacheUtil.setUser(null)
                CacheUtil.setIsLogin(false)
                val intent = Intent(context, LoginActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }, { })

            pop.show()
        }
    }

    lateinit var pvTime: TimePickerView
    lateinit var curDate: Date
    fun initTimePickerrDialog() {
        val startDate: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        val endDate: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        endDate.set(2020, 1, 1);
        startDate.set(1940, 0, 1, 12, 0, 0)
        val selectedDate: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        selectedDate.timeInMillis = DateTimeUtil.formatDate(DateTimeUtil.DATE_PATTERN, App.userInfo.birthday).time
        pvTime = TimePickerBuilder(context) { date, v -> //选中事件回调
            curDate = date
            tempUserInfo = App.userInfo
            tempUserInfo.birthday = DateTimeUtil.formatDate(date, DateTimeUtil.DATE_PATTERN)
            saveUserInfoViewModel.saveInfoReq(tempUserInfo)
            mDatabind.bornLayout.setValue(App.userInfo.birthday)
        }.setType(booleanArrayOf(true, true, true, false, false, false)) // 默认全部显示
            .setCancelText(getString(R.string.cancel)) //取消按钮文字
            .setSubmitText(getString(R.string.sure)) //确认按钮文字
            .setContentTextSize(28) //滚轮文字大小
            .setTitleSize(30) //标题文字大小
            .setTitleText("") //标题文字
            .setOutSideCancelable(false) //点击屏幕，点在控件外部范围时，是否取消显示
            .isCyclic(false) //是否循环滚动
            .setTitleColor(Color.BLACK) //标题文字颜色
            .setSubmitColor(Color.BLUE) //确定按钮文字颜色
            .setCancelColor(Color.BLUE) //取消按钮文字颜色
//            .setTitleBgColor(-0x99999a) //标题背景颜色 Night mode
//            .setBgColor(-0xcccccd) //滚轮背景颜色 Night mode
            .setDate(selectedDate) // 如果不设置的话，默认是系统时间*/
            .setOutSideCancelable(true).setRangDate(startDate, endDate) //起始终止年月日设定
            .setLabel("年", "月", "日", "时", "分", "秒") //默认设置为年月日时分秒
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .isDialog(true) //是否显示为对话框样式
            .build()
        val mDialog = pvTime.dialog
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)
            //去掉默认的maring
            params.leftMargin = 0
            params.rightMargin = 0
            pvTime.dialogContainerLayout.layoutParams = params

            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                val lp = dialogWindow.attributes
                val dm = resources.displayMetrics
                //设置横屏，占满屏幕宽度
                lp.width = dm.widthPixels
                dialogWindow.attributes = lp
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM)//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f)
            }
        }
    }

    fun showSingleSelectedDialog(type: SingleSelectType) {
        val pop = XPopup.Builder(context).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(SingleSelectedPop(requireContext(), type, object : SingleSelectedPop.Listener {
            override fun onSelected(data: String, pop: SingleSelectedPop) {
                if (type == SingleSelectType.HEIGHT) {
                    tempUserInfo = App.userInfo
                    tempUserInfo.height = NumberUtils.extractNumbers(data)[0]
                    saveUserInfoViewModel.saveInfoReq(tempUserInfo)

                } else if (type == SingleSelectType.WEIGHT) {
                    tempUserInfo = App.userInfo
                    tempUserInfo.weight = NumberUtils.extractNumbers(data)[0]
                    saveUserInfoViewModel.saveInfoReq(tempUserInfo)

                } else if (type == SingleSelectType.WAIST_LINE) {
                    tempUserInfo = App.userInfo
                    tempUserInfo.waistline = NumberUtils.extractNumbers(data)[0].toString()
                    saveUserInfoViewModel.saveInfoReq(tempUserInfo)
                } else if (type == SingleSelectType.SEX) {
                    tempUserInfo = App.userInfo
                    tempUserInfo.sex = data
                    saveUserInfoViewModel.saveInfoReq(tempUserInfo)
                }
                pop.dismiss()
            }

        }))

        pop.show()
    }

    fun showTakePhotoPop() {
        val pop = XPopup.Builder(context).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(TakePhotoPop(requireContext(), object : TakePhotoPop.Listener {
            override fun onclickItem(index: Int, pop: TakePhotoPop) {
                if (index == TakePhotoPop.TAKE_PHOTO) {
                    if (PermissionUtils.hasPermission(context!!, android.Manifest.permission.CAMERA)) {
                        takePhoto()
                    } else {
                        PermissionUtils.requestPermission(activity as Activity, arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE), takePhotoCode)
                    }
                } else if (index == TakePhotoPop.ALBUM) {
                    if (PermissionUtils.hasPermission(context!!, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        takeAlbum()
                    } else {
                        PermissionUtils.requestPermission(activity as Activity, arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE), takeAlbumCode)
                    }

                }
                pop.dismiss()
            }

        }))

        pop.show()
    }

    fun takeAlbum() {
        PictureSelector.create(this).openSystemGallery(SelectMimeType.ofImage()).isOriginalSkipCompress(false).forSystemResult(object : OnResultCallbackListener<LocalMedia?> {
            override fun onResult(result: ArrayList<LocalMedia?>) {
                println(result)
                updateAvatar(result[0]?.compressPath)
            }

            override fun onCancel() {

            }
        })
    }

    fun takePhoto() {
        PictureSelector.create(this).openCamera(SelectMimeType.ofImage()).isOriginalSkipCompress(false).forResult(object : OnResultCallbackListener<LocalMedia?> {
            override fun onResult(result: ArrayList<LocalMedia?>) {
                println(result)
                updateAvatar(result[0]?.realPath)
            }

            override fun onCancel() {}
        })
    }

    private fun updateAvatar(path: String?) {
        var file: File = File(path)
        path?.let {
            // 获取图片类型
            val index = file.name.lastIndexOf(".");
            var fileType = file.name.substring(index + 1)
            if (fileType == "jpg") {
                fileType = "jpeg"
            }

            // 创建图片的Part
            val reqFile = RequestBody.create(MediaType.parse("image/" + fileType), file)
            val body = MultipartBody.Part.createFormData("file", file.getName(), reqFile)

            requestUserInfoViewModel.updateAvatar(body)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermissionUtils.verifyPermissions(grantResults)) {
            if (requestCode == takePhotoCode) {
                takePhoto()
            } else if (requestCode == takeAlbumCode) {
                takeAlbum()
            }
        } else {
            appContext.showToast("请打开读写权限")
        }


    }

}
