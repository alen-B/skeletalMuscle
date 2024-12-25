package com.fjp.skeletalmuscle.ui.setting.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.util.PDFManager
import com.fjp.skeletalmuscle.data.model.bean.result.ExportData
import com.fjp.skeletalmuscle.databinding.FragmentExportReportBinding
import com.fjp.skeletalmuscle.viewmodel.state.ExportReportViewModel
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.parseState
import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class ExportReportFragment : BaseFragment<ExportReportViewModel, FragmentExportReportBinding>() {
    val PERMISSION_REQUEST_CODE = 201
    lateinit var pvTime: TimePickerView
    var endDate: Date = Date()
    lateinit var startDate: Date

    companion object {
        fun newInstance() = ExportReportFragment()
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.exportLiveData.observe(this) {
            parseState(it, {
                exportPDF(it)
            })
        }
    }

    private fun exportPDF(it: ExportData) {

        try {
            PDFManager.createPDF(requireContext())
            if (mDatabind.curWeekRB.isChecked) {
                PDFManager.createParagraph("报告日期：" + mDatabind.curWeekTv.text, TextAlignment.RIGHT, 6f)

            } else if (mDatabind.curMonthRB.isChecked) {
                PDFManager.createParagraph("报告日期：" + mDatabind.curMonthTv.text, TextAlignment.RIGHT, 6f)
            } else {
                PDFManager.createParagraph("报告日期：" + mDatabind.curCustomerStartTv.text + mDatabind.curCustomerEndTv.text, TextAlignment.RIGHT, 6f)
            }
            var title = "${App.userInfo.name}爷爷，骨骼肌运动报告"
            if (App.userInfo.sex == requireContext().getString(R.string.setting_sex_woman)) {
                title = "${App.userInfo.name}奶奶的，骨骼肌运动报告"
            }
            PDFManager.createParagraph(title, TextAlignment.CENTER, 25f)
            PDFManager.createLine()
            PDFManager.createParagraph("姓名：" + App.userInfo.name + "\n性别：${App.userInfo.sex}" + "\n出生日期：${App.userInfo.birthday}" + "\n身高：${App.userInfo.height}" + "\n体重：${App.userInfo.weight}" + "\n腰围：${App.userInfo.waistline}")
            PDFManager.createParagraph("运动总结", TextAlignment.LEFT)
            // 创建表格对象，此处设置表格列数为3
            val table = Table(UnitValue.createPercentArray(4))
            // 添加表头行
            table.addHeaderCell(PDFManager.createCell("平均得分"))
            table.addHeaderCell(PDFManager.createCell("运动时长"))
            table.addHeaderCell(PDFManager.createCell("消耗卡路里"))
            table.addHeaderCell(PDFManager.createCell("平均心率"))
            table.addCell(PDFManager.createCell(it.score.toString()))
            table.addCell(PDFManager.createCell(DateTimeUtil.formatExportTime(it.sport_time.toLong())))
            table.addCell(PDFManager.createCell("${it.calorie_total / 1000}千卡"))
            table.addCell(PDFManager.createCell(it.avg_rate_value.toString()))
            PDFManager.add(table)
            if (it.sport_lift_leg != null) {
                PDFManager.createParagraph("高抬腿运动")
                val highLegTab = Table(UnitValue.createPercentArray(8))
                // 添加表头行
                highLegTab.addHeaderCell(PDFManager.createCell("平均得分"))
                highLegTab.addHeaderCell(PDFManager.createCell("运动时长"))
                highLegTab.addHeaderCell(PDFManager.createCell("消耗卡路里"))
                highLegTab.addHeaderCell(PDFManager.createCell("平均心率"))
                highLegTab.addHeaderCell(PDFManager.createCell("暖身激活"))
                highLegTab.addHeaderCell(PDFManager.createCell("高效燃脂"))
                highLegTab.addHeaderCell(PDFManager.createCell("心肺提升"))
                highLegTab.addHeaderCell(PDFManager.createCell("极限突破"))
                highLegTab.addCell(PDFManager.createCell(it.sport_lift_leg.score.toString()))
                highLegTab.addCell(PDFManager.createCell(DateTimeUtil.formatExportTime(it.sport_lift_leg.sport_time.toLong())))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.sum_calorie / 1000}千卡"))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.avg_rate_value}"))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.warm_up_activation}"))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.efficient_grease_burning}"))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.heart_lung_enhancement}"))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.extreme_breakthrough}"))
                PDFManager.add(highLegTab)
            }

            if (it.sport_dumbbell!=null){
                PDFManager.createParagraph("哑铃运动")
                val dumbbellTab = Table(UnitValue.createPercentArray(5))
                dumbbellTab.addHeaderCell(PDFManager.createCell("平均得分"))
                dumbbellTab.addHeaderCell(PDFManager.createCell("运动时长"))
                dumbbellTab.addHeaderCell(PDFManager.createCell("上举次数"))
                dumbbellTab.addHeaderCell(PDFManager.createCell("扩胸次数"))
                dumbbellTab.addHeaderCell(PDFManager.createCell("哑铃重量"))

                dumbbellTab.addCell(PDFManager.createCell(it.sport_dumbbell.score.toString()))
                dumbbellTab.addCell(PDFManager.createCell(DateTimeUtil.formatExportTime(it.sport_dumbbell.sport_time.toLong())))
                dumbbellTab.addCell(PDFManager.createCell(it.sport_dumbbell.up_times.toString()))
                dumbbellTab.addCell(PDFManager.createCell(it.sport_dumbbell.expand_chest_times.toString()))
                dumbbellTab.addCell(PDFManager.createCell(it.sport_dumbbell.weight.toString() + "kg"))

                PDFManager.add(dumbbellTab)
            }

            if(it.sport_flat_support!=null){
                PDFManager.createParagraph("平板支撑运动")
                val plankTab = Table(UnitValue.createPercentArray(3))
                plankTab.addHeaderCell(PDFManager.createCell("平均得分"))
                plankTab.addHeaderCell(PDFManager.createCell("运动时长"))
                plankTab.addHeaderCell(PDFManager.createCell("消耗卡路里"))

                plankTab.addCell(PDFManager.createCell(it.sport_flat_support.score.toString()))
                plankTab.addCell(PDFManager.createCell(DateTimeUtil.formatExportTime(it.sport_flat_support.sport_time.toLong())))
                plankTab.addCell(PDFManager.createCell("${it.sport_flat_support.sum_calorie / 1000}千卡"))

                PDFManager.add(plankTab)
            }

            PDFManager.close()
            dismissLoading()
            Toast.makeText(context, "PDF生成成！功路径：${PDFManager.getFilePath()}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            dismissLoading()
        }
    }

    inner class ProxyClick {

        fun clickExport() {
            if (checkPermission()) {
                var startTime: Long = 0
                var endTime: Long = 0
                if (mDatabind.curWeekRB.isChecked) {
                    startTime = DateTimeUtil.getFirstDayTimeOfWeek()
                    endTime = System.currentTimeMillis()

                } else if (mDatabind.curMonthRB.isChecked) {
                    startTime = DateTimeUtil.getFirstDayTimeOfMonth()
                    endTime = System.currentTimeMillis()
                } else {
                    startTime = startDate.time
                    endTime = endDate.time
                }
                showLoading("正在导出pdf文件....")
                mViewModel.getTodayData(startTime / 1000, endTime / 1000)
            }

        }

        fun clickCustomerStart() {
            showTimePicker(startDate, true)
        }

        fun clickCustomerEnd() {
            showTimePicker(endDate, false)
        }
    }


    fun checkPermission(): Boolean {
        // 检查写外部存储权限是否已授予
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 若未授予权限，则发起权限申请
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            return false
        } else {
            // 权限已授予，可直接调用生成PDF方法
            return true
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        initStartDate()
        mDatabind.curCustomerStartTv.text = DateTimeUtil.formatDate(startDate, DateTimeUtil.DATE_PATTERN2)
        mDatabind.curCustomerEndTv.text = " - ${DateTimeUtil.formatDate(endDate, DateTimeUtil.DATE_PATTERN2)}"
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