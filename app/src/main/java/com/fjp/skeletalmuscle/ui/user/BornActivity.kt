package com.fjp.skeletalmuscle.ui.user

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.DatetimeUtil
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.databinding.ActivityBornBinding
import com.fjp.skeletalmuscle.viewmodel.state.BornViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectType
import java.util.Calendar
import java.util.Date


class BornActivity : BaseActivity<BornViewModel, ActivityBornBinding>() {
    lateinit var pvTime: TimePickerView
    lateinit var curDate: Date
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(resources.getString(R.string.born_title))
        mViewModel.curIndex.set("3")
        mViewModel.totalIndex.set("/10")
        mViewModel.showRightText.set(true)
        initTimePicker()
    }

    private fun initTimePicker() {
        val selectedDate: Calendar = Calendar.getInstance()
        val startDate: Calendar = Calendar.getInstance()
        val endDate: Calendar = Calendar.getInstance()
        //endDate.set(2020,1,1);
        selectedDate.set(1978, 7, 8)
        curDate = selectedDate.time
        startDate.set(1940, 0, 1)

        pvTime = TimePickerBuilder(this) { date, v -> //选中事件回调
            curDate = date
            val formatDate = DatetimeUtil.formatDate(date,DatetimeUtil.DATE_PATTERN)
            val dateArray = formatDate.split("-")
            mViewModel.year.set(dateArray[0])
            mViewModel.month.set(dateArray[1])
            mViewModel.day.set(dateArray[2])
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
            params.rightMargin =0
            pvTime.dialogContainerLayout.layoutParams = params

            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                val lp =dialogWindow.attributes
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

    inner class ProxyClick {
        fun next() {
            val intent = Intent(this@BornActivity, SingleSelectActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.HEIGHT.type)
            startActivity(intent)
        }

        fun finish() {
            this@BornActivity.finish()

        }

        fun showTimePicker() {


            pvTime.show()
        }
    }

    override fun createObserver() {
        super.createObserver()
        App.eventViewModelInstance.finish.observeInActivity(this) {
            finish()
        }
    }
}