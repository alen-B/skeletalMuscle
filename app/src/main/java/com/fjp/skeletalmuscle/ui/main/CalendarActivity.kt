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


class CalendarActivity : BaseActivity<CalendarViewModel, ActivityCalendarBinding>(), CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, View.OnClickListener {
    var year:Int=0
    var month:Int=0
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.calendarLayout.expand()
        mViewModel.title.set(getString(R.string.calendar_title))

        year= mDatabind.calendarView.getCurYear()
        month= mDatabind.calendarView.getCurMonth()
        setCalendarTitle(year, month)
//        val map: MutableMap<String, Calendar?> = HashMap()
//        map[getSchemeCalendar(year, 11, 3, -0xbf24db, "20").toString()] = getSchemeCalendar(year, month, 3, -0xbf24db, "20")
//        map[getSchemeCalendar(year, 11, 6, -0x196ec8, "33").toString()] = getSchemeCalendar(year, month, 6, -0x196ec8, "33")
//        map[getSchemeCalendar(year, 11, 9, -0x20ecaa, "25").toString()] = getSchemeCalendar(year, month, 9, -0x20ecaa, "25")
//        map[getSchemeCalendar(year, 11, 13, -0x123a93, "50").toString()] = getSchemeCalendar(year, month, 13, -0x123a93, "50")
//        map[getSchemeCalendar(year, 11, 14, -0x123a93, "80").toString()] = getSchemeCalendar(year, month, 14, -0x123a93, "80")
//        map[getSchemeCalendar(year, 11, 15, -0x5533bc, "20").toString()] = getSchemeCalendar(year, month, 15, -0x5533bc, "20")
//        map[getSchemeCalendar(year, 11, 18, -0x43ec10, "70").toString()] = getSchemeCalendar(year, month, 18, -0x43ec10, "70")
//        map[getSchemeCalendar(year, 11, 25, -0xec5310, "36").toString()] = getSchemeCalendar(year, month, 25, -0xec5310, "36")
//        map[getSchemeCalendar(year, 11, 27, -0xec5310, "95").toString()] = getSchemeCalendar(year, month, 27, -0xec5310, "95")
//        //此方法在巨大的数据量上不影响遍历性能，推荐使用
//        mDatabind.calendarView.setSchemeDate(map)
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
        val map: MutableMap<String, Calendar?> = HashMap()
        mViewModel.response.observe(this){
            parseState(it,{list->
                    setCalendarData(list)
            })



        }
    }

    private fun setCalendarData(list: ArrayList<CalendarResult>) {
        val map: MutableMap<String, Calendar?> = HashMap()
        for ((index, calendarResult) in list.withIndex()) {
            val data = DateTimeUtil.formatDate(DateTimeUtil.DATE_PATTERN,calendarResult.date)
            val score = calendarResult.score.toDouble().toInt()
            var color = Color.parseColor("#FF574C")
            if(score>80){
                color = resources.getColor(R.color.color_blue)
            }else if(score>60){
                color =Color.parseColor("#FFC019")
            }
            map[getSchemeCalendar(year, month, data.day, color,score.toString()).toString()] = getSchemeCalendar(year, month, data.day, color,score.toString())
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
//        mDatabind.tvLunar.visibility = View.VISIBLE
//        mDatabind.tvYear.visibility = View.VISIBLE
//        mDatabind.tvMonthDay.text = "${calendar.month}月${calendar.getDay()} 日";
//        mDatabind.tvYear.text = calendar.year.toString();
//        mDatabind.tvLunar.text = calendar.lunar;
//        mYear = calendar.year
        if (isClick) {
//            showToast("点击了"+calendar.month+"月"+calendar.day+"日")
            val intent = Intent(this@CalendarActivity, SportsRecordActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_YEAR, calendar.year)
            intent.putExtra(Constants.INTENT_KEY_MONTH, calendar.month)
            intent.putExtra(Constants.INTENT_KEY_DAY, calendar.day)
            startActivity(intent)
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