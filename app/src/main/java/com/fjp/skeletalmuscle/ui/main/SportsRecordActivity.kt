package com.fjp.skeletalmuscle.ui.main

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.dp
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.weight.recyclerview.SpaceItemDecoration
import com.fjp.skeletalmuscle.databinding.ActivitySportsRecordBinding
import com.fjp.skeletalmuscle.ui.main.adapter.SportsRecordLegAdapter
import com.fjp.skeletalmuscle.viewmodel.state.SportsRecordViewModel
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import me.hgj.jetpackmvvm.base.appContext
import java.util.Calendar

class SportsRecordActivity : BaseActivity<SportsRecordViewModel,ActivitySportsRecordBinding>() {

    val calendar = Calendar.getInstance()
    override fun initView(savedInstanceState: Bundle?) {
        val year = intent.getIntExtra(Constants.INTENT_KEY_YEAR, 0)
        val month = intent.getIntExtra(Constants.INTENT_KEY_MONTH, 0)
        val day = intent.getIntExtra(Constants.INTENT_KEY_DAY, 0)

        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        calendar.set(year,month,day)

        mViewModel.title.set("看看${year}年${month}月${day}日运动记录吧")
        mViewModel.leftImg.set(R.drawable.title_icon_sports_record)
        findViewById<TextView>(R.id.titleTv).setTextColor(ContextCompat.getColor(appContext,R.color.white))
        setCalendarTitle()
        initSportsLeg()
        initSportsDumbbell()
        initSportsPlank()
        initLineChart()
    }


    private fun initSportsDumbbell() {
        mDatabind.dumbbellRecyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), SportsRecordLegAdapter(mViewModel.dumbbellSportsDate))
        mDatabind.dumbbellRecyclerView.addItemDecoration(SpaceItemDecoration( 16.dp.toInt(),0))
    }
    private fun initSportsPlank() {
        mDatabind.plankRecyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), SportsRecordLegAdapter(mViewModel.plankSportsDate))
        mDatabind.plankRecyclerView.addItemDecoration(SpaceItemDecoration( 16.dp.toInt(),0))
    }

    private fun initSportsLeg() {
        mDatabind.legRecyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), SportsRecordLegAdapter(mViewModel.legSportsDate))
        mDatabind.legRecyclerView.addItemDecoration(SpaceItemDecoration( 16.dp.toInt(),0))
    }

    fun setCalendarTitle(){
        mViewModel.calendarTitle.set("${calendar.year}年${calendar.month}月${calendar.dayOfMonth}日")
        mViewModel.legSportsTime.set("${calendar.month}月${calendar.dayOfMonth}日")
        mViewModel.dumbbellSportsTime.set("${calendar.month}月${calendar.dayOfMonth}日")
        mViewModel.plankSportsTime.set("${calendar.month}月${calendar.dayOfMonth}日")
    }
    private fun initLineChart() {
        val lineChart = mDatabind.lineChart
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setDrawBorders(false)
        lineChart.setDrawGridBackground(false)
        val description = Description()
        description.text = ""
        lineChart.description = description
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.textColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        xAxis.setDrawGridLines(false)
        xAxis.enableGridDashedLine(2f,1f,0f)
        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.enableGridDashedLine(2f,1f,0f)
        leftAxis.enableAxisLineDashedLine(2f,1f,0f)
        leftAxis.gridLineWidth = 0.5f
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)
        leftAxis.textColor = ContextCompat.getColor(appContext, R.color.color_331c1c1c)

        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        val values = ArrayList<Entry>()

        for (i in 0 until 8) {
            val num = (Math.random() * 180).toFloat() - 30
            values.add(BarEntry(i.toFloat(), num))
        }
        val dataSets = ArrayList<ILineDataSet>()
        val lineDataSet = LineDataSet(values, "千卡")
        lineDataSet.setDrawIcons(false)
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawCircles(false)
        lineDataSet.color = ContextCompat.getColor(appContext, R.color.color_blue)
        lineDataSet.setDrawCircleHole(false)

        // text size of values
        lineDataSet.setDrawValues(false)


        // draw selection line as dashed
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)

        // set the filled area

        // set the filled area
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillFormatter = IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }

        // set color of filled area

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_blue)
            lineDataSet.fillDrawable = drawable
        } else {
            lineDataSet.fillColor = Color.BLACK
        }
        dataSets.add(lineDataSet)
        val barData = LineData(dataSets)
        lineChart.data = barData
        lineChart.setNoDataText("暂无数据")
        lineChart.animateY(500)
    }
    inner class ProxyClick{
        fun clickPreDay(){
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            setCalendarTitle()
        }
        fun clickNextDay(){
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            setCalendarTitle()
        }
        fun clickfinish(){
            this@SportsRecordActivity.finish()
        }
    }
    override fun onResume() {
        super.onResume()
        mDatabind.blurLayout.startBlur()
    }
    override fun onPause() {
        super.onPause()
        mDatabind.blurLayout.pauseBlur()
    }
}