package com.fjp.skeletalmuscle.ui.sports

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import coil.load
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.weight.CircleImageView
import com.fjp.skeletalmuscle.app.weight.pop.SharePop
import com.fjp.skeletalmuscle.data.model.bean.result.SaveDumbbellResult
import com.fjp.skeletalmuscle.databinding.ActivitySportsDumbbellCompletedBinding
import com.fjp.skeletalmuscle.viewmodel.state.ShareViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SportsDumbbellCompletedViewModel
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
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.base.appContext

class SportsDumbbellCompletedActivity : BaseActivity<SportsDumbbellCompletedViewModel, ActivitySportsDumbbellCompletedBinding>() {
    lateinit var dumbbellResult: SaveDumbbellResult
    val shareViewmodel: ShareViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
        dumbbellResult = intent.getParcelableExtra(Constants.INTENT_COMPLETED)!!
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.sports_completed_title))
        mViewModel.sportsTime.set(DateTimeUtil.formSportTime(dumbbellResult.sport_time))
        mViewModel.curScore.set(dumbbellResult.score.toString())
        mViewModel.heartRate.set(dumbbellResult.avg_rate_value.toString())
        mViewModel.weight.set(dumbbellResult.weight.toString())
        mViewModel.calorie.set((dumbbellResult.calorie / 1000).toString())
        mViewModel.upTimes.set(dumbbellResult.up_times.toString())
        mViewModel.expandChestTimes.set(dumbbellResult.expand_chest_times.toString())
        mViewModel.expandChestDegree.set(String.format("%.0f", dumbbellResult.avg_expand_chest_degree) + "°")
//
        initCalorieBarChart()
        initHeartRateLineChart()
        initLegAngleLineChart()
    }

    private fun initCalorieBarChart() {
        val barChart = mDatabind.calorieBarChat
        barChart.setTouchEnabled(false)
        barChart.isDragEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setDrawBorders(false)
        barChart.setDrawGridBackground(false)
        barChart.extraBottomOffset = 5f
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
        xAxis.labelCount = Math.min(2, dumbbellResult.calorie_list.size)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < dumbbellResult.calorie_list.size) {
                    DateTimeUtil.completedTimeFromat(dumbbellResult.calorie_list[value.toInt()].record_time, DateTimeUtil.DATE_PATTERN_SS)
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

        for (i in dumbbellResult.heart_rate.indices) {
            values.add(BarEntry(i.toFloat(), dumbbellResult.heart_rate[i].rate_value.toFloat()))

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

    private fun initHeartRateLineChart() {
        val lineChart = mDatabind.heartRateLineChart
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawBorders(false)
        lineChart.setDrawGridBackground(false)
        lineChart.extraBottomOffset = 5f
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 20f
        xAxis.labelCount = 2
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < dumbbellResult.heart_rate.size) {
                    DateTimeUtil.completedTimeFromat(dumbbellResult.heart_rate[value.toInt()].record_time, DateTimeUtil.DATE_PATTERN_SS)
                } else {
                    ""
                }
            }

        }
        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.axisMinimum = 0f
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

        for (i in dumbbellResult.heart_rate.indices) {
            values.add(BarEntry(i.toFloat(), dumbbellResult.heart_rate[i].rate_value.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius = 4f
        lineDataSet.setCircleColor(resources.getColor(R.color.color_ff574c))
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

    private fun initLegAngleLineChart() {
        val lineChart = mDatabind.legAngleLineChart
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawBorders(false)
        lineChart.setDrawGridBackground(false)
        lineChart.extraBottomOffset = 5f
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.enableGridDashedLine(2f, 1f, 0f)

        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.setDrawLabels(false)
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawAxisLine(false)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)

        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        val values2 = ArrayList<Entry>()
        val expandAngle = dumbbellResult.record.filter { it.type == 2 }
        for (i in expandAngle.indices) {
            values2.add(BarEntry(i.toFloat(), expandAngle[i].degree.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet2 = LineDataSet(values2, "千卡")
        lineDataSet2.setDrawIcons(false)
        lineDataSet2.mode = LineDataSet.Mode.LINEAR
        lineDataSet2.setDrawCircles(true)
        lineDataSet2.color = appContext.getColor(R.color.color_blue)
        lineDataSet2.setCircleColor(appContext.getColor(R.color.color_blue))
        lineDataSet2.setDrawCircleHole(false)
        lineDataSet2.setDrawValues(false)
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

        dataSets.add(lineDataSet2)
        val barData = LineData(dataSets)
        lineChart.data = barData
        lineChart.setNoDataText("暂无数据")
        lineChart.animateY(0)
    }

    inner class ProxyClick {
        fun clickShare() {
//            sharePop()
            val shareTitleView = View.inflate(this@SportsDumbbellCompletedActivity, R.layout.share_title, null)
            val shareTBottomView = View.inflate(this@SportsDumbbellCompletedActivity, R.layout.share_bottom, null)
            val avatarIv = shareTitleView.findViewById<CircleImageView>(R.id.avatarIv)
            val nameTv = shareTitleView.findViewById<TextView>(R.id.nameTv)
            val timeTv = shareTitleView.findViewById<TextView>(R.id.timeTv)
            nameTv.text = App.userInfo.name
            timeTv.text = DateTimeUtil.formatShareTime(System.currentTimeMillis())
            avatarIv.load(App.userInfo.profile)
            avatarIv.load(App.userInfo.profile, builder = {
                allowHardware(false)
                this.error(R.drawable.avatar_default)
                this.placeholder(R.drawable.avatar_default)
            })
            shareViewmodel.share(this@SportsDumbbellCompletedActivity, shareTitleView, mDatabind.shareCl, shareTBottomView)
        }

        fun clickCompleted() {
            finish()

        }

        fun clickFinish() {
            finish()

        }
    }

    fun sharePop() {
        val sharePop = SharePop(this@SportsDumbbellCompletedActivity, object : SharePop.Listener {
            override fun share(pop: SharePop) {
                pop.dismiss()
            }

        })
        val pop = XPopup.Builder(this@SportsDumbbellCompletedActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(sharePop)

        pop.show()
    }
}