package com.fjp.skeletalmuscle.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.databinding.ActivityCalendarBinding
import com.fjp.skeletalmuscle.view.calendar.SMWeekBar
import com.fjp.skeletalmuscle.viewmodel.state.CalendarViewModel
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView


class CalendarActivity : BaseActivity<CalendarViewModel,ActivityCalendarBinding>(), CalendarView.OnCalendarSelectListener,
    CalendarView.OnYearChangeListener,
    View.OnClickListener  {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel=mViewModel
        mDatabind.click=ProxyClick()
        mDatabind.calendarLayout.expand()
        mViewModel.title.set(getString(R.string.calendar_title))
        val map: MutableMap<String, Calendar?> = HashMap()
        val year: Int = mDatabind.calendarView.getCurYear()
        val month: Int = mDatabind.calendarView.getCurMonth()
        setCalendarTitle(year,month)

        map[getSchemeCalendar(year, month, 3, -0xbf24db, "20").toString()] = getSchemeCalendar(year, month, 3, -0xbf24db, "20")
        map[getSchemeCalendar(year, month, 6, -0x196ec8, "33").toString()] = getSchemeCalendar(year, month, 6, -0x196ec8, "33")
        map[getSchemeCalendar(year, month, 9, -0x20ecaa, "25").toString()] = getSchemeCalendar(year, month, 9, -0x20ecaa, "25")
        map[getSchemeCalendar(year, month, 13, -0x123a93, "50").toString()] = getSchemeCalendar(year, month, 13, -0x123a93, "50")
        map[getSchemeCalendar(year, month, 14, -0x123a93, "80").toString()] = getSchemeCalendar(year, month, 14, -0x123a93, "80")
        map[getSchemeCalendar(year, month, 15, -0x5533bc, "20").toString()] = getSchemeCalendar(year, month, 15, -0x5533bc, "20")
        map[getSchemeCalendar(year, month, 18, -0x43ec10, "70").toString()] = getSchemeCalendar(year, month, 18, -0x43ec10, "70")
        map[getSchemeCalendar(year, month, 25, -0xec5310, "36").toString()] = getSchemeCalendar(year, month, 25, -0xec5310, "36")
        map[getSchemeCalendar(year, month, 27, -0xec5310, "95").toString()] = getSchemeCalendar(year, month, 27, -0xec5310, "95")
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mDatabind.calendarView.setSchemeDate(map)
        mDatabind.calendarView.setOnCalendarSelectListener(this)
        mDatabind.calendarView.setOnYearChangeListener(this)
        mDatabind.calendarView.setOnMonthChangeListener { year, month ->
            setCalendarTitle(year,month)
        }
        mDatabind.calendarView.setWeekBar(SMWeekBar::class.java)
    }

    private fun setCalendarTitle(year:Int,month:Int){
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
        if(isClick){
            showToast("点击了"+calendar.month+"月"+calendar.day+"日")
            val intent = Intent(this@CalendarActivity,SportsRecordActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_YEAR,calendar.year)
            intent.putExtra(Constants.INTENT_KEY_MONTH,calendar.month)
            intent.putExtra(Constants.INTENT_KEY_DAY,calendar.day)
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

    inner class ProxyClick{
        fun clickPreMonth(){
            mDatabind.calendarView.scrollToPre()
        }
        fun clickNextMonth(){
            mDatabind.calendarView.scrollToNext()
        }
        fun clickfinish(){
           this@CalendarActivity.finish()
        }

    }
}