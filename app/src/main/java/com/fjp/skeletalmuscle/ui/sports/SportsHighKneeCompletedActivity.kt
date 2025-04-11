package com.fjp.skeletalmuscle.ui.sports

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.loadImageWithCallback
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.weight.CircleImageView
import com.fjp.skeletalmuscle.app.weight.pop.SharePop
import com.fjp.skeletalmuscle.data.model.bean.result.SaveLiftLegResult
import com.fjp.skeletalmuscle.databinding.ActivitySportsHightKneeCompletedBinding
import com.fjp.skeletalmuscle.viewmodel.state.ShareViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SportsHighKneeCompletedViewModel
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

class SportsHighKneeCompletedActivity : BaseActivity<SportsHighKneeCompletedViewModel, ActivitySportsHightKneeCompletedBinding>() {
    lateinit var sportLiftLeg: SaveLiftLegResult
    val shareViewModel: ShareViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.title.set(getString(R.string.sports_completed_title))
        sportLiftLeg = intent.getParcelableExtra(Constants.INTENT_SPORT_LIFT_LEG)!!
        mViewModel.curScore.set(sportLiftLeg.score.toString())
        mViewModel.sportsTime.set(DateTimeUtil.formSportTime(sportLiftLeg.sport_time))
        mViewModel.endurance.set(sportLiftLeg.cardiorespiratory_endurance.toString())
        mViewModel.heat.set((sportLiftLeg.calorie / 1000).toString())
        mDatabind.avgLeftTv.text = "${Math.round(sportLiftLeg.avg_left_degree)}°"
        mDatabind.avgRightTv.text = "${Math.round(sportLiftLeg.avg_right_degree)}°"
        mDatabind.warmupTimePB.setProgressPercentage(((sportLiftLeg.warm_up_activation / (sportLiftLeg.sport_time).toDouble()) * 100), true)
        mDatabind.fatBurningTimePb.setProgressPercentage(((sportLiftLeg.efficient_grease_burning / (sportLiftLeg.sport_time).toDouble()) * 100), true)
        mDatabind.cardioTimePb.setProgressPercentage(((sportLiftLeg.heart_lung_enhancement / (sportLiftLeg.sport_time).toDouble()) * 100), true)
        mDatabind.breakTimePB.setProgressPercentage(((sportLiftLeg.extreme_breakthrough / (sportLiftLeg.sport_time).toDouble()) * 100), true)
        mDatabind.warmupTimeMinTv.text = DateTimeUtil.formSportTime(sportLiftLeg.warm_up_activation.toLong()) + "'"
        mDatabind.fatBurningTimeMinTv.text = DateTimeUtil.formSportTime(sportLiftLeg.efficient_grease_burning.toLong()) + "'"
        mDatabind.cardioTimeTotalMinTv.text = DateTimeUtil.formSportTime(sportLiftLeg.heart_lung_enhancement.toLong()) + "'"
        mDatabind.breakTimeTotalMinTv.text = DateTimeUtil.formSportTime(sportLiftLeg.extreme_breakthrough.toLong()) + "'"
        mViewModel.totalCount.set((sportLiftLeg.left_sport_amount + sportLiftLeg.right_sport_amount).toString())
        mViewModel.leftCount.set((sportLiftLeg.left_times).toString())
        mViewModel.rightCount.set(sportLiftLeg.right_times.toString())
        mViewModel.avgHeart.set(sportLiftLeg.avg_rate_value.toString())
        mViewModel.maxHeart.set(sportLiftLeg.max_rate_value.toString())
        initCalorieBarChart()
        initHeartRateLineChart()
        initLegAngleLineChart()

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
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 20f
        xAxis.labelCount = 2
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < sportLiftLeg.heart_rate.size) {
                    DateTimeUtil.completedTimeFromat(sportLiftLeg.heart_rate[value.toInt()].record_time, DateTimeUtil.DATE_PATTERN_SS)
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

        for (i in sportLiftLeg.heart_rate.indices) {
            values.add(BarEntry(i.toFloat(), sportLiftLeg.heart_rate[i].rate_value.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(true)
        lineDataSet.setCircleColor(resources.getColor(R.color.color_ff574c))
        lineDataSet.circleRadius = 4f
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
        val description = Description()
        description.text = ""
        barChart.description = description
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 20f
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.labelCount = Math.min(2, sportLiftLeg.calorie_list.size)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < sportLiftLeg.calorie_list.size) {
                    DateTimeUtil.completedTimeFromat(sportLiftLeg.calorie_list[index].record_time, DateTimeUtil.DATE_PATTERN_SS)
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
        for (i in sportLiftLeg.calorie_list) {
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
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.enableGridDashedLine(2f, 1f, 0f)
        xAxis.textSize = 20f
        xAxis.labelCount = 2
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toInt() + 1}组"
            }

        }
        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(false)
        leftAxis.axisMinimum = 0f
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)

        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        val values = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()

        val leftLegRecord = sportLiftLeg.record.filter { it.type == 1 }
        for (i in leftLegRecord.indices) {
            values.add(BarEntry(i.toFloat(), leftLegRecord[i].degree.toFloat()))
        }
        val rightLegRecord = sportLiftLeg.record.filter { it.type == 2 }
        for (i in rightLegRecord.indices) {
            values2.add(BarEntry(i.toFloat(), rightLegRecord[i].degree.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(false)
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
        lineDataSet2.setDrawCircles(false)
        lineDataSet2.color = appContext.getColor(R.color.color_ffc019)
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

        dataSets.add(lineDataSet)
        dataSets.add(lineDataSet2)
        val barData = LineData(dataSets)
        lineChart.data = barData
        lineChart.setNoDataText("暂无数据")
        lineChart.animateY(0)
    }


    inner class ProxyClick {
        fun clickShare() {
//            sharePop()
            val shareTitleView = View.inflate(this@SportsHighKneeCompletedActivity, R.layout.share_title, null)
            val shareTBottomView = View.inflate(this@SportsHighKneeCompletedActivity, R.layout.share_bottom, null)
            val avatarIv = shareTitleView.findViewById<CircleImageView>(R.id.avatarIv)
            val nameTv = shareTitleView.findViewById<TextView>(R.id.nameTv)
            val timeTv = shareTitleView.findViewById<TextView>(R.id.timeTv)
            nameTv.text = App.userInfo.name
            timeTv.text = DateTimeUtil.formatShareTime(System.currentTimeMillis())

            loadImageWithCallback(avatarIv, App.userInfo.profile) {
                shareViewModel.share(this@SportsHighKneeCompletedActivity, shareTitleView, mDatabind.shareCl, shareTBottomView)
            }


        }

        fun clickCompleted() {
            finish()

        }

        fun clickFinish() {
            finish()

        }
    }

    override fun onResume() {
        super.onResume()
//        mDatabind.blurLayout.startBlur()
    }

    override fun onPause() {
        super.onPause()
//        mDatabind.blurLayout.pauseBlur()
    }

    fun sharePop() {
        val sharePop = SharePop(this@SportsHighKneeCompletedActivity, object : SharePop.Listener {
            override fun share(pop: SharePop) {
                pop.dismiss()
            }

        })
        val pop = XPopup.Builder(this@SportsHighKneeCompletedActivity).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(sharePop)

        pop.show()
    }
}