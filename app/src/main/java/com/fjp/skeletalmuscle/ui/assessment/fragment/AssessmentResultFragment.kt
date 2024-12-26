package com.fjp.skeletalmuscle.ui.assessment.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.data.model.bean.result.AssessmentHistoryData
import com.fjp.skeletalmuscle.databinding.FragmentAssessmentResultBinding
import com.fjp.skeletalmuscle.viewmodel.state.AssessmentResultViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import me.hgj.jetpackmvvm.base.appContext

class AssessmentResultFragment(val year: Int, val curMonth: Int, val assessmentHistory: ArrayList<AssessmentHistoryData>) : BaseFragment<AssessmentResultViewModel, FragmentAssessmentResultBinding>() {

    companion object {
        fun newInstance(year: Int, curMonth: Int, assessmentHistory: ArrayList<AssessmentHistoryData>) = AssessmentResultFragment(year, curMonth, assessmentHistory)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        initRadarChart()
        if (curMonth == 1) {
            mDatabind.preMonth.visibility= View.GONE
            mDatabind.curMonth.text = "${year}-${curMonth}"
            mDatabind.nextMonth.visibility= View.GONE
        } else if (curMonth == 2) {
            mDatabind.preMonth.text = "${year}-${curMonth - 1}"
            mDatabind.curMonth.visibility=View.GONE
            mDatabind.nextMonth.text = "${year}-${curMonth}"
        } else{
            mDatabind.preMonth.text = "${year}-${curMonth -2}"
            mDatabind.curMonth.text = "${year}-${curMonth-1}"
            mDatabind.nextMonth.text = "${year}-${curMonth}"
        }
    }

    private fun initRadarChart() {
        val chart = mDatabind.radarChart
        chart.description.isEnabled = false
        chart.setTouchEnabled(false)
        chart.webLineWidth = 1f
        chart.webColor = Color.LTGRAY
        chart.webLineWidthInner = 1f
        chart.webColorInner = Color.LTGRAY
        chart.webAlpha = 100
        setData()
        chart.animateXY(1400, 1400, Easing.EaseInOutQuad)
        val xAxis = chart.xAxis
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f
        xAxis.setLabelCount(4, true)
        xAxis.axisMaximum = 4f
        xAxis.axisMinimum = 0f
        xAxis.textSize = 20f
        xAxis.setDrawLabels(false)

        xAxis.textColor = appContext.getColor(R.color.color_1c1c1c)

        val yAxis = chart.yAxis
        yAxis.axisMinimum = 0f
        yAxis.setLabelCount(5, false)
        yAxis.setDrawLabels(false)
        //设置字体大小
        yAxis.textSize = 15f
        //设置字体颜色
        yAxis.textColor = Color.RED

        val l = chart.legend
        l.isEnabled = false
    }

    private fun setData() {

        val entries1 = ArrayList<RadarEntry>()
        val entries2 = ArrayList<RadarEntry>()
        val entries3 = ArrayList<RadarEntry>()

        val sets = ArrayList<IRadarDataSet>()
        if (curMonth == 1) {
            val curAssessment = assessmentHistory[0]
            entries1.add(RadarEntry(curAssessment.grip/10f))
            entries1.add(RadarEntry(curAssessment.sit_up.toFloat()))
            entries1.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries1.add(RadarEntry(curAssessment.weight.toFloat()))
            entries1.add(RadarEntry(curAssessment.lift_leg.toFloat()))
            val set1 = RadarDataSet(entries1, "Last Week")
            set1.color = resources.getColor(R.color.color_blue)
            set1.lineWidth = 4f
            set1.setDrawValues(true)
            set1.isDrawHighlightCircleEnabled = true
            set1.setDrawHighlightIndicators(false)
            sets.add(set1)

        } else if (curMonth == 2) {
            var curAssessment = assessmentHistory[0]
            entries1.add(RadarEntry(curAssessment.grip/10f))
            entries1.add(RadarEntry(curAssessment.sit_up.toFloat()))
            entries1.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries1.add(RadarEntry(curAssessment.weight.toFloat()))
            entries1.add(RadarEntry(curAssessment.lift_leg.toFloat()))
            val set1 = RadarDataSet(entries1, "Last Week")
            set1.color = resources.getColor(R.color.color_blue)
            set1.lineWidth = 4f
            set1.isDrawHighlightCircleEnabled = true
            set1.setDrawValues(true)
            set1.setDrawHighlightIndicators(false)
            sets.add(set1)
            curAssessment = assessmentHistory[curMonth-1]
            entries3.add(RadarEntry(curAssessment.grip/10f))
            entries3.add(RadarEntry(curAssessment.sit_up.toFloat()))
            entries3.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries3.add(RadarEntry(curAssessment.weight.toFloat()))
            entries3.add(RadarEntry(curAssessment.lift_leg.toFloat()))

            val set2 = RadarDataSet(entries3, "This Week")
            set2.color = appContext.getColor(R.color.color_ffc019)
            set2.lineWidth = 4f
            set2.setDrawValues(true)
            set2.isDrawHighlightCircleEnabled = true
            set2.setDrawHighlightIndicators(false)
            sets.add(set2)
        } else {
            var curAssessment = assessmentHistory[curMonth - 3]
            entries1.add(RadarEntry(curAssessment.grip/10f))
            entries1.add(RadarEntry(curAssessment.sit_up.toFloat()))
            entries1.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries1.add(RadarEntry(curAssessment.weight.toFloat()))
            entries1.add(RadarEntry(curAssessment.lift_leg.toFloat()))
            val set1 = RadarDataSet(entries1, "Last Week")
            set1.color = resources.getColor(R.color.color_blue)
            set1.lineWidth = 4f
            set1.isDrawHighlightCircleEnabled = true
            set1.setDrawValues(true)
            set1.setDrawHighlightIndicators(false)
            sets.add(set1)

            curAssessment = assessmentHistory[curMonth - 2]
            entries2.add(RadarEntry(curAssessment.grip/10f))
            entries2.add(RadarEntry(curAssessment.sit_up.toFloat()))
            entries2.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries2.add(RadarEntry(curAssessment.weight.toFloat()))
            entries2.add(RadarEntry(curAssessment.lift_leg.toFloat()))
            val set2 = RadarDataSet(entries2, "This Week")
            set2.color = appContext.getColor(R.color.color_ffc019)
            set2.lineWidth = 4f
            set2.isDrawHighlightCircleEnabled = true
            set2.setDrawValues(true)
            set2.setDrawHighlightIndicators(false)
            sets.add(set2)

            curAssessment = assessmentHistory[curMonth-1]
            entries3.add(RadarEntry(curAssessment.grip/10f))
            entries3.add(RadarEntry(curAssessment.sit_up.toFloat()))
            entries3.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries3.add(RadarEntry(curAssessment.weight.toFloat()))
            entries3.add(RadarEntry(curAssessment.lift_leg.toFloat()))
            val set3 = RadarDataSet(entries3, "This Week")
            set3.color = appContext.getColor(R.color.color_ff574c)
            set3.lineWidth = 4f
            set3.isDrawHighlightCircleEnabled = true
            set3.setDrawHighlightIndicators(false)
            set3.setDrawValues(true)
            sets.add(set3)
        }

        val data = RadarData(sets)
        data.setValueTextSize(20f)
        data.setDrawValues(true)
        data.setValueTextColor(resources.getColor(R.color.color_801c1c1c))
        mDatabind.radarChart.data = data
        mDatabind.radarChart.invalidate()
    }


}