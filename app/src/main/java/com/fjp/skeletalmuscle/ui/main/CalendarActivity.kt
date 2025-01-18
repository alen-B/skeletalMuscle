package com.fjp.skeletalmuscle.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.weight.calendar.SMWeekBar
import com.fjp.skeletalmuscle.data.model.bean.result.CalendarResult
import com.fjp.skeletalmuscle.databinding.ActivityCalendarBinding
import com.fjp.skeletalmuscle.viewmodel.state.CalendarViewModel
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import me.hgj.jetpackmvvm.ext.parseState
import java.time.LocalDate


class CalendarActivity : BaseActivity<CalendarViewModel, ActivityCalendarBinding>(), CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, View.OnClickListener {
    var year: Int = 0
    var month: Int = 0
    var list: ArrayList<CalendarResult> = arrayListOf()
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.calendarLayout.expand()
        mViewModel.title.set(getString(R.string.calendar_title))

        year = mDatabind.calendarView.getCurYear()
        month = mDatabind.calendarView.getCurMonth()
        setCalendarTitle(year, month)
        mDatabind.calendarView.setOnCalendarSelectListener(this)
        mDatabind.calendarView.setOnYearChangeListener(this)
        mDatabind.calendarView.setOnMonthChangeListener { year, month ->
            this.year = year
            this.month = month
            setCalendarTitle(year, month)
            mViewModel.calendar("${year}-${month}")
        }
        mViewModel.calendar("${year}-${month}")
        mDatabind.calendarView.setWeekBar(SMWeekBar::class.java)
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.response.observe(this) {
            parseState(it, { list ->
                this.list = list
                setCalendarData(list)
            })


        }
    }

    private fun setCalendarData(list: ArrayList<CalendarResult>) {
        val map: MutableMap<String, Calendar?> = HashMap()
        for ((index, calendarResult) in list.withIndex()) {
            val data = DateTimeUtil.formatDate(DateTimeUtil.DATE_PATTERN, calendarResult.date)
            val score = calendarResult.score.toDouble().toInt()
            var color = Color.parseColor("#FF574C")
            if (score > 80) {
                color = resources.getColor(R.color.color_blue)
            } else if (score > 60) {
                color = Color.parseColor("#FFC019")
            } else {
                color = Color.parseColor("#FF574C")
            }
            val calendar = LocalDate.parse(calendarResult.date)
            map[getSchemeCalendar(year, month, calendar.dayOfMonth, Color.parseColor("#FF574C"), "81").toString()] = getSchemeCalendar(year, month, data.day, color, score.toString())
            map[getSchemeCalendar(year, month, calendar.dayOfMonth, Color.parseColor("#FFC019"), "70").toString()] = getSchemeCalendar(year, month, data.day, color, score.toString())
            map[getSchemeCalendar(year, month, calendar.dayOfMonth, Color.parseColor("#FF574C"), "50").toString()] = getSchemeCalendar(year, month, data.day, color, score.toString())
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mDatabind.calendarView.setSchemeDate(map)
    }

    private fun setCalendarTitle(year: Int, month: Int) {
        mViewModel.calendarTitle.set("${year}年${month}月")
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {
    }

    override fun onCalendarSelect(calendar: Calendar, isClick: Boolean) {
        if (isClick) {
            list.forEach {
                var day = "1"
                var month = "1"
                if (calendar.day < 10) {
                    day = "0" + calendar.day
                } else {
                    day = calendar.day.toString()
                }
                if(calendar.month<10){
                    month = "0${calendar.month}"
                }else{
                    month = calendar.month.toString()
                }
                if (it.date == "${calendar.year}-${month}-${day}") {
                    val intent = Intent(this@CalendarActivity, SportsRecordActivity::class.java)
                    intent.putExtra(Constants.INTENT_KEY_YEAR, calendar.year)
                    intent.putExtra(Constants.INTENT_KEY_MONTH, calendar.month)
                    intent.putExtra(Constants.INTENT_KEY_DAY, calendar.day)
                    startActivity(intent)
                }
            }
        }
    }

    private fun getSchemeCalendar(year: Int, month: Int, day: Int, color: Int, text: String): Calendar? {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        return calendar
    }

    override fun onYearChange(year: Int) {
//        mDatabind.tvMonthDay.setText(year.toString());
    }

    override fun onClick(p0: View?) {
    }

    inner class ProxyClick {
        fun clickPreMonth() {
            mDatabind.calendarView.scrollToPre()
        }

        fun clickNextMonth() {
            mDatabind.calendarView.scrollToNext()
        }

        fun clickfinish() {
            this@CalendarActivity.finish()
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