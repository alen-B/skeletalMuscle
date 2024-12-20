package com.fjp.skeletalmuscle.ui.main.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.result.SportFlatSupport
import com.fjp.skeletalmuscle.databinding.FragmentTodaySportsPlankBinding
import com.fjp.skeletalmuscle.ui.main.TodaySportsDetailActivity
import com.fjp.skeletalmuscle.viewmodel.state.ChartType
import com.fjp.skeletalmuscle.viewmodel.state.TodaySportsPlankViewModel
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import me.hgj.jetpackmvvm.base.appContext

class TodaySportsPlankFragment(val sportFlatSupport: SportFlatSupport) : BaseFragment<TodaySportsPlankViewModel, FragmentTodaySportsPlankBinding>() {

    companion object {
        fun newInstance(sportFlatSupport: SportFlatSupport) = TodaySportsPlankFragment(sportFlatSupport)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.curScore.set(sportFlatSupport.score.toString())
        mViewModel.curScore.set(sportFlatSupport.score.toString())
        mViewModel.sportsTime.set(DateTimeUtil.formSportTime(sportFlatSupport.end_time - sportFlatSupport.start_time))
        mViewModel.heat.set(sportFlatSupport.avg_rate_value.toString())
        mViewModel.calorie.set(sportFlatSupport.sum_calorie.toString())
        initCalorieBarChart()
        initHeartRateLineChart()
    }

    private fun initHeartRateLineChart() {
        val lineChart = mDatabind.heartRateLineChart
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawBorders(false)
        lineChart.setDrawGridBackground(false)
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.enableGridDashedLine(2f, 1f, 0f)
        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.gridLineWidth = 0f
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(false)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        leftAxis.textColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)

        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        val values = ArrayList<Entry>()

        for (i in sportFlatSupport.heart_rate.indices) {
            values.add(BarEntry(i.toFloat(), sportFlatSupport.heart_rate[i].rate_value.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        lineDataSet.setDrawCircles(false)
        lineDataSet.color = ContextCompat.getColor(appContext, R.color.color_ff574c)
        lineDataSet.setDrawCircleHole(false)

        // text size of values
        lineDataSet.setDrawValues(false)


        // draw selection line as dashed
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area

        // set the filled area
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillFormatter = IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }

        // set color of filled area

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_red)
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


    private fun initCalorieBarChart() {
        val barChart = mDatabind.calorieBarChat
        barChart.setTouchEnabled(false)
        barChart.isDragEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setDrawBorders(false)
        barChart.setDrawGridBackground(false)
        val description = Description()
        description.text = ""
        barChart.description = description
        val xAxis = barChart.xAxis
        xAxis.isEnabled = false

        val leftAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.gridLineWidth = 0f
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(false)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)

        val rightAxis: YAxis = barChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        barChart.legend.isEnabled = false
        val values = ArrayList<BarEntry>()

        for (i in sportFlatSupport.calorie.indices) {
            values.add(BarEntry(i.toFloat(), sportFlatSupport.calorie[i].calorie.toFloat()))

        }
        val dataSets = ArrayList<IBarDataSet>()
        val barDataSet = BarDataSet(values, "千卡")
        dataSets.add(barDataSet)
        barDataSet.setDrawIcons(false)
        barDataSet.color = ContextCompat.getColor(appContext, R.color.color_blue)
        barDataSet.setDrawValues(false)//不显示柱状图上数据
        val barData = BarData(dataSets)
        barData.barWidth = 0.2f
        barChart.data = barData
        barChart.setNoDataText("暂无数据")
        barChart.animateY(500)
    }


    inner class ProxyClick {
        fun clickCalorie() {
            TodaySportsDetailActivity.startActivity(requireContext(), SportsType.PLANK, ChartType.BURN_CALORIES)
        }

        fun clickHeartRate() {
            TodaySportsDetailActivity.startActivity(requireContext(), SportsType.PLANK, ChartType.HEART_RATE_TREND)
        }

    }
}