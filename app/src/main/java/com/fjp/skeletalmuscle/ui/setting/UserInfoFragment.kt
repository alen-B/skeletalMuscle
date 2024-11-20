package com.fjp.skeletalmuscle.ui.setting

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.util.DatetimeUtil
import com.fjp.skeletalmuscle.app.weight.pop.SingleSelectedPop
import com.fjp.skeletalmuscle.app.weight.pop.TakePhotoPop
import com.fjp.skeletalmuscle.databinding.FragmentUserInfoBinding
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectType
import com.fjp.skeletalmuscle.viewmodel.state.UserInfoViewModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class UserInfoFragment : BaseFragment<UserInfoViewModel, FragmentUserInfoBinding>() {

    companion object {
        fun newInstance() = UserInfoFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.userNameLayout.setValue(App.userInfo.name)
        mDatabind.phoneLayout.setValue(App.userInfo.phone)
        mDatabind.bornLayout.setValue(DatetimeUtil.formatDate(App.userInfo.born, DatetimeUtil.DATE_PATTERN))
        var sex = getString(R.string.setting_sex_man)
        if (App.userInfo.sex == 0) {
            sex = getString(R.string.setting_sex_woman)
        }
        mDatabind.sexLayout.setValue(sex)
        mDatabind.weightLayout.setValue(App.userInfo.weight)
        mDatabind.heightLayout.setValue(App.userInfo.height)
        mDatabind.waistLinLayout.setValue(App.userInfo.waist_line)
    }

    override fun createObserver() {
        super.createObserver()
        eventViewModel.updateUserNameEvent.observeInFragment(this) {
            mDatabind.userNameLayout.setValue(it)
        }
        eventViewModel.updatePhoneEvent.observeInFragment(this) {
            mDatabind.phoneLayout.setValue(it)
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
    }

    lateinit var pvTime: TimePickerView
    lateinit var curDate: Date
    fun initTimePickerrDialog() {
        val startDate: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        val endDate: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        endDate.set(2020, 1, 1);
        startDate.set(1940, 0, 1, 12, 0, 0)
        val selectedDate: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        selectedDate.timeInMillis = App.userInfo.born
        pvTime = TimePickerBuilder(context) { date, v -> //选中事件回调
            curDate = date
            App.userInfo.born = date.time
            mDatabind.bornLayout.setValue(DatetimeUtil.formatDate(App.userInfo.born, DatetimeUtil.DATE_PATTERN))
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
                        App.userInfo.height = data
                        mDatabind.heightLayout.setValue(data)
                    } else if (type == SingleSelectType.WEIGHT) {
                        App.userInfo.weight = data
                        mDatabind.weightLayout.setValue(data)


                    } else if (type == SingleSelectType.WAIST_LINE) {
                        App.userInfo.waist_line = data
                        mDatabind.waistLinLayout.setValue(data)
                    } else if (type == SingleSelectType.SEX) {
                        if (data.equals(getString(R.string.setting_sex_man))) {
                            App.userInfo.sex = 1
                        } else {
                            App.userInfo.sex = 0
                        }
                        mDatabind.sexLayout.setValue(data)
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
                        takePhoto()
                    } else if (index == TakePhotoPop.ALBUM) {
                        takePicker()
                    }
                    pop.dismiss()
                }

            }))

        pop.show()
    }

    fun takePicker() {
        PictureSelector.create(this).openSystemGallery(SelectMimeType.ofImage()).forSystemResult(object : OnResultCallbackListener<LocalMedia?> {
            override fun onResult(result: ArrayList<LocalMedia?>) {
                println(result)
            }

            override fun onCancel() {

            }
        })
    }

    fun takePhoto() {
        PictureSelector.create(this).openCamera(SelectMimeType.ofImage()).forResult(object : OnResultCallbackListener<LocalMedia?> {
            override fun onResult(result: ArrayList<LocalMedia?>) {
                println(result)
            }

            override fun onCancel() {}
        })
    }
}