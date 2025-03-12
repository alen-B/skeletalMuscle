package com.fjp.skeletalmuscle.ui.assessment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.AssessmentUtils
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.data.model.bean.result.AssessmentHistoryData
import com.fjp.skeletalmuscle.databinding.ActivitySportsAssessmentResultBinding
import com.fjp.skeletalmuscle.viewmodel.state.SportsAssessmentResultViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import me.hgj.jetpackmvvm.ext.parseState


class SportsAssessmentResultActivity : BaseActivity<SportsAssessmentResultViewModel, ActivitySportsAssessmentResultBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.getLastTest()
        if (App.userInfo?.sex == getString(R.string.setting_sex_woman)) {
            mViewModel.title.set("${App.userInfo.name}，这里可以查看您的运动数据")
        } else {
            mViewModel.title.set("${App.userInfo.name}，这里可以查看您的运动数据")
        }

    }

    override fun onResume() {
        super.onResume()
        mViewModel.getLastTest()
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.sportsCalorieResult.observe(this) { it ->
            parseState(it, {
                initRadarChart(it)
                if (it != null) {
                    mDatabind.timeTv.visibility = View.VISIBLE
                    mDatabind.timeTv.text = DateTimeUtil.formatDate(it.test_time * 1000, DateTimeUtil.DATE_PATTERN_SS)
                    mDatabind.dashboard.setValue(it.result)
                    mDatabind.dashboard.setPointerAngle(AssessmentUtils.getResultLevel(it.lift_leg, it.sit_up, it.grip / 10f, App.userInfo.sex == "男").toFloat())
                    mDatabind.resultTv.text = it.result_description

                } else {
                    mDatabind.timeTv.visibility = View.GONE
                    mDatabind.dashboard.setPointerAngle(0f)
                    mDatabind.resultTv.text = "请完成测评"
                }

            })
        }
    }

    private fun initRadarChart(assessmentHistoryData: AssessmentHistoryData?) {
        val chart = mDatabind.radarChart
        chart.description.isEnabled = false
        chart.webLineWidth = 1f
        chart.webColor = Color.LTGRAY
        chart.setTouchEnabled(false)
        chart.webLineWidthInner = 1f
        chart.webColorInner = Color.LTGRAY
        chart.webAlpha = 100
        setData(assessmentHistoryData)
        chart.animateXY(1400, 1400, Easing.EaseInOutQuad)
        val xAxis = chart.xAxis
        xAxis.textSize = 9f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f
        var mActivities = arrayOf<String>()
        if (assessmentHistoryData == null) {
            mActivities = arrayOf(" - kg", " - 次", " - cm", " - kg", " - 次")
        } else {
            mActivities = arrayOf("${assessmentHistoryData.grip / 10f}kg", "${assessmentHistoryData.sit_up}次", "${assessmentHistoryData.waistline}cm", "${assessmentHistoryData.weight}kg", "${assessmentHistoryData.lift_leg}次")
        }
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return mActivities[value.toInt() % mActivities.size]
            }
        }
        xAxis.textSize = 20f
        xAxis.textColor = getColor(R.color.color_cc1c1c1c)

        val yAxis = chart.yAxis
        yAxis.setLabelCount(5, false)
        yAxis.textSize = 9f
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 80f
        yAxis.setDrawLabels(false)

        val l = chart.legend
        l.isEnabled = false
    }

    private fun setData(assessmentHistoryData: AssessmentHistoryData?) {
        val entries1 = ArrayList<RadarEntry>()

        if (assessmentHistoryData == null) {
            entries1.add(RadarEntry(0f))
            entries1.add(RadarEntry(0f))
            entries1.add(RadarEntry(0f))
            entries1.add(RadarEntry(0f))
            entries1.add(RadarEntry(0f))
        } else {
            entries1.add(RadarEntry(assessmentHistoryData.grip / 10f))
            entries1.add(RadarEntry(assessmentHistoryData.sit_up.toFloat()))
            entries1.add(RadarEntry(assessmentHistoryData.waistline.toFloat()))
            entries1.add(RadarEntry(assessmentHistoryData.weight.toFloat()))
            entries1.add(RadarEntry(assessmentHistoryData.lift_leg.toFloat()))
        }

        val set1 = RadarDataSet(entries1, "Last Week")
        set1.color = resources.getColor(R.color.color_cc4e71ff)
        set1.fillColor = resources.getColor(R.color.color_cc4e71ff)
        set1.setDrawFilled(true)
        set1.fillAlpha = 180
        set1.lineWidth = 1f
        set1.isDrawHighlightCircleEnabled = true
        set1.setDrawHighlightIndicators(false)

        val sets = ArrayList<IRadarDataSet>()
        sets.add(set1)
        val data = RadarData(sets)
        data.setValueTextSize(8f)
        data.setDrawValues(false)
        data.setValueTextColor(Color.WHITE)
        mDatabind.radarChart.data = data
        mDatabind.radarChart.invalidate()
    }

    inner class ProxyClick {

        fun clickFinish() {
            finish()
        }

        fun clickSelectedAssessmentPorts() {
            val intent = Intent(this@SportsAssessmentResultActivity, SportsAssessmentGuideActivity::class.java)
            startActivity(intent)
        }

        fun clickHistoryAssessment() {
            val intent = Intent(this@SportsAssessmentResultActivity, SportsAssessmentsHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}