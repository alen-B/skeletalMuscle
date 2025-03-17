package com.fjp.skeletalmuscle.ui.main.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.data.model.bean.TodayDetailSportsType
import com.fjp.skeletalmuscle.data.model.bean.TodaySports
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import me.hgj.jetpackmvvm.base.appContext


class TodaySportsDataAdapter(data: ArrayList<TodaySports>, var clickItem: (item: TodaySports) -> Unit) : BaseQuickAdapter<TodaySports, BaseViewHolder>(R.layout.item_today_sports_data, data) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: TodaySports) {
        muscleProportionVisiable(holder, item.type == TodayDetailSportsType.MUSCLE_PROPORTION)
        aerobicVisiable(holder, item.type == TodayDetailSportsType.AEROBIC_ENERGY_CONSUMPTION)

        holder.setVisible(R.id.lineChart, item.type == TodayDetailSportsType.Heart_RATE)
        holder.setVisible(R.id.arrow2Iv, !(item.type == TodayDetailSportsType.MUSCLE_PROPORTION || item.type == TodayDetailSportsType.AEROBIC_ENERGY_CONSUMPTION))
        holder.setVisible(R.id.arrow1Iv, !(item.type == TodayDetailSportsType.MUSCLE_PROPORTION || item.type == TodayDetailSportsType.AEROBIC_ENERGY_CONSUMPTION))
        holder.setVisible(R.id.sport_timeTv, !(item.type == TodayDetailSportsType.MUSCLE_PROPORTION || item.type == TodayDetailSportsType.AEROBIC_ENERGY_CONSUMPTION))
        holder.setVisible(R.id.sport_timeUnitTv, !(item.type == TodayDetailSportsType.MUSCLE_PROPORTION || item.type == TodayDetailSportsType.AEROBIC_ENERGY_CONSUMPTION))
        holder.setVisible(R.id.sport_kilocalorieTv, !(item.type == TodayDetailSportsType.MUSCLE_PROPORTION || item.type == TodayDetailSportsType.AEROBIC_ENERGY_CONSUMPTION))
        holder.setVisible(R.id.sport_kilocalorieUnitTv, !(item.type == TodayDetailSportsType.MUSCLE_PROPORTION || item.type == TodayDetailSportsType.AEROBIC_ENERGY_CONSUMPTION))
        holder.setVisible(R.id.detailIv, !(item.type == TodayDetailSportsType.MUSCLE_PROPORTION || item.type == TodayDetailSportsType.AEROBIC_ENERGY_CONSUMPTION))
        if (item.type == TodayDetailSportsType.HIGH_KNEE) {
            holder.setText(R.id.sportTypeTv, appContext.getString(R.string.today_sports_data_type1))
            holder.setVisible(R.id.chart, true)
            initBarChart(holder)
        } else if (item.type == TodayDetailSportsType.DUMBBELL) {
            holder.setText(R.id.sportTypeTv, appContext.getString(R.string.today_sports_data_type2))
            holder.setVisible(R.id.chart, true)
            initBarChart(holder)

        } else if (item.type == TodayDetailSportsType.Plank) {
            holder.setVisible(R.id.chart, true)
            initBarChart(holder)
            holder.setText(R.id.sportTypeTv, appContext.getString(R.string.today_sports_data_type3))

        } else if (item.type == TodayDetailSportsType.DURATION_OF_EXERCISE) {
            holder.setVisible(R.id.chart, true)
            initBarChart(holder)
            holder.setText(R.id.sportTypeTv, appContext.getString(R.string.today_sports_data_type5))
        } else if (item.type == TodayDetailSportsType.Heart_RATE) {
            holder.setVisible(R.id.chart, false)
            initLineChat(holder)
            holder.setText(R.id.sportTypeTv, appContext.getString(R.string.today_sports_data_type4))

        } else if (item.type == TodayDetailSportsType.MUSCLE_PROPORTION) {
            holder.setVisible(R.id.chart, false)
            holder.setText(R.id.sportTypeTv, appContext.getString(R.string.today_sports_data_type6))

        } else if (item.type == TodayDetailSportsType.AEROBIC_ENERGY_CONSUMPTION) {
            holder.setVisible(R.id.chart, false)
            holder.setText(R.id.sportTypeTv, appContext.getString(R.string.today_sports_data_type7))

        }


        holder.itemView.setOnClickListener {
            clickItem.invoke(item)
        }

    }

    private fun initLineChat(holder: BaseViewHolder) {
        val lineChart = holder.getView<LineChart>(R.id.lineChart)
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
        leftAxis.gridLineWidth = 0.5f
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        leftAxis.isEnabled = false

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
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius = 4f
        lineDataSet.setColor(Color.parseColor("#FFDD82"))
//        lineDataSet.setCircleColor(Color.BLACK)

        // line thickness and point size

        // line thickness and point size
//        lineDataSet.setLineWidth(1f)
//        lineDataSet.setCircleRadius(3f)
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
            val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_yellow)
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

    private fun muscleProportionVisiable(holder: BaseViewHolder, visiable: Boolean) {
        holder.setVisible(R.id.circleIv, visiable)
        holder.setVisible(R.id.muscleProportionvalueTv, visiable)
        holder.setVisible(R.id.unitTv, visiable)
    }

    private fun aerobicVisiable(holder: BaseViewHolder, visiable: Boolean) {
        holder.setVisible(R.id.aerobicBgIv, visiable)
        holder.setVisible(R.id.aerobicValTv, visiable)
    }

    private fun initBarChart(holder: BaseViewHolder) {
        val barChart = holder.getView<BarChart>(R.id.chart)
        barChart.setTouchEnabled(false)
        barChart.isDragEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setDrawBorders(false)
        barChart.setDrawGridBackground(false)

        barChart.extraBottomOffset = 18f
        barChart.extraRightOffset = 18f
        val description = Description()
        description.text = ""
        barChart.description = description
        val xAxis = barChart.xAxis
        xAxis.isEnabled = false

        val leftAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.gridLineWidth = 0.5f
        leftAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        leftAxis.isEnabled = false
        leftAxis.enableGridDashedLine(2f, 1f, 0f)
        leftAxis.axisMinimum = 0f
        val rightAxis: YAxis = barChart.axisRight
        rightAxis.gridLineWidth = 0.5f
        rightAxis.gridColor = ContextCompat.getColor(appContext, R.color.color_gray)
        rightAxis.isEnabled = false

        barChart.legend.isEnabled = false
        val values = ArrayList<BarEntry>()

        for (i in 0 until 24) {
            if (i < 8 || i > 20) {
                values.add(BarEntry(i.toFloat(), 0.0f))
            } else {
                val num = (Math.random() * 180).toFloat()
                values.add(BarEntry(i.toFloat(), num))
            }

        }
        val dataSets = ArrayList<IBarDataSet>()
        val barDataSet = BarDataSet(values, "千卡")
        dataSets.add(barDataSet)
        barDataSet.setDrawIcons(false)
        barDataSet.color = ContextCompat.getColor(appContext, R.color.color_blue)
        if (holder.adapterPosition == 1) {
            barDataSet.color = ContextCompat.getColor(appContext, R.color.color_ffc019)
        } else if (holder.adapterPosition == 2) {
            barDataSet.color = ContextCompat.getColor(appContext, R.color.color_ff574c)
        }

        barDataSet.setDrawValues(false)//不显示柱状图上数据
        val barData = BarData(dataSets)
        barData.barWidth = 0.4f
        barChart.data = barData
        barChart.setNoDataText("暂无数据")
        barChart.animateY(0)
    }
}


