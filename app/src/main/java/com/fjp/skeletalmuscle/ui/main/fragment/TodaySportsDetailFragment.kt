package com.fjp.skeletalmuscle.ui.main.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.ext.dp
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.result.HeartRateResult
import com.fjp.skeletalmuscle.data.model.bean.result.LiftLegTrendResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendCalorieResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendDumbbellExpandChestAndUpResult
import com.fjp.skeletalmuscle.data.model.bean.result.SportTrendLiftLegSportTimeResult
import com.fjp.skeletalmuscle.databinding.FragmentTodaySportsDetailBinding
import com.fjp.skeletalmuscle.viewmodel.request.RequestTodaySportsDetailFragmentViewModel
import com.fjp.skeletalmuscle.viewmodel.state.ChartType
import com.fjp.skeletalmuscle.viewmodel.state.DateType
import com.fjp.skeletalmuscle.viewmodel.state.TodaySportsDetailFragmentViewModel
import com.github.mikephil.charting.charts.LineChart
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
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.parseState


class TodaySportsDetailFragment(val sportsType: SportsType, val type: Int, val dateType: DateType) : BaseFragment<TodaySportsDetailFragmentViewModel, FragmentTodaySportsDetailBinding>() {
    val request: RequestTodaySportsDetailFragmentViewModel by viewModels()

    companion object {
        fun newInstance(sportsType: SportsType, chartType: Int, dateType: DateType) = TodaySportsDetailFragment(sportsType, chartType, dateType)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        when (type) {
            ChartType.BURN_CALORIES.type -> {
                showCaloriesView()
                if (sportsType == SportsType.HIGH_KNEE) {
                    request.getLiftLegCalorie(dateType.value)
                } else if (sportsType == SportsType.DUMBBELL) {
                    request.getSportTrendDumbbellCalorie(dateType.value)
                } else if (sportsType == SportsType.PLANK) {
                    request.getSportTrendFlatSupportCalorie(dateType.value)
                }
            }

            ChartType.HEART_RATE_TREND.type -> {
                showHeartRatetrendView()
                if (sportsType == SportsType.HIGH_KNEE) {
                    request.getLiftLegHeartRate(dateType.value)
                } else if (sportsType == SportsType.DUMBBELL) {
                    request.getSportTrendDumbbellHeartRate(dateType.value)
                } else if (sportsType == SportsType.PLANK) {
                    request.getSportTrendFlatSupportHeartRate(dateType.value)
                }
            }

            ChartType.LEG_LIFTING_ANGLE.type -> {
                showLegAngleView()
                request.getSportTrendLiftLeg(dateType.value)

            }

            ChartType.INTENSITY_AND_TIME.type -> {
                mDatabind.horizontalBarChart.visibility = View.VISIBLE
                request.getSportTrendLiftLegSportTime(dateType.value)
            }

            ChartType.All_CALORIES.type -> {
                request.getSportTrendCalorie(dateType.value)
                showCaloriesView()
            }

            ChartType.DUMBBELL_AVG_ANGLE.type -> {
                request.getSportTrendDumbbellUp(dateType.value)
                request.getSportTrendDumbbellExpandChest(dateType.value)
                mDatabind.upAvgAngleTv.visibility = View.VISIBLE
                mDatabind.upAvgAngleValueTv.visibility = View.VISIBLE
                mDatabind.expandChestAvgAngleTv.visibility = View.VISIBLE
                mDatabind.expandChestAvgAngValueTv.visibility = View.VISIBLE
                mDatabind.upTv.visibility = View.VISIBLE
                mDatabind.expandChestTv.visibility = View.VISIBLE
                mDatabind.upTv.visibility = View.VISIBLE
                mDatabind.expandChestTv.visibility = View.VISIBLE
                mDatabind.dumbbellUpAngleLineChart.visibility = View.VISIBLE
                mDatabind.dumbbellExpandAngleLineChart.visibility = View.VISIBLE
            }
        }
    }

    override fun createObserver() {
        super.createObserver()
        request.sportsCalorieResult.observe(this) {
            parseState(it, { result ->
                mDatabind.sportKilocalorieTv.text = (result.total / 1000).toString()
                initBarChart(result)
            }, {
                appContext.showToast(getString(R.string.request_failed))
            })

        }

        request.heartRateResult.observe(this) {
            parseState(it, { result ->
                mDatabind.avgHeartTv.text = result.avg.toInt().toString()
                mDatabind.maxHeartRateTv.text = result.max.toString()
                initHeartRatelineChart(result)
            }, {
                appContext.showToast(getString(R.string.request_failed))
            })

        }

        request.liftLegTrendResult.observe(this) {
            parseState(it, { result ->
                mDatabind.leftLegAvgAngleValueTv.text = "${result.avg_left_degree.toInt()}°"
                mDatabind.rightLegAvgAngleValueTv.text = "${result.avg_right_degree.toInt()}°"
                initLegAngleLineChart(result)
            }, {
                appContext.showToast(getString(R.string.request_failed))
            })

        }
        request.strongTimeResult.observe(this) {
            parseState(it, { result ->
                initHorizontalBarChart(result)
            }, {
                appContext.showToast(getString(R.string.request_failed))
            })

        }

        request.dumbbellUpResult.observe(this) {
            parseState(it, { result ->
                initDumbbellUpAngleLineChart(mDatabind.dumbbellUpAngleLineChart, result, true)
            }, {
                appContext.showToast(getString(R.string.request_failed))
            })

        }

        request.dumbbellExpandChestResult.observe(this) {
            parseState(it, { result ->
                initDumbbellUpAngleLineChart(mDatabind.dumbbellUpAngleLineChart, result, false)
            }, {
                appContext.showToast(getString(R.string.request_failed))
            })

        }
    }

    private fun showLegAngleView() {
        mDatabind.leftLegAvgAngleTv.visibility = View.VISIBLE
        mDatabind.leftLegAvgAngleValueTv.visibility = View.VISIBLE
        mDatabind.rightLegAvgAngleTv.visibility = View.VISIBLE
        mDatabind.rightLegAvgAngleValueTv.visibility = View.VISIBLE
        mDatabind.rightLegTv.visibility = View.VISIBLE
        mDatabind.leftLegTv.visibility = View.VISIBLE
        mDatabind.angleLineChart.visibility = View.VISIBLE
    }

    private fun showCaloriesView() {
        mDatabind.sportKilocalorieTv.visibility = View.VISIBLE
        mDatabind.sportKilocalorieUnitTv.visibility = View.VISIBLE
        mDatabind.barChart.visibility = View.VISIBLE
    }

    private fun showHeartRatetrendView() {
        mDatabind.avgLabTv.visibility = View.VISIBLE
        mDatabind.maxTv.visibility = View.VISIBLE
        mDatabind.avgHeartTv.visibility = View.VISIBLE
        mDatabind.maxHeartRateTv.visibility = View.VISIBLE
        mDatabind.heartRateLineChart.visibility = View.VISIBLE
    }

    private fun getFormatterData(type: ChartType): Array<String> {
        return if (type == ChartType.BURN_CALORIES || type == ChartType.HEART_RATE_TREND || type == ChartType.LEG_LIFTING_ANGLE) {
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


    private fun initBarChart(result: SportTrendCalorieResult) {
        val barChart = mDatabind.barChart
        barChart.legend.isEnabled = false
        barChart.setTouchEnabled(false)
        barChart.isDragEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setDrawBorders(false)
        barChart.setDrawGridBackground(false)

        barChart.extraBottomOffset = 18f
        barChart.extraRightOffset = 58f
        //xAxis 设置底部样式
        val xAxis = mDatabind.barChart.xAxis
        xAxis.mAxisMinimum = 0f
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.textSize = 20.dp
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.labelCount = Math.min(6,result.trend.size)
//        val dayFormatterData = getFormatterData(ChartType.BURN_CALORIES)
        //设置底部文字显示格式化
        xAxis.valueFormatter =  object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return result.trend[value.toInt()].time
            }

        }
        //设置左边轴样式
        val axisLeft = barChart.axisLeft
        axisLeft.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        axisLeft.textSize = 20.dp
        axisLeft.axisMinimum = 0f
        axisLeft.enableGridDashedLine(2f, 1f, 0f)
        axisLeft.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        //设置右边轴样式
        val axisRight = barChart.axisRight
        axisRight.isEnabled = false
        axisRight.axisMinimum = 0f

        val values = ArrayList<BarEntry>()
        for (i in result.trend.indices) {
            values.add(BarEntry(i.toFloat(), result.trend[i].calorie/1000f))
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
        barChart.animateY(0)
        val description = Description()
        description.text = ""
        barChart.description = description
    }

    private fun initHorizontalBarChart(result: SportTrendLiftLegSportTimeResult) {
        val horizontalBarChart = mDatabind.horizontalBarChart
        horizontalBarChart.legend.isEnabled = false
        horizontalBarChart.setTouchEnabled(false)
        horizontalBarChart.isDragEnabled = false
        horizontalBarChart.setScaleEnabled(false)
        horizontalBarChart.setDrawBorders(false)
        horizontalBarChart.setDrawGridBackground(false)
        horizontalBarChart.isHighlightFullBarEnabled = false
        horizontalBarChart.extraBottomOffset = 18f
        horizontalBarChart.extraRightOffset = 18f
        val xAxis = horizontalBarChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_cc1c1c1c)
        xAxis.textSize = 20.dp
        xAxis.labelCount = 4
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        val dayFormatterData = getFormatterData(ChartType.INTENSITY_AND_TIME)
        xAxis.valueFormatter = IndexAxisValueFormatter(dayFormatterData)
        val axisLeft = horizontalBarChart.axisLeft
        axisLeft.enableGridDashedLine(2f, 1f, 0f)
        axisLeft.isEnabled = false
        axisLeft.axisMinimum = 0f
        val axisRight = horizontalBarChart.axisRight
//        axisRight.isEnabled = false
        axisRight.setDrawGridLines(false)
        axisRight.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        axisRight.axisMinimum = 0f
        axisRight.textSize = 20.dp

        axisRight.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return DateTimeUtil.formatTime(value.toLong())
            }
        }

        val values = ArrayList<BarEntry>()
        values.add(BarEntry(0f, result.extreme_breakthrough.toFloat()))
        values.add(BarEntry(1f, result.heart_lung_enhancement.toFloat()))
        values.add(BarEntry(2f, result.efficient_grease_burning.toFloat()))
        values.add(BarEntry(3f, result.warm_up_activation.toFloat()))

        val dataSets = ArrayList<IBarDataSet>()
        val barDataSet = BarDataSet(values, "消耗(千卡)")
        dataSets.add(barDataSet)
        barDataSet.setDrawIcons(false)
        barDataSet.colors = arrayListOf(ContextCompat.getColor(appContext, R.color.color_ff574c), ContextCompat.getColor(appContext, R.color.color_ff824c), ContextCompat.getColor(appContext, R.color.color_ffc019), ContextCompat.getColor(appContext, R.color.color_blue))
        barDataSet.setDrawValues(true)//不显示柱状图上数据
        barDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return DateTimeUtil.formatTime(value.toLong())
            }
        }
        val barData = BarData(dataSets)
        barData.barWidth = 0.3f
        horizontalBarChart.data = barData
        val description = Description()
        description.text = ""//去掉描述，必须设置为空字符串，要不然还会显示
        horizontalBarChart.description = description
        horizontalBarChart.setNoDataText("暂无数据")
        horizontalBarChart.animateY(0)

    }

    private fun initDumbbellUpAngleLineChart(lineChart: LineChart, result: SportTrendDumbbellExpandChestAndUpResult, isUp: Boolean) {
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawBorders(false)
        lineChart.setDrawGridBackground(false)
        lineChart.extraBottomOffset = 18f
        lineChart.extraRightOffset = 18f
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 20.dp
        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.gridLineWidth = 0.5f
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.axisMinimum = 0f
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        leftAxis.labelCount=5
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.textSize = 20.dp
        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        val values = ArrayList<Entry>()

        for (i in result.trend.indices) {
            values.add(BarEntry(i.toFloat(), result.trend[i].up_degree.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius=4f
        if (isUp) {
            lineDataSet.color = appContext.getColor(R.color.color_ffc019)
        } else {
            lineDataSet.color = appContext.getColor(R.color.color_blue)
        }
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

    private fun initHeartRatelineChart(result: HeartRateResult) {
        val lineChart = mDatabind.heartRateLineChart
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawBorders(false)
        lineChart.setDrawGridBackground(false)

        lineChart.extraBottomOffset = 18f
        lineChart.extraRightOffset = 54f
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 20.dp
        xAxis.labelCount =if(result.trend.size<8) result.trend.size else 6
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value.toInt() >= 0 && value.toInt() < result.trend.size) {
                    result.trend[value.toInt()].time
                } else {
                    ""
                }
            }

        }
        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.gridLineWidth = 0.5f
        leftAxis.textSize = 20.dp
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.axisMinimum = 0f
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        leftAxis.labelCount=5
        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        val values = ArrayList<Entry>()

        for (i in result.trend.indices) {
            values.add(BarEntry(i.toFloat(), result.trend[i].rate_value.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius=4f
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

    private fun initLegAngleLineChart(result: LiftLegTrendResult) {
        val lineChart = mDatabind.angleLineChart
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawBorders(false)
        lineChart.setDrawGridBackground(false)

        lineChart.extraBottomOffset = 18f
        lineChart.extraRightOffset = 18f
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.textSize = 20.dp
        xAxis.labelCount =if(result.trend.size<8) result.trend.size else 8
        xAxis.enableGridDashedLine(2f, 1f, 0f)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toInt()+1}组"
            }

        }

        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.axisMinimum = 0f
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.setDrawLabels(true)
        leftAxis.setDrawAxisLine(false)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        leftAxis.textSize = 20.dp
        leftAxis.labelCount=5
        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        val values = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()

        val leftLegRecord = result.trend.filter { it.left_degree != 0.0 }
        for (i in leftLegRecord.indices) {
            values.add(BarEntry(i.toFloat(), leftLegRecord[i].left_degree.toFloat()))
        }
        val rightLegRecord = result.trend.filter { it.right_degree != 0.0 }
        for (i in rightLegRecord.indices) {
            values2.add(BarEntry(i.toFloat(), rightLegRecord[i].right_degree.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius=4f
        lineDataSet.setDrawValues(false)
        lineDataSet.color = appContext.getColor(R.color.color_blue)
        // draw selection line as dashed
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area
        lineDataSet.setDrawFilled(true)
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
        lineDataSet2.setDrawCircles(true)
        lineDataSet.setDrawCircles(true)
        lineDataSet.setCircleColors(resources.getColor(R.color.color_blue))
        lineDataSet.circleRadius=4f
        lineDataSet2.color = appContext.getColor(R.color.color_ffc019)
        lineDataSet2.setDrawCircleHole(false)
        lineDataSet2.setDrawValues(false)
        lineDataSet2.setCircleColors(resources.getColor(R.color.color_ffc019))
        lineDataSet2.circleRadius=4f
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

        // set color of filled area

        dataSets.add(lineDataSet)
        dataSets.add(lineDataSet2)
        val barData = LineData(dataSets)
        lineChart.data = barData
        lineChart.setNoDataText("暂无数据")
        lineChart.animateY(0)
    }


    inner class ProxyClick {

    }
}