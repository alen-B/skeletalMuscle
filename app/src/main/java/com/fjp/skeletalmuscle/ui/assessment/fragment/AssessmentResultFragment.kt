package com.fjp.skeletalmuscle.ui.assessment.fragment

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class AssessmentResultFragment(val curMonth:Int) : BaseFragment<AssessmentResultViewModel,FragmentAssessmentResultBinding>() {

    companion object {
        fun newInstance(curMonth:Int) = AssessmentResultFragment(curMonth)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel=mViewModel
        initRadarChart()
    }

    private fun initRadarChart() {
        val chart = mDatabind.radarChart
        chart.description.isEnabled = false
        chart.webLineWidth = 1f
        chart.webColor = Color.LTGRAY
        chart.webLineWidthInner = 1f
        chart.webColorInner = Color.LTGRAY
        chart.webAlpha = 100
        setData()
        chart.animateXY(1400, 1400, Easing.EaseInOutQuad)
        val xAxis = chart.xAxis
        xAxis.textSize = 9f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f
        val mActivities = arrayOf(getString(R.string.sports_assessment_history_result_max_grips),
            getString(R.string.sports_assessment_history_result_max_sit_up_times),
            getString(R.string.sports_assessment_waistline),
            getString(R.string.sports_assessment_weight),
            getString(R.string.sports_assessment_history_result_high_leg_times),
        )
        xAxis.valueFormatter = object: ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return mActivities[value.toInt() % mActivities.size]
            }
        }

        xAxis.textColor = appContext.getColor(R.color.color_801c1c1c)

        val yAxis = chart.yAxis
        yAxis.setLabelCount(5, false)
        yAxis.textSize = 9f
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 80f
        yAxis.setDrawLabels(false)

        val l = chart.legend
        l.isEnabled=false
    }

    private fun setData() {
        val mul = 80f
        val min = 20f
        val cnt = 5
        val entries1 = ArrayList<RadarEntry>()
        val entries2 = ArrayList<RadarEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until cnt) {
            val val1 = (Math.random() * mul).toFloat() + min
            entries1.add(RadarEntry(val1))
            val val2 = (Math.random() * mul).toFloat() + min
            entries2.add(RadarEntry(val2))
        }
        val set1 = RadarDataSet(entries1, "Last Week")
        set1.color = Color.rgb(103, 110, 129)
        set1.fillColor = Color.rgb(103, 110, 129)
        set1.setDrawFilled(true)
        set1.fillAlpha = 180
        set1.lineWidth = 1f
        set1.isDrawHighlightCircleEnabled = true
        set1.setDrawHighlightIndicators(false)
        val set2 = RadarDataSet(entries2, "This Week")
        set2.color = appContext.getColor(R.color.color_1a4e71ff)
        set2.fillColor = appContext.getColor(R.color.color_1a4e71ff)
        set2.setDrawFilled(true)
        set2.fillAlpha = 180
        set2.lineWidth = 1f
        set2.isDrawHighlightCircleEnabled = true
        set2.setDrawHighlightIndicators(false)
        val sets = ArrayList<IRadarDataSet>()
        sets.add(set1)
        sets.add(set2)
        val data = RadarData(sets)
        data.setValueTextSize(8f)
        data.setDrawValues(false)
        data.setValueTextColor(Color.WHITE)
        mDatabind.radarChart.data = data
        mDatabind.radarChart.invalidate()
    }


}