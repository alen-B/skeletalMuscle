package com.fjp.skeletalmuscle.ui.main.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.Constants
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
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import me.hgj.jetpackmvvm.base.appContext

class TodaySportsPlankFragment : BaseFragment<TodaySportsPlankViewModel, FragmentTodaySportsPlankBinding>() {
    lateinit var sportFlatSupport: SportFlatSupport

    companion object {
        fun newInstance(sportFlatSupport: SportFlatSupport): TodaySportsPlankFragment {
            val fragment = TodaySportsPlankFragment()
            val bundle = Bundle()
            bundle.putParcelable(Constants.INTENT_KEY_TODAY_SPORTS_DATA, sportFlatSupport)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        sportFlatSupport = arguments?.get(Constants.INTENT_KEY_TODAY_SPORTS_DATA) as SportFlatSupport
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.curScore.set(sportFlatSupport.score.toString())
        mViewModel.sportsTime.set(DateTimeUtil.formSportTime(sportFlatSupport.sport_time))
        mViewModel.heat.set(sportFlatSupport.avg_rate_value.toString())
        mViewModel.calorie.set((sportFlatSupport.sum_calorie / 1000).toString())
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
        lineChart.extraBottomOffset = 18f
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.labelCount = 3
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                if (index >= 0 && index < sportFlatSupport.heart_rate.size) {
                    return DateTimeUtil.formatDate(sportFlatSupport.heart_rate[value.toInt()].record_time.toLong() * 1000, DateTimeUtil.HH_MM_SS)
                } else {
                    return ""
                }
            }

        }
        xAxis.textSize = 20f
        xAxis.enableGridDashedLine(2f, 1f, 0f)
        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.gridLineWidth = 0f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(false)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        leftAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)

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
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius = 4f
        lineDataSet.setCircleColor(ContextCompat.getColor(appContext, R.color.color_ff574c))
        lineDataSet.color = ContextCompat.getColor(appContext, R.color.color_ff574c)
        lineDataSet.setDrawCircleHole(false)

        // text size of values
        lineDataSet.setDrawValues(false)


        // draw selection line as dashed
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area

        // set the filled area
        lineDataSet.setDrawFilled(true)

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
        lineChart.animateY(0)
    }


    private fun initCalorieBarChart() {
        val barChart = mDatabind.calorieBarChat
        barChart.setTouchEnabled(false)
        barChart.isDragEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setDrawBorders(false)
        barChart.setDrawGridBackground(false)
        barChart.extraBottomOffset = 15f
        barChart.extraLeftOffset = 45f
        barChart.extraRightOffset = 45f
        val description = Description()
        description.text = ""
        barChart.description = description
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 20f
        xAxis.labelCount = 2
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value.toInt() >= 0 && value.toInt() < sportFlatSupport.calorie.size) {
                    DateTimeUtil.formatDate(sportFlatSupport.calorie[value.toInt()].record_time.toLong() * 1000, DateTimeUtil.HH_MM_SS)
                } else {
                    ""
                }
            }

        }

        val leftAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.gridLineWidth = 0f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(false)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)

        val rightAxis: YAxis = barChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        barChart.legend.isEnabled = false
        val values = ArrayList<BarEntry>()
        var index = 0f
        for (i in sportFlatSupport.calorie) {
            values.add(BarEntry(index, i.calorie.toFloat()))
            index++
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
        barChart.animateY(0)
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