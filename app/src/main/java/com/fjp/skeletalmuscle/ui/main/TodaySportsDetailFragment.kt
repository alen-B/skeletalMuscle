package com.fjp.skeletalmuscle.ui.main

import android.os.Bundle
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentTodaySportsDetailBinding
import com.fjp.skeletalmuscle.ext.dp
import com.fjp.skeletalmuscle.viewmodel.state.ChartType
import com.fjp.skeletalmuscle.viewmodel.state.TodaySportsDetailFragmentViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import me.hgj.jetpackmvvm.base.appContext


class TodaySportsDetailFragment : BaseFragment<TodaySportsDetailFragmentViewModel, FragmentTodaySportsDetailBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        initBarChart()
        initHorizontalBarChart()
        initLineChart()
    }

    private fun initLineChart() {

    }

    private fun getFormatterData(type: ChartType): Array<String> {
        return if (type == ChartType.BarChart || type == ChartType.LineChart) {
            val formatterData = Array(24) { "00:00" }
            for (i: Int in 1..24) {
                if (i < 10) {
                    formatterData[i - 1] = "0${i}:00"
                } else {
                    formatterData[i - 1] = "${i}:00"
                }
            }
            formatterData
        } else {
            arrayOf(getString(R.string.today_sports_detail_break_through), getString(R.string.today_sports_detail_heart_enhancement), getString(R.string.today_sports_detail_fat_burn), getString(R.string.today_sports_detail_active))
        }

    }
    private fun initBarChart() {
        val barChart = mDatabind.barChart
        //xAxis 设置底部样式
        val xAxis = mDatabind.barChart.xAxis
        xAxis.mAxisMinimum = 0f
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textSize = 18.dp
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)

        val dayFormatterData = getFormatterData(ChartType.LineChart)
        //设置底部文字显示格式化
        xAxis.valueFormatter = IndexAxisValueFormatter(dayFormatterData)
        //设置左边轴样式
        val axisLeft = barChart.axisLeft
        axisLeft.textColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        axisLeft.textSize = 18.dp
        axisLeft.axisMinimum =0f
        axisLeft.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        //设置右边轴样式
        val axisRight = barChart.axisRight
        axisRight.isEnabled = false
        axisRight.axisMinimum =0f

        val values = ArrayList<BarEntry>()
        for (i in 0 until 24) {
            if (i == 3 || i == 5) {
                values.add(BarEntry(i.toFloat(), 0.0f))
            } else {
                val num = (Math.random() * 180).toFloat()
                values.add(BarEntry(i.toFloat(), num))
            }

        }
        val dataSets = ArrayList<IBarDataSet>()
        val barDataSet = BarDataSet(values, "消耗(千卡)")
        barChart.legend.isEnabled = false
        dataSets.add(barDataSet)
        barDataSet.setDrawIcons(false)
        barDataSet.color = ContextCompat.getColor(appContext, R.color.color_blue)
        barDataSet.setDrawValues(false)//不显示柱状图上数据
        val barData = BarData(dataSets)
        barData.barWidth = 0.2f
        barChart.data = barData
        barChart.setNoDataText("暂无数据")
        barChart.animateY(500)
        val description = Description()
        description.text = ""
        barChart.description = description
    }
    private fun initHorizontalBarChart() {
        val horizontalBarChart = mDatabind.horizontalBarChart
        horizontalBarChart.legend.isEnabled = false
        horizontalBarChart.isHighlightFullBarEnabled=false
        val xAxis = horizontalBarChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_cc1c1c1c)
        xAxis.textSize = 24.dp
        xAxis.labelCount = 4
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        val dayFormatterData = getFormatterData(ChartType.HBarChart)
        xAxis.valueFormatter = IndexAxisValueFormatter(dayFormatterData)
        val axisLeft = horizontalBarChart.axisLeft
        axisLeft.isEnabled = false
        axisLeft.axisMinimum =0f
        val axisRight = horizontalBarChart.axisRight
//        axisRight.isEnabled = false
        axisRight.setDrawGridLines(false)
        axisRight.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        axisRight.axisMinimum =0f

        val values = ArrayList<BarEntry>()
        for (i in 0 until 4) {
            val num = (Math.random() * 180).toFloat()
            values.add(BarEntry(i.toFloat(), num))

        }

        val dataSets = ArrayList<IBarDataSet>()
        val barDataSet = BarDataSet(values, "消耗(千卡)")
        dataSets.add(barDataSet)
        barDataSet.setDrawIcons(false)
        barDataSet.colors = arrayListOf(ContextCompat.getColor(appContext, R.color.color_ff574c), ContextCompat.getColor(appContext, R.color.color_ff824c), ContextCompat.getColor(appContext, R.color.color_ffc019), ContextCompat.getColor(appContext, R.color.color_blue))
        barDataSet.setDrawValues(true)//不显示柱状图上数据
//        barDataSet.valueFormatter = object:ValueFormatter(){
//            override fun getFormattedValue(value: Float): String {
//                return value.toString()+"min"
//            }
//        }
        val barData = BarData(dataSets)
        barData.barWidth = 0.4f
        horizontalBarChart.data = barData
        val description = Description()
        description.text = ""//去掉描述，必须设置为空字符串，要不然还会显示
        horizontalBarChart.description = description
        horizontalBarChart.setNoDataText("暂无数据")
        horizontalBarChart.animateY(500)

    }




    inner class ProxyClick {

    }
}