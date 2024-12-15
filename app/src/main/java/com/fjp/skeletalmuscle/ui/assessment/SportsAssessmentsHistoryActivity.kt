package com.fjp.skeletalmuscle.ui.assessment

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.result.AssessmentHistoryData
import com.fjp.skeletalmuscle.databinding.ActivitySportsAssessmentsHistoryBinding
import com.fjp.skeletalmuscle.ui.assessment.adapter.SportsAssessmentsHistoryAdapter
import com.fjp.skeletalmuscle.ui.assessment.fragment.AssessmentResultFragment
import com.fjp.skeletalmuscle.viewmodel.state.SportsAssessmentsHistoryViewModel
import com.flyco.tablayout.listener.OnTabSelectListener
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.dp2px
import java.time.LocalDate
import kotlin.random.Random

class SportsAssessmentsHistoryActivity : BaseActivity<SportsAssessmentsHistoryViewModel, ActivitySportsAssessmentsHistoryBinding>() {
    var curYear= LocalDate.now().year
    var curMonth= LocalDate.now().monthValue
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.sports_assessment_history_title))
        val fragments = arrayListOf<Fragment>(AssessmentResultFragment.newInstance(curYear,1), AssessmentResultFragment.newInstance(curYear,2), AssessmentResultFragment.newInstance(curYear,3), AssessmentResultFragment.newInstance(curYear,4), AssessmentResultFragment.newInstance(curYear,5), AssessmentResultFragment.newInstance(curYear,6), AssessmentResultFragment.newInstance(curYear,7), AssessmentResultFragment.newInstance(curYear,8), AssessmentResultFragment.newInstance(curYear,9), AssessmentResultFragment.newInstance(curYear,10), AssessmentResultFragment.newInstance(curYear,11), AssessmentResultFragment.newInstance(curYear,12))
        mDatabind.viewpager.adapter = SportsAssessmentsHistoryAdapter(supportFragmentManager, fragments)
        mDatabind.tabLayout.setViewPager(mDatabind.viewpager)
        mDatabind.tabLayout.currentTab = curMonth
        mDatabind.tabLayout.setOnTabSelectListener(object: OnTabSelectListener{
            override fun onTabSelect(position: Int) {
                curMonth = position
            }

            override fun onTabReselect(position: Int) {
            }

        })
        mViewModel.calendarTitle.set("${curYear}年")
        mViewModel.getAssessment(curYear.toString())
    }

    private fun initTestLineChart(assessmentHistory: ArrayList<AssessmentHistoryData>) {
        val lineChart = mDatabind.sportsLineChart
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawBorders(false)
        lineChart.setDrawGridBackground(false)

        lineChart.extraBottomOffset=18f
        lineChart.extraRightOffset=18f
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(true)
        xAxis.textColor=getColor(R.color.color_801c1c1c)
        xAxis.labelCount=10
        xAxis.yOffset=23f
        xAxis.textSize=dp2px(20).toFloat()
        xAxis.enableGridDashedLine(2f, 1f, 0f)
        xAxis.valueFormatter=object: ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return "${(value+1).toInt()}月"
            }
        }

        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.setDrawAxisLine(false)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        leftAxis.labelCount=10
        leftAxis.xOffset=23f
        leftAxis.axisMinimum = 0f
        leftAxis.textColor=ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        leftAxis.textSize=dp2px(20).toFloat()
        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.labelCount=10
        rightAxis.axisMinimum=0f
        rightAxis.xOffset=10f
        rightAxis.textSize=dp2px(20).toFloat()
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.textColor=ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        val values = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()
        val values3 = ArrayList<Entry>()

        for (i in assessmentHistory.indices) {
            values.add(BarEntry(i.toFloat(), assessmentHistory[i].lift_leg.toFloat()))
        }
        for (i in  assessmentHistory.indices) {
            values2.add(BarEntry(i.toFloat(),  assessmentHistory[i].sit_up.toFloat()))
        }

        for (i in assessmentHistory.indices) {
            values3.add(BarEntry(i.toFloat(),  assessmentHistory[i].grip.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency=YAxis.AxisDependency.LEFT
        lineDataSet.color = appContext.getColor(R.color.color_blue)
        // draw selection line as dashed
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillFormatter = IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_blue)
            lineDataSet.fillDrawable = drawable
        } else {
            lineDataSet.fillColor = Color.BLACK
        }

        val lineDataSet2 = LineDataSet(values2, "千卡")
        lineDataSet2.setDrawIcons(false)
        lineDataSet2.mode = LineDataSet.Mode.LINEAR
        lineDataSet2.setDrawCircles(false)
        lineDataSet2.color = appContext.getColor(R.color.color_ffc019)
        lineDataSet2.setDrawCircleHole(false)
        lineDataSet2.setDrawValues(false)
        lineDataSet2.axisDependency=YAxis.AxisDependency.LEFT
        // draw selection line as dashed
        lineDataSet2.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area
        lineDataSet2.setDrawFilled(true)
        lineDataSet2.fillFormatter = IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_yellow)
            lineDataSet2.fillDrawable = drawable
        } else {
            lineDataSet2.fillColor = Color.BLACK
        }

        val lineDataSet3 = LineDataSet(values2, "千卡")
        lineDataSet3.setDrawIcons(false)
        lineDataSet3.mode = LineDataSet.Mode.LINEAR
        lineDataSet3.setDrawCircles(false)
        lineDataSet3.color = appContext.getColor(R.color.color_ff574c)
        lineDataSet3.setDrawCircleHole(false)
        lineDataSet3.setDrawValues(false)
        lineDataSet3.axisDependency=YAxis.AxisDependency.RIGHT
        // draw selection line as dashed
        lineDataSet3.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area
        lineDataSet3.setDrawFilled(true)
        lineDataSet3.fillFormatter = IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_red)
            lineDataSet3.fillDrawable = drawable
        } else {
            lineDataSet3.fillColor = Color.BLACK
        }

        // set color of filled area

        dataSets.add(lineDataSet)
        dataSets.add(lineDataSet2)
        dataSets.add(lineDataSet3)
        val barData = LineData(dataSets)
        lineChart.data = barData
        lineChart.setNoDataText("暂无数据")
        lineChart.animateY(500)
    }
    private fun initWeightLineChart(assessmentHistory: ArrayList<AssessmentHistoryData>) {
        val lineChart = mDatabind.weightLineChart
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawBorders(false)
        lineChart.setDrawGridBackground(false)
        lineChart.extraBottomOffset=18f
        lineChart.extraRightOffset=18f
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(true)
        xAxis.textColor=getColor(R.color.color_801c1c1c)
        xAxis.labelCount=10
        xAxis.yOffset=10f
        xAxis.textSize=dp2px(20).toFloat()
        xAxis.enableGridDashedLine(2f, 1f, 0f)
        xAxis.valueFormatter=object: ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return "${(value+1).toInt()}月"
            }
        }

        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.setDrawAxisLine(false)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        leftAxis.labelCount=10
        leftAxis.xOffset=23f
        leftAxis.axisMinimum = 0f
        leftAxis.textColor=ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        leftAxis.textSize=dp2px(20).toFloat()
        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.labelCount=10
        rightAxis.axisMinimum=0f
        rightAxis.xOffset=23f
        rightAxis.textSize=dp2px(20).toFloat()
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.textColor=ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        val values = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()
        val values3 = ArrayList<Entry>()

        for (i in assessmentHistory.indices) {
            values.add(BarEntry(i.toFloat(), assessmentHistory[i].weight.toFloat()))
        }
        for (i in assessmentHistory.indices) {
            values2.add(BarEntry(i.toFloat(),  assessmentHistory[i].bmi.toFloat()))
        }

        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency=YAxis.AxisDependency.LEFT
        lineDataSet.color = appContext.getColor(R.color.color_23d3f2)
        // draw selection line as dashed
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillFormatter = IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_green)
            lineDataSet.fillDrawable = drawable
        } else {
            lineDataSet.fillColor = Color.BLACK
        }

        val lineDataSet2 = LineDataSet(values2, "千卡")
        lineDataSet2.setDrawIcons(false)
        lineDataSet2.mode = LineDataSet.Mode.LINEAR
        lineDataSet2.setDrawCircles(false)
        lineDataSet.color = appContext.getColor(R.color.color_8e4cff)
        lineDataSet2.setDrawCircleHole(false)
        lineDataSet2.setDrawValues(false)
        lineDataSet2.axisDependency=YAxis.AxisDependency.RIGHT
        // draw selection line as dashed
        lineDataSet2.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area
        lineDataSet2.setDrawFilled(true)
        lineDataSet2.fillFormatter = IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_purple)
            lineDataSet2.fillDrawable = drawable
        } else {
            lineDataSet2.fillColor = Color.BLACK
        }

        // set color of filled area

        dataSets.add(lineDataSet)
        dataSets.add(lineDataSet2)
        val barData = LineData(dataSets)
        lineChart.data = barData
        lineChart.setNoDataText("暂无数据")
        lineChart.animateY(500)
    }

    private fun initWaistlineLineChart(assessmentHistory: ArrayList<AssessmentHistoryData>) {
        val lineChart = mDatabind.waistlineLineChart
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawBorders(false)
        lineChart.extraBottomOffset=18f
        lineChart.extraRightOffset=18f
        lineChart.setDrawGridBackground(false)
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(true)
        xAxis.textColor=getColor(R.color.color_801c1c1c)
        xAxis.labelCount=10
        xAxis.yOffset=10f
        xAxis.textSize=dp2px(20).toFloat()
        xAxis.enableGridDashedLine(2f, 1f, 0f)
        xAxis.valueFormatter=object: ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return "${(value+1).toInt()}月"
            }
        }

        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.setDrawAxisLine(false)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        leftAxis.labelCount=10
        leftAxis.xOffset=23f
        leftAxis.axisMinimum = 0f
        leftAxis.textColor=ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        leftAxis.textSize=dp2px(20).toFloat()
        val rightAxis: YAxis = lineChart.axisRight
        leftAxis.axisMinimum = 0f
        rightAxis.isEnabled=false
        val values = ArrayList<Entry>()

        for (i in assessmentHistory.indices) {
            values.add(BarEntry(i.toFloat(), assessmentHistory[i].waistline.toFloat()))
        }

        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "cm")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.axisDependency=YAxis.AxisDependency.LEFT
        lineDataSet.color = appContext.getColor(R.color.color_blue)
        // draw selection line as dashed
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillFormatter = IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_blue)
            lineDataSet.fillDrawable = drawable
        } else {
            lineDataSet.fillColor = Color.BLACK
        }


        dataSets.add(lineDataSet)
        val barData = LineData(dataSets)
        lineChart.data = barData
        lineChart.setNoDataText("暂无数据")
        lineChart.animateY(500)
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.assessmentHistoryResult.observe(this){
            parseState(it,{assessmentHistory->
                initData(assessmentHistory)

            })
        }
    }

    private fun initData(assessmentHistory: ArrayList<AssessmentHistoryData>) {
      initWaistlineLineChart(assessmentHistory)
        initTestLineChart(assessmentHistory)
        initWeightLineChart(assessmentHistory)
    }

    inner class ProxyClick {

        fun clickPreYear() {
            curYear = DateTimeUtil.getPreYear(curYear,curMonth)
            mViewModel.calendarTitle.set("${curYear}年")
            mViewModel.getAssessment(curYear.toString())
        }

        fun clickNextYear() {
            curYear = DateTimeUtil.getNextYear(curYear,curMonth)
            mViewModel.calendarTitle.set("${curYear}年")
            mViewModel.getAssessment(curYear.toString())
        }
    }

}