package com.fjp.skeletalmuscle.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.dp
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.ext.loadImageWithCallback
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.weight.CircleImageView
import com.fjp.skeletalmuscle.app.weight.recyclerview.SpaceItemDecoration
import com.fjp.skeletalmuscle.data.model.bean.HeartRate
import com.fjp.skeletalmuscle.data.model.bean.SportsRecord
import com.fjp.skeletalmuscle.data.model.bean.result.ExpandChestDegree
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.data.model.bean.result.UpDegree
import com.fjp.skeletalmuscle.databinding.ActivitySportsRecordBinding
import com.fjp.skeletalmuscle.ui.main.adapter.SportsRecordLegAdapter
import com.fjp.skeletalmuscle.viewmodel.request.RequestMainViewModel
import com.fjp.skeletalmuscle.viewmodel.state.ShareViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SportsRecordViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.toJson
import java.util.Calendar

class SportsRecordActivity : BaseActivity<SportsRecordViewModel, ActivitySportsRecordBinding>() {
    val shareViewModel: ShareViewModel by viewModels()
    private val requestMainViewModel: RequestMainViewModel by viewModels()
    val calendar = Calendar.getInstance()
    override fun initView(savedInstanceState: Bundle?) {
        val year = intent.getIntExtra(Constants.INTENT_KEY_YEAR, 0)
        val month = intent.getIntExtra(Constants.INTENT_KEY_MONTH, 0)
        val day = intent.getIntExtra(Constants.INTENT_KEY_DAY, 0)
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        calendar.set(year, month - 1, day)

        mViewModel.title.set("看看${year}年${month}月${day}日运动记录吧")

        setCalendarTitle()

    }


    override fun createObserver() {
        super.createObserver()
        requestMainViewModel.mainLiveData.observe(this) {
            parseState(it, { todayData ->
                initData(todayData)
            }, {
                it.printStackTrace()
                showToast(getString(R.string.request_failed))
            })
        }
    }

    private fun initData(todayData: TodayDataResult) {
        if (todayData.score > 80) {
            mDatabind.circleProgressView.setTextColor(resources.getColor(R.color.color_blue))
            mDatabind.circleProgressView.setCircleColor(resources.getColor(R.color.color_blue))
        } else if (todayData.score > 60) {
            mDatabind.circleProgressView.setTextColor(resources.getColor(R.color.color_ffc019))
            mDatabind.circleProgressView.setCircleColor(resources.getColor(R.color.color_ffc019))
        } else if (todayData.score > 30) {
            mDatabind.circleProgressView.setTextColor(resources.getColor(R.color.color_ff824c))
            mDatabind.circleProgressView.setCircleColor(resources.getColor(R.color.color_ff824c))
        } else {
            mDatabind.circleProgressView.setTextColor(resources.getColor(R.color.color_ff574c))
            mDatabind.circleProgressView.setCircleColor(resources.getColor(R.color.color_ff574c))
        }
        mDatabind.circleProgressView.setScore(todayData.score)
        mDatabind.allSportsSRIL.setValue(DateTimeUtil.sceond2Min(todayData.sport_time.toLong()))
        mDatabind.totalConsumptionSRIL.setValue((todayData.calorie_total / 1000).toString())
        mDatabind.avgHeartRateSRIL.setValue(todayData.avg_rate_value.toString())


        if (todayData.sport_lift_leg != null) {
            mDatabind.highKneeSRIL.setValue(DateTimeUtil.sceond2Min(todayData.sport_lift_leg.sport_time))
        } else {
            mDatabind.highKneeSRIL.setValue("0")
        }
        if (todayData.sport_dumbbell != null) {
            mDatabind.dumbbellSRIL.setValue(DateTimeUtil.sceond2Min(todayData.sport_dumbbell.sport_time))
        } else {
            mDatabind.dumbbellSRIL.setValue("0")
        }

        if (todayData.sport_flat_support != null) {
            mDatabind.plankSRIL.setValue(DateTimeUtil.sceond2Min(todayData.sport_flat_support.sport_time))
        } else {
            mDatabind.plankSRIL.setValue("0")
        }

        if (todayData.sport_lift_leg != null && todayData.sport_lift_leg.sport_time != 0L) {
            mDatabind.heightLegBg.visibility = View.VISIBLE
            val sportLiftLeg = todayData.sport_lift_leg
            val time = sportLiftLeg.sport_time
            mDatabind.highKneeSRIL.setValue(DateTimeUtil.sceond2Min(time))
            mDatabind.highKneeScoreTv.text = sportLiftLeg.score.toString()
            mDatabind.highKneeSportsTimeValueTv.text = DateTimeUtil.sceond2Min(time)
            mDatabind.highKneeBurnCaloriesValueTv.text = (sportLiftLeg.sum_calorie / 1000).toString()
            mDatabind.highKneeExerciseAmountTotalValueTv.text = (sportLiftLeg.left_sport_amount + sportLiftLeg.right_sport_amount).toString()
            mDatabind.highKneeExerciseAmountRightValueTv.text = (sportLiftLeg.right_times).toString()
            mDatabind.highKneeExerciseAmountLeftValueTv.text = (sportLiftLeg.left_times).toString()
            mDatabind.highKneeAngleRightValueTv.text = "${sportLiftLeg.avg_right_degree}°"
            mDatabind.highKneeAngleLeftValueTv.text = "${sportLiftLeg.avg_left_degree}°"
            mDatabind.highKneeEnduranceValueTv.text = (sportLiftLeg.cardiorespiratory_endurance).toString()

            if (time != 0.toLong()) {
                mDatabind.rpb1.setProgressPercentage((sportLiftLeg.warm_up_activation / (time * 1.0) * 100), true)
                mDatabind.rpb2.setProgressPercentage((sportLiftLeg.efficient_grease_burning / (time * 1.0) * 100), true)
                mDatabind.rpb3.setProgressPercentage((sportLiftLeg.heart_lung_enhancement / (time * 1.0) * 100), true)
                mDatabind.rpb4.setProgressPercentage((sportLiftLeg.extreme_breakthrough / (time * 1.0) * 100), true)
            }

            mDatabind.rpb1Time.text = DateTimeUtil.formatTime(sportLiftLeg.warm_up_activation.toLong())
            mDatabind.rpb2Time.text = DateTimeUtil.formatTime(sportLiftLeg.efficient_grease_burning.toLong())
            mDatabind.rpb3Time.text = DateTimeUtil.formatTime(sportLiftLeg.heart_lung_enhancement.toLong())
            mDatabind.rpb4Time.text = DateTimeUtil.formatTime(sportLiftLeg.extreme_breakthrough.toLong())
            mDatabind.maxValueTv.text = sportLiftLeg.max_rate_value.toString()
            mDatabind.avgValueTv.text = sportLiftLeg.avg_rate_value.toString()
            initLineChart(mDatabind.lineChart, sportLiftLeg.heart_rate, 3)
        } else {
            mDatabind.heightLegBg.visibility = View.GONE
        }

        if (todayData.sport_dumbbell != null && todayData.sport_dumbbell.sport_time != 0L) {
            mDatabind.dumbbeBg.visibility = View.VISIBLE
            mDatabind.dumbbellScoreTv.text = todayData.sport_dumbbell.score.toString()
            mDatabind.dumbbellAvgHeartRateValueTv.text = todayData.sport_dumbbell.avg_rate_value.toString()
            mDatabind.expandChestValueTv.text = "${todayData.sport_dumbbell.avg_expand_chest_degree.toInt().toString()}°"
            initSportsDumbbell(todayData)
            initDumbbellExpandChestLineChart(todayData.sport_dumbbell.expand_chest_degree)
        } else {
            mDatabind.dumbbeBg.visibility = View.GONE
        }
        if (todayData.sport_flat_support != null && todayData.sport_flat_support.sport_time != 0L) {
            mDatabind.flatSupportBg.visibility = View.VISIBLE
            mDatabind.plankScoreTv.text = todayData.sport_flat_support.score.toString()
            mDatabind.plankAvgHeartRateValueTv.text = todayData.sport_flat_support.avg_rate_value.toString()
            mDatabind.plankBurnCaloriesValueTv.text = (todayData.sport_flat_support.sum_calorie / 1000).toString()
            mDatabind.plankSportsTimeValueTv.text = DateTimeUtil.sceond2Min(todayData.sport_flat_support.sport_time)
            initSportsPlank(todayData)
        } else {
            mDatabind.flatSupportBg.visibility = View.GONE
        }
    }

    private fun initDumbbellExpandChestLineChart(expandChestDegree: List<ExpandChestDegree>) {
        val lineChart = mDatabind.dumbbellExpandChestLineChart
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
        xAxis.isEnabled = false

        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.gridLineWidth = 0.5f
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        leftAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        leftAxis.textSize = 16.dp
        leftAxis.axisMinimum = 0f
        leftAxis.labelCount = 4
        leftAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                if(index<expandChestDegree.size){
                    return "${index}°"
                }else{
                    return ""
                }
            }
        }
        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.isEnabled = false
        val values = ArrayList<Entry>()

        for (i in expandChestDegree.indices) {
            values.add(BarEntry(i.toFloat(), expandChestDegree[i].expand_chest_degree.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "")
        lineDataSet.setDrawIcons(false)
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setDrawCircles(true)
        lineDataSet.setCircleColor(ContextCompat.getColor(appContext, R.color.color_4e71ff))
        lineDataSet.circleRadius = 4f
        lineDataSet.color = ContextCompat.getColor(appContext, R.color.color_4e71ff)
        lineDataSet.setDrawCircleHole(false)

        lineDataSet.setDrawValues(false)


        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)

        lineDataSet.setDrawFilled(true)

        if (Utils.getSDKInt() >= 18) {
            val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_blue)
            lineDataSet.fillDrawable = drawable
        } else {
            lineDataSet.fillColor = Color.BLACK
        }
        dataSets.add(lineDataSet)
        val barData = LineData(dataSets)
        lineChart.data = barData
        lineChart.setNoDataText("暂无数据")
    }

    private fun initSportsDumbbell(todayData: TodayDataResult) {
        var dummbbell = todayData.sport_dumbbell!!
        mViewModel.dumbbellSportsDate.add(SportsRecord("运动时长", DateTimeUtil.sceond2Min(dummbbell.sport_time), "分钟"))
        mViewModel.dumbbellSportsDate.add(SportsRecord("哑铃重量", dummbbell.weight.toString(), "kg"))
        mViewModel.dumbbellSportsDate.add(SportsRecord("上举次数", dummbbell.left_up_times.toString(), "次",dummbbell.right_up_times.toString(), "次"))
        mViewModel.dumbbellSportsDate.add(SportsRecord("扩胸次数", dummbbell.expand_chest_times.toString(), "次"))
        mViewModel.dumbbellSportsDate.add(SportsRecord("消耗卡路里", (dummbbell.sum_calorie / 1000).toString(), "千卡"))
        mDatabind.dumbbellRecyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), SportsRecordLegAdapter(mViewModel.dumbbellSportsDate))
        mDatabind.dumbbellRecyclerView.addItemDecoration(SpaceItemDecoration(16.dp.toInt(), 0))
        initLineChart(mDatabind.dumbbellHeartRateLineChart, dummbbell.heart_rate, 3)
//        dumbbellAngleLineChart
    }

    private fun initSportsPlank(todayData: TodayDataResult) {
        var flatSupport = todayData.sport_flat_support!!
        initLineChart(mDatabind.plankHeartRateLineChart, flatSupport.heart_rate, 5)
    }


    fun setCalendarTitle() {
        mViewModel.title.set("看看${calendar.year}年${calendar.month + 1}月${calendar.dayOfMonth}日运动记录吧")
        mViewModel.calendarTitle.set("${calendar.year}年${calendar.month + 1}月${calendar.dayOfMonth}日")
        mViewModel.legSportsTime.set("${calendar.month + 1}月${calendar.dayOfMonth}日")
        mViewModel.dumbbellSportsTime.set("${calendar.month + 1}月${calendar.dayOfMonth}日")
        mViewModel.plankSportsTime.set("${calendar.month + 1}月${calendar.dayOfMonth}日")
        requestMainViewModel.getTodayData("${calendar.year}-${calendar.month + 1}-${calendar.dayOfMonth}")
    }

    private fun initLineChart(lineChart: LineChart, heartRate: List<HeartRate>, labelCount: Int) {
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
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
        xAxis.labelCount = labelCount
        xAxis.setDrawAxisLine(true)
        xAxis.enableGridDashedLine(2f, 1f, 0f)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < heartRate.size) {
                    DateTimeUtil.formatDate(heartRate[value.toInt()].record_time.toLong() * 1000, DateTimeUtil.HH_MM_SS)
                } else {
                    ""
                }

            }

        }

        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.enableAxisLineDashedLine(2f, 1f, 0f)
        leftAxis.gridLineWidth = 0f
        leftAxis.setDrawLabels(true)
        leftAxis.textSize = 20.dp
        leftAxis.axisMinimum = 0f
        leftAxis.labelCount = 3
        leftAxis.setDrawAxisLine(false)
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)
        leftAxis.textColor = ContextCompat.getColor(appContext, R.color.color_801c1c1c)

        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        val values = ArrayList<Entry>()

        for (i in heartRate.indices) {
            values.add(BarEntry(i.toFloat(), heartRate[i].rate_value.toFloat()))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "0")
        lineDataSet.setDrawIcons(false)
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawCircles(true)
        lineDataSet.setCircleColor(ContextCompat.getColor(appContext, R.color.color_ff574c))
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
//        lineChart.animateY(500)
    }

    inner class ProxyClick {
        fun clickPreDay() {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            setCalendarTitle()
        }

        fun clickNextDay() {
//            if(calendar.after(Calendar.getInstance())){
//                showToast("大于今天是没有运动数据的")
//                return
//            }
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            setCalendarTitle()
        }

        fun clickfinish() {
            this@SportsRecordActivity.finish()
        }

        fun clickShare() {
            val shareTitleView = View.inflate(this@SportsRecordActivity, R.layout.share_title, null)
            val shareTBottomView = View.inflate(this@SportsRecordActivity, R.layout.share_bottom, null)
            val avatarIv = shareTitleView.findViewById<CircleImageView>(R.id.avatarIv)
            val nameTv = shareTitleView.findViewById<TextView>(R.id.nameTv)
            val timeTv = shareTitleView.findViewById<TextView>(R.id.timeTv)
            nameTv.text = App.userInfo.name
            timeTv.text = DateTimeUtil.formatShareTime(System.currentTimeMillis())

            loadImageWithCallback(avatarIv,App.userInfo.profile) {
                shareViewModel.share(this@SportsRecordActivity, shareTitleView, mDatabind.scrollView.getChildAt(0), shareTBottomView)
            }
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
}