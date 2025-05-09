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
import com.fjp.skeletalmuscle.app.ext.loadImageWithCallback
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.weight.CircleImageView
import com.fjp.skeletalmuscle.app.weight.pop.SharePop
import com.fjp.skeletalmuscle.data.model.bean.result.SavePlankResult
import com.fjp.skeletalmuscle.databinding.ActivitySportsPlankCompletedBinding
import com.fjp.skeletalmuscle.viewmodel.state.ShareViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SportsPlankCompletedViewModel
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
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.base.appContext

class SportsPlankCompletedActivity : BaseActivity<SportsPlankCompletedViewModel, ActivitySportsPlankCompletedBinding>() {
    lateinit var sportFlatSupport: SavePlankResult
    val shareViewmodel: ShareViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
        sportFlatSupport = intent.getParcelableExtra(Constants.INTENT_SPORT_PLANK)!!
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.sports_completed_title))
        mViewModel.curScore.set(sportFlatSupport.score.toString())
        mViewModel.sportsTime.set(DateTimeUtil.formSportTime(sportFlatSupport.sport_time))
        mViewModel.heat.set(sportFlatSupport.avg_rate_value.toString())
        mViewModel.calorie.set((sportFlatSupport.calorie / 1000).toString())
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
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 20f
        xAxis.labelCount = 3
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < sportFlatSupport.heart_rate.size) {
                    DateTimeUtil.completedTimeFromat(sportFlatSupport.heart_rate[value.toInt()].record_time, DateTimeUtil.DATE_PATTERN_SS)
                } else {
                    ""
                }
            }
        }
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

        lineDataSet.setDrawValues(false)
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)

        lineDataSet.setDrawFilled(true)

        if (Utils.getSDKInt() >= 18) {
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
        val description = Description()
        description.text = ""
        barChart.description = description
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 20f
        xAxis.labelCount = 2
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < sportFlatSupport.calorie_list.size) {
                    DateTimeUtil.completedTimeFromat(sportFlatSupport.calorie_list[value.toInt()].record_time, DateTimeUtil.DATE_PATTERN_SS)
                } else {
                    ""
                }
            }

        }
        xAxis.enableGridDashedLine(2f, 1f, 0f)

        val leftAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.gridLineWidth = 0f
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(false)
        leftAxis.axisMinimum = 0f
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)

        val rightAxis: YAxis = barChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        barChart.legend.isEnabled = false
        val values = ArrayList<BarEntry>()

        for (i in sportFlatSupport.calorie_list.indices) {
            values.add(BarEntry(i.toFloat(), sportFlatSupport.calorie_list[i].calorie.toFloat()))

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
        fun clickShare() {
//            sharePop()
            val shareTitleView = View.inflate(this@SportsPlankCompletedActivity, R.layout.share_title, null)
            val shareTBottomView = View.inflate(this@SportsPlankCompletedActivity, R.layout.share_bottom, null)
            val avatarIv = shareTitleView.findViewById<CircleImageView>(R.id.avatarIv)
            val nameTv = shareTitleView.findViewById<TextView>(R.id.nameTv)
            val timeTv = shareTitleView.findViewById<TextView>(R.id.timeTv)
            nameTv.text = App.userInfo.name
            timeTv.text = DateTimeUtil.formatShareTime(System.currentTimeMillis())
            loadImageWithCallback(avatarIv,App.userInfo.profile) {
                shareViewmodel.share(this@SportsPlankCompletedActivity, shareTitleView, mDatabind.shareCl, shareTBottomView)
            }

        }

        fun clickCompleted() {
            finish()

        }

        fun clickFinish() {
            finish()

        }
    }

    fun sharePop() {
        val sharePop = SharePop(this@SportsPlankCompletedActivity, object : SharePop.Listener {
            override fun share(pop: SharePop) {
                pop.dismiss()
            }

        })
        val pop = XPopup.Builder(this@SportsPlankCompletedActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(sharePop)

        pop.show()
    }
}