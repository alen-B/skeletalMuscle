package com.fjp.skeletalmuscle.ui.assessment.fragment

import android.graphics.Color
import android.os.Bundle
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.databinding.FragmentAssessmentResultBinding
import com.fjp.skeletalmuscle.viewmodel.state.AssessmentResultViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import me.hgj.jetpackmvvm.base.appContext

class AssessmentResultFragment(val year: Int, val curMonth: Int) : BaseFragment<AssessmentResultViewModel, FragmentAssessmentResultBinding>() {

    companion object {
        fun newInstance(year: Int, curMonth: Int) = AssessmentResultFragment(year, curMonth)
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
        }else{
            mDatabind.preMonth.text = "${year}-${curMonth - 1}"
            mDatabind.curMonth.text = "${year}-${curMonth}"
            mDatabind.nextMonth.text = "${year}-${curMonth+1}"
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
        val mActivities = arrayOf("握力", "起坐", "腰围", "体重", "抬腿"
//            getString(R.string.sports_assessment_history_result_max_grips),
//            getString(R.string.sports_assessment_history_result_max_sit_up_times),
//            getString(R.string.sports_assessment_waistline),
//            getString(R.string.sports_assessment_weight),
//            getString(R.string.sports_assessment_history_result_high_leg_times),

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
        val mul = 80f
        val min = 20f
        val cnt = 5
        val entries1 = ArrayList<RadarEntry>()
        val entries2 = ArrayList<RadarEntry>()
        val entries3 = ArrayList<RadarEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until cnt) {
            val val1 = (Math.random() * mul).toFloat() + min
            entries1.add(RadarEntry(val1))
            val val2 = (Math.random() * mul).toFloat() + min
            entries2.add(RadarEntry(val2))
            val val3 = (Math.random() * mul).toFloat() + min
            entries3.add(RadarEntry(val3))

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