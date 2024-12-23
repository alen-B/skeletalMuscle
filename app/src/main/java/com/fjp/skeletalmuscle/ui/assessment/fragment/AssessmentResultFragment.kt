package com.fjp.skeletalmuscle.ui.assessment.fragment

import android.graphics.Color
import android.os.Bundle
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
            mDatabind.preMonth.text = "${year}-${curMonth}"
            mDatabind.curMonth.text = "${year}-${curMonth + 1}"
            mDatabind.nextMonth.text = "${year}-${curMonth + 2}"
        } else if (curMonth == 12) {
            mDatabind.preMonth.text = "${year}-${curMonth - 2}"
            mDatabind.curMonth.text = "${year}-${curMonth - 1}"
            mDatabind.nextMonth.text = "${year}-${curMonth}"
        } else {
            mDatabind.preMonth.text = "${year}-${curMonth - 1}"
            mDatabind.curMonth.text = "${year}-${curMonth}"
            mDatabind.nextMonth.text = "${year}-${curMonth + 1}"
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
        val curAssessment = assessmentHistory[curMonth - 1]

        val mActivities = arrayOf("${curAssessment.grip/10f}kg", "${curAssessment.sit_up}次", "${curAssessment.waistline}cm", "${curAssessment.weight}kg", "${curAssessment.lift_leg}次"

        )
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return mActivities[value.toInt() % mActivities.size]
            }
        }
        xAxis.setTextSize(34f)
        xAxis.textColor = appContext.getColor(R.color.color_1c1c1c)

        val yAxis = chart.yAxis
        yAxis.setLabelCount(5, false)
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 80f
        yAxis.setDrawLabels(false)

        val l = chart.legend
        l.isEnabled = false
    }

    private fun setData() {

        val entries1 = ArrayList<RadarEntry>()
        val entries2 = ArrayList<RadarEntry>()
        val entries3 = ArrayList<RadarEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        if (curMonth == 1) {
            var curAssessment = assessmentHistory[curMonth - 1]
            entries1.add(RadarEntry(curAssessment.grip/10f*2))
            entries1.add(RadarEntry(curAssessment.sit_up*2.toFloat()))
            entries1.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries1.add(RadarEntry(curAssessment.weight.toFloat()))
            entries1.add(RadarEntry(curAssessment.lift_leg.toFloat()))
            curAssessment = assessmentHistory[curMonth]
            entries2.add(RadarEntry(curAssessment.grip/10f))
            entries2.add(RadarEntry(curAssessment.sit_up.toFloat()))
            entries2.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries2.add(RadarEntry(curAssessment.weight.toFloat()))
            entries2.add(RadarEntry(curAssessment.lift_leg.toFloat()))

            curAssessment = assessmentHistory[curMonth + 1]
            entries3.add(RadarEntry(curAssessment.grip/10f*2))
            entries3.add(RadarEntry(curAssessment.sit_up*2.toFloat()))
            entries3.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries3.add(RadarEntry(curAssessment.weight.toFloat()))
            entries3.add(RadarEntry(curAssessment.lift_leg.toFloat()))

        } else if (curMonth == 12) {
            var curAssessment = assessmentHistory[curMonth - 3]
            entries1.add(RadarEntry(curAssessment.grip/10f*2))
            entries1.add(RadarEntry(curAssessment.sit_up*2.toFloat()))
            entries1.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries1.add(RadarEntry(curAssessment.weight.toFloat()))
            entries1.add(RadarEntry(curAssessment.lift_leg.toFloat()))
            curAssessment = assessmentHistory[curMonth - 2]
            entries2.add(RadarEntry(curAssessment.grip/10f*2))
            entries2.add(RadarEntry(curAssessment.sit_up*2.toFloat()))
            entries2.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries2.add(RadarEntry(curAssessment.weight.toFloat()))
            entries2.add(RadarEntry(curAssessment.lift_leg.toFloat()))

            curAssessment = assessmentHistory[curMonth - 1]
            entries3.add(RadarEntry(curAssessment.grip/10f*2))
            entries3.add(RadarEntry(curAssessment.sit_up*2.toFloat()))
            entries3.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries3.add(RadarEntry(curAssessment.weight.toFloat()))
            entries3.add(RadarEntry(curAssessment.lift_leg.toFloat()))

        } else if (curMonth == 11) {
            var curAssessment = assessmentHistory[curMonth - 2]
            entries1.add(RadarEntry(curAssessment.grip/10f*2))
            entries1.add(RadarEntry(curAssessment.sit_up*2.toFloat()))
            entries1.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries1.add(RadarEntry(curAssessment.weight.toFloat()))
            entries1.add(RadarEntry(curAssessment.lift_leg.toFloat()))
            curAssessment = assessmentHistory[curMonth - 1]
            entries2.add(RadarEntry(curAssessment.grip/10f*2))
            entries2.add(RadarEntry(curAssessment.sit_up*2.toFloat()))
            entries2.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries2.add(RadarEntry(curAssessment.weight.toFloat()))
            entries2.add(RadarEntry(curAssessment.lift_leg.toFloat()))

            curAssessment = assessmentHistory[curMonth]
            entries3.add(RadarEntry(curAssessment.grip/10f*2))
            entries3.add(RadarEntry(curAssessment.sit_up*2.toFloat()))
            entries3.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries3.add(RadarEntry(curAssessment.weight.toFloat()))
            entries3.add(RadarEntry(curAssessment.lift_leg.toFloat()))
        } else {
            var curAssessment = assessmentHistory[curMonth - 1]
            entries1.add(RadarEntry(curAssessment.grip/10f*2))
            entries1.add(RadarEntry(curAssessment.sit_up*2.toFloat()))
            entries1.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries1.add(RadarEntry(curAssessment.weight.toFloat()))
            entries1.add(RadarEntry(curAssessment.lift_leg.toFloat()))
            curAssessment = assessmentHistory[curMonth]
            entries2.add(RadarEntry(curAssessment.grip/10f))
            entries2.add(RadarEntry(curAssessment.sit_up.toFloat()))
            entries2.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries2.add(RadarEntry(curAssessment.weight.toFloat()))
            entries2.add(RadarEntry(curAssessment.lift_leg.toFloat()))

            curAssessment = assessmentHistory[curMonth + 1]
            entries3.add(RadarEntry(curAssessment.grip/10f))
            entries3.add(RadarEntry(curAssessment.sit_up.toFloat()))
            entries3.add(RadarEntry(curAssessment.waistline.toFloat()))
            entries3.add(RadarEntry(curAssessment.weight.toFloat()))
            entries3.add(RadarEntry(curAssessment.lift_leg.toFloat()))
        }

        val set1 = RadarDataSet(entries1, "Last Week")
        set1.color = resources.getColor(R.color.color_blue)
//        set1.setDrawFilled(true)
//        set1.fillAlpha = 180
        set1.lineWidth = 4f
        set1.isDrawHighlightCircleEnabled = true
        set1.setDrawHighlightIndicators(false)
        val set2 = RadarDataSet(entries2, "This Week")
        set2.color = appContext.getColor(R.color.color_ffc019)
//        set2.setDrawFilled(true)
//        set2.fillAlpha = 180
        set2.lineWidth = 4f
        set2.isDrawHighlightCircleEnabled = true
        set2.setDrawHighlightIndicators(false)

        val set3 = RadarDataSet(entries3, "This Week")
        set3.color = appContext.getColor(R.color.color_ff574c)
//        set2.setDrawFilled(true)
//        set2.fillAlpha = 180
        set3.lineWidth = 4f
        set3.isDrawHighlightCircleEnabled = true
        set3.setDrawHighlightIndicators(false)
        val sets = ArrayList<IRadarDataSet>()
        sets.add(set1)
        sets.add(set2)
        sets.add(set3)
        val data = RadarData(sets)
        data.setValueTextSize(34f)
        data.setDrawValues(false)
        data.setValueTextColor(Color.WHITE)
        mDatabind.radarChart.data = data
        mDatabind.radarChart.invalidate()
    }


}