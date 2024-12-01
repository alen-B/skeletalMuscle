package com.fjp.skeletalmuscle.ui.setting.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.databinding.FragmentExportReportBinding
import com.fjp.skeletalmuscle.viewmodel.state.ExportReportViewModel
import me.hgj.jetpackmvvm.base.appContext
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class ExportReportFragment : BaseFragment<ExportReportViewModel, FragmentExportReportBinding>() {
    lateinit var pvTime: TimePickerView
    var endDate: Date = Date()
    lateinit var startDate: Date

    companion object {
        fun newInstance() = ExportReportFragment()
    }

    inner class ProxyClick {

        fun clickExport() {

        }

        fun clickCustomerStart() {
            showTimePicker(startDate, true)
        }

        fun clickCustomerEnd() {
            showTimePicker(endDate, false)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        initStartDate()
        mDatabind.curCustomerStartTv.text = DateTimeUtil.formatDate(startDate, DateTimeUtil.DATE_PATTERN2)
        mDatabind.curCustomerEndTv.text = "-" + DateTimeUtil.formatDate(endDate, DateTimeUtil.DATE_PATTERN2)
        mDatabind.curWeekTv.text = DateTimeUtil.getCurWeek()
        mDatabind.curMonthTv.text = DateTimeUtil.getCurMonth()
        mDatabind.curWeekRB.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                mDatabind.customerTimeRB.isChecked = false
                mDatabind.curMonthRB.isChecked = false
            }
        }
        mDatabind.curMonthRB.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                mDatabind.customerTimeRB.isChecked = false
                mDatabind.curWeekRB.isChecked = false
            }
        }
        mDatabind.customerTimeRB.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                mDatabind.curMonthRB.isChecked = false
                mDatabind.curWeekRB.isChecked = false
            }
        }
    }

    private fun initStartDate() {
        val calendar = Calendar.getInstance()
        calendar.time = endDate
        calendar.add(Calendar.MONTH, -2)
        startDate = calendar.time
    }

    private fun showTimePicker(curDate: Date, isStart: Boolean) {
        val selectedCalendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        val starCalendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        val endCalendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        selectedCalendar.time = curDate
        starCalendar.set(2024, 10, 19, 12, 0, 0)
        pvTime = TimePickerBuilder(context) { date, v -> //选中事件回调
            val formatDate = DateTimeUtil.formatDate(date, DateTimeUtil.DATE_PATTERN2)
            if (isStart) {
                if (date.time > endDate.time) {
                    appContext.showToast(getString(R.string.setting_export_start_time_than_end_time))
                    return@TimePickerBuilder
                }
                startDate = date
                mDatabind.curCustomerStartTv.text = formatDate
            } else {
                if (date.time < startDate.time) {
                    appContext.showToast(getString(R.string.setting_export_end_time_less_start_time))
                    return@TimePickerBuilder
                }
                endDate = date
                mDatabind.curCustomerEndTv.text = "-$formatDate"
            }


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
            .setDate(selectedCalendar) // 如果不设置的话，默认是系统时间*/
            .setOutSideCancelable(true).setRangDate(starCalendar, endCalendar) //起始终止年月日设定
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
        mDialog.show()

    }

}