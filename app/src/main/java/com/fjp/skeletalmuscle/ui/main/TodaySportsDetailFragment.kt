package com.fjp.skeletalmuscle.ui.main

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentTodaySportsDetailBinding
import com.fjp.skeletalmuscle.app.ext.dp
import com.fjp.skeletalmuscle.viewmodel.state.ChartType
import com.fjp.skeletalmuscle.viewmodel.state.TodaySportsDetailFragmentViewModel
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import me.hgj.jetpackmvvm.base.appContext


class TodaySportsDetailFragment : BaseFragment<TodaySportsDetailFragmentViewModel, FragmentTodaySportsDetailBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        initBarChart()
        initHorizontalBarChart()
        initLineChart()
        mDatabind.maxTv.text = getString(R.string.today_sports_detail_max)+" 142"
        mDatabind.averageTv.text = getString(R.string.today_sports_detail_average)+" 111"
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
        barChart.legend.isEnabled = false
        barChart.setTouchEnabled(false)
        barChart.isDragEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setDrawBorders(false)
        barChart.setDrawGridBackground(false)
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
        axisLeft.axisMinimum = 0f
        axisLeft.enableGridDashedLine(2f,1f,0f)
        axisLeft.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        //设置右边轴样式
        val axisRight = barChart.axisRight
        axisRight.isEnabled = false
        axisRight.axisMinimum = 0f

        val values = ArrayList<BarEntry>()
        for (i in 0 until 24) {
            if (i<8 || i >20) {
                values.add(BarEntry(i.toFloat(), 0.0f))
            } else {
                val num = (Math.random() * 30).toFloat()
                values.add(BarEntry(i.toFloat(), num))
            }

        }
        val dataSets = ArrayList<IBarDataSet>()
        val barDataSet = BarDataSet(values, "消耗(千卡)")
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
        horizontalBarChart.setTouchEnabled(false)
        horizontalBarChart.isDragEnabled = false
        horizontalBarChart.setScaleEnabled(false)
        horizontalBarChart.setDrawBorders(false)
        horizontalBarChart.setDrawGridBackground(false)
        horizontalBarChart.isHighlightFullBarEnabled = false
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
        axisLeft.enableGridDashedLine(2f,1f,0f)
        axisLeft.isEnabled = false
        axisLeft.axisMinimum = 0f
        val axisRight = horizontalBarChart.axisRight
//        axisRight.isEnabled = false
        axisRight.setDrawGridLines(false)
        axisRight.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        axisRight.axisMinimum = 0f

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

    private fun initLineChart() {
        val lineChart = mDatabind.lineChart
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
        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.gridLineWidth = 0.5f
        leftAxis.enableGridDashedLine(2f,1f,0f)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)

        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        val values = ArrayList<Entry>()

        for (i in 0 until 8) {
            val num = 110+(Math.random() * 30).toFloat()
            values.add(BarEntry(i.toFloat(), num))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawCircles(false)
        lineDataSet.color = ContextCompat.getColor(appContext, R.color.color_blue)
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


    inner class ProxyClick {

    }
}