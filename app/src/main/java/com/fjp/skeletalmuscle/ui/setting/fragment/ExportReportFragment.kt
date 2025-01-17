package com.fjp.skeletalmuscle.ui.setting.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.util.PDFManager
import com.fjp.skeletalmuscle.data.model.bean.result.ExportData
import com.fjp.skeletalmuscle.data.model.bean.result.ExportSportDumbbell
import com.fjp.skeletalmuscle.data.model.bean.result.ExportSportFlatSupport
import com.fjp.skeletalmuscle.data.model.bean.result.ExportSportLiftLeg
import com.fjp.skeletalmuscle.databinding.FragmentExportReportBinding
import com.fjp.skeletalmuscle.viewmodel.state.ExportReportViewModel
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Div
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.BorderRadius
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.parseState
import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class ExportReportFragment : BaseFragment<ExportReportViewModel, FragmentExportReportBinding> {
    val PERMISSION_REQUEST_CODE = 201
    lateinit var pvTime: TimePickerView
    var endDate: Date = Date()
    lateinit var exportData: ExportData
    lateinit var startDate: Date
    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            showLoading("正在生成PDF...")
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    exportPDF(exportData)
                }
                dismissLoading()
                Toast.makeText(context, "PDF生成成！功路径：${PDFManager.getFilePath()}", Toast.LENGTH_LONG).show()
            }

        }
    }

    constructor() : super()

    companion object {
        fun newInstance() = ExportReportFragment()
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.exportLiveData.observe(this) {
            parseState(it, {
                exportData = it
                handler.sendEmptyMessage(200)
            })

        }
    }

    private fun exportPDF(it: ExportData) {

        try {
            val pageWidth: Float = PageSize.A4.width
            val pageHeight: Float = PageSize.A4.height
            PDFManager.createPDF(requireContext())
            val title = "${App.userInfo.name},骨骼肌运动报告"
            PDFManager.createParagraph(title, TextAlignment.LEFT, 18f)
            PDFManager.add(Paragraph().setWidth(pageWidth / 5 * 2).setHeight(0.5f).setBackgroundColor(DeviceRgb(0, 0, 0)))
//            val icon = BitmapFactory.decodeResource(resources, R.drawable.pdf_icon)
//            val imgData = ImageDataFactory.create(PDFManager.bitmapToBytes(icon))
//            val image = Image(imgData)
//            image.scale(0.5f, 0.5f)
//            image.setWidth(60f)
            // 设置图片位置为右上角
//            val imageWidth: Float = image.getImageScaledWidth()
//            val imageHeight: Float = image.getImageScaledHeight()
//            image.setFixedPosition(pageWidth - imageWidth - 40, pageHeight - imageHeight * 2)
//            PDFManager.addImage(image)
//
            if (mDatabind.curWeekRB.isChecked) {
                PDFManager.createParagraph("报告日期：" + mDatabind.curWeekTv.text, TextAlignment.LEFT, 8f, fontColor = ColorConstants.GRAY)

            } else if (mDatabind.curMonthRB.isChecked) {
                PDFManager.createParagraph("报告日期：" + mDatabind.curMonthTv.text, TextAlignment.LEFT, 8f, fontColor = ColorConstants.GRAY)
            } else {
                PDFManager.createParagraph("报告日期：" + mDatabind.curCustomerStartTv.text + mDatabind.curCustomerEndTv.text, TextAlignment.LEFT, 8f, fontColor = ColorConstants.GRAY)
            }
            val userinfoTab = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
            userinfoTab.setBorder(null)
            userinfoTab.setMarginTop(20f)

            userinfoTab.width = UnitValue.createPercentValue(100f)
            val cell1 = Cell()
            cell1.setBorder(null)
            cell1.setHorizontalAlignment(HorizontalAlignment.LEFT)
            val div1 = Div()
            div1.setWidth(UnitValue.createPercentValue(100f))
            div1.setPadding(10f)
            div1.setMarginRight(10f)
            div1.setBackgroundColor(DeviceRgb(244, 249, 255)).setBorderRadius(BorderRadius(4f))
            div1.add(Paragraph(PDFManager.createText("用户姓名：" + App.userInfo.name + "\n性别：${App.userInfo.sex}" + "\n出生日期：${App.userInfo.birthday}", 11f, DeviceRgb(0, 0, 0))))
            cell1.add(div1)
            val div2 = Div()
            div2.setWidth(UnitValue.createPercentValue(100f))
            div2.setPadding(10f)
            div2.setMarginLeft(10f)
            div2.setBackgroundColor(DeviceRgb(244, 249, 255)).setBorderRadius(BorderRadius(4f))
            div2.add(Paragraph(PDFManager.createText("身高：${App.userInfo.height}cm" + "\n体重：${App.userInfo.weight}kg" + "\n腰围：${App.userInfo.waistline}cm", 11f, DeviceRgb(0, 0, 0))))

            val cell2 = Cell()
            cell2.add(div2)
            cell2.setBorder(null)
            cell2.setHorizontalAlignment(HorizontalAlignment.RIGHT)
            userinfoTab.addCell(cell1)
            userinfoTab.addCell(cell2)
            PDFManager.add(userinfoTab)

            val allSportstable = Table(5, true)
            allSportstable.setBorder(null)
            allSportstable.setMarginTop(40f)
            val runIcon = BitmapFactory.decodeResource(resources, R.drawable.pdf_run)
            val runImgData = ImageDataFactory.create(PDFManager.bitmapToBytes(runIcon))
            val run = Image(runImgData)
            run.setAutoScale(true)
            run.setHeight(40f)
            allSportstable.addCell(Cell().add(run).setBorder(null))
            allSportstable.addCell(PDFManager.createCellAndBackground(Text("平均得分 ").setFontSize(10f).setFontColor(ColorConstants.GRAY), Text(it.score.toString() + "分").setFontSize(12f).setFontColor(DeviceRgb(30, 157, 144)), cellBackGround = DeviceRgb(244, 249, 255), isFirst = true, isLast = false).setMarginLeft(40f))
            allSportstable.addCell(PDFManager.createCellAndBackground(Text("运动时长 ").setFontSize(10f).setFontColor(ColorConstants.GRAY), Text(DateTimeUtil.formatExportTime(it.sport_time.toLong())).setFontSize(12f).setFontColor(ColorConstants.BLACK), cellBackGround = DeviceRgb(244, 249, 255), isFirst = false, isLast = false))
            allSportstable.addCell(PDFManager.createCellAndBackground(Text("消耗卡路里 ").setFontSize(10f).setFontColor(ColorConstants.GRAY), Text("${it.calorie_total / 1000f}千卡").setFontSize(12f).setFontColor(ColorConstants.BLACK), cellBackGround = DeviceRgb(244, 249, 255), isFirst = false, isLast = false))
            allSportstable.addCell(PDFManager.createCellAndBackground(Text("平均心率 ").setFontSize(10f).setFontColor(ColorConstants.GRAY), Text(it.avg_rate_value.toString()).setFontColor(ColorConstants.BLACK).setFontSize(12f), cellBackGround = DeviceRgb(244, 249, 255), isFirst = false, isLast = true))
            PDFManager.add(allSportstable)
            if (it.sport_lift_leg != null) {
                PDFManager.createParagraph(Text("高抬腿运动 ").setFontSize(12f).setFontColor(ColorConstants.BLACK), Text(" " + it.sport_lift_leg.score.toString()).setFontSize(12f).setFontColor(DeviceRgb(30, 157, 144)))
                val highLegTab = Table(UnitValue.createPercentArray(floatArrayOf(30f, 30f, 30f, 30f,30f,30f)))
                highLegTab.setBorder(null)
                // 添加表头行
//                highLegTab.addHeaderCell(PDFManager.createCell("平均得分"))
                highLegTab.addHeaderCell(PDFManager.createCell("运动时长", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
                highLegTab.addHeaderCell(PDFManager.createCell("消耗卡路里", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
                highLegTab.addHeaderCell(PDFManager.createCell("运动总数", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
                highLegTab.addHeaderCell(PDFManager.createCell("左腿运动量", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
                highLegTab.addHeaderCell(PDFManager.createCell("右腿运动量", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
                highLegTab.addHeaderCell(PDFManager.createCell("心肺能力", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
//                highLegTab.addCell(PDFManager.createCell(it.sport_lift_leg.score.toString()))
                highLegTab.addCell(PDFManager.createCell(DateTimeUtil.sceond2Min(it.sport_lift_leg.sport_time.toLong()) + "分钟",textAligment = TextAlignment.LEFT).setWidth(80f))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.sum_calorie / 1000}千卡",textAligment = TextAlignment.LEFT).setWidth(80f))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.left_times+it.sport_lift_leg.right_times}次",textAligment = TextAlignment.LEFT).setWidth(80f))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.left_times}次",textAligment = TextAlignment.LEFT).setWidth(80f))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.right_times}次",textAligment = TextAlignment.LEFT).setWidth(80f))
                highLegTab.addCell(PDFManager.createCell("${it.sport_lift_leg.cardiorespiratory_endurance}",textAligment = TextAlignment.LEFT).setWidth(80f))
                PDFManager.add(highLegTab)
                PDFManager.createLine()
            }
            if (it.sport_dumbbell != null) {
                PDFManager.createParagraph(Text("哑铃运动 ").setFontSize(12f).setFontColor(ColorConstants.BLACK), Text(" " + it.sport_dumbbell.score.toString()).setFontSize(12f).setFontColor(DeviceRgb(30, 157, 144)))
                val dumbbellTab = Table(UnitValue.createPercentArray(floatArrayOf(80f, 80f, 80f, 80f, 80f)))
                dumbbellTab.setBorder(null)
//                dumbbellTab.addHeaderCell(PDFManager.createCell("平均得分"))
                dumbbellTab.addHeaderCell(PDFManager.createCell("运动时长", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
                dumbbellTab.addHeaderCell(PDFManager.createCell("消耗卡路里", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
                dumbbellTab.addHeaderCell(PDFManager.createCell("上举次数", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
                dumbbellTab.addHeaderCell(PDFManager.createCell("扩胸次数", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
                dumbbellTab.addHeaderCell(PDFManager.createCell("哑铃重量", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))

//                dumbbellTab.addCell(PDFManager.createCell(it.sport_dumbbell.score.toString()))
                dumbbellTab.addCell(PDFManager.createCell(DateTimeUtil.sceond2Min(it.sport_dumbbell.sport_time.toLong()) + "分钟",textAligment = TextAlignment.LEFT).setWidth(80f))
                dumbbellTab.addCell(PDFManager.createCell("${it.sport_dumbbell.sum_calorie / 1000}千卡",textAligment = TextAlignment.LEFT).setWidth(80f))
                dumbbellTab.addCell(PDFManager.createCell("${it.sport_dumbbell.up_times}次",textAligment = TextAlignment.LEFT).setWidth(80f))
                dumbbellTab.addCell(PDFManager.createCell("${it.sport_dumbbell.expand_chest_times}次",textAligment = TextAlignment.LEFT).setWidth(80f))
                dumbbellTab.addCell(PDFManager.createCell(it.sport_dumbbell.weight.toString() + "kg",textAligment = TextAlignment.LEFT).setWidth(80f))

                PDFManager.add(dumbbellTab)
                PDFManager.createLine()
            }
            if (it.sport_flat_support != null) {
                PDFManager.createParagraph(Text("平板支撑运动 ").setFontSize(12f).setFontColor(ColorConstants.BLACK), Text(" " + it.sport_flat_support.score.toString()).setFontSize(12f).setFontColor(DeviceRgb(30, 157, 144)))
                val plankTab = Table(UnitValue.createPercentArray(2))
                plankTab.setBorder(null)
//                plankTab.addHeaderCell(PDFManager.createCell("平均得分"))
                plankTab.addHeaderCell(PDFManager.createCell("运动时长", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))
                plankTab.addHeaderCell(PDFManager.createCell("消耗卡路里", color = ColorConstants.GRAY,textAligment = TextAlignment.LEFT).setWidth(80f))

//                plankTab.addCell(PDFManager.createCell(it.sport_flat_support.score.toString())) 30 157 144
                plankTab.addCell(PDFManager.createCell(DateTimeUtil.sceond2Min(it.sport_flat_support.sport_time.toLong()) + "分钟",textAligment = TextAlignment.LEFT).setWidth(80f))
                plankTab.addCell(PDFManager.createCell("${it.sport_flat_support.sum_calorie / 1000}千卡",textAligment = TextAlignment.LEFT).setWidth(80f))
                PDFManager.add(plankTab)
                PDFManager.createLine()
            }

            PDFManager.close()
        } catch (e: IOException) {
            e.printStackTrace()
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
                mDatabind.curCustomerEndTv.text = " - $formatDate"
            }


        }.setType(booleanArrayOf(true, true, true, false, false, false)) // 默认全部显示
            .setCancelText(getString(R.string.cancel)) //取消按钮文字
            .setSubmitText(getString(R.string.sure)) //确认按钮文字
            .setContentTextSize(28) //滚轮文字大小
            .setTitleSize(30) //标题文字大小
            .setTitleText("") //标题文字
            .setOutSideCancelable(false) //点击屏幕，点在控件外部范围时，是否取消显示
            .isCyclic(false) //是否循环滚动
            .setTitleColor(android.graphics.Color.BLACK) //标题文字颜色
            .setSubmitColor(android.graphics.Color.BLUE) //确定按钮文字颜色
            .setCancelColor(android.graphics.Color.BLUE) //取消按钮文字颜色
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