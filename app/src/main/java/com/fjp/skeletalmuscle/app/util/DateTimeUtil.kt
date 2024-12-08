package com.fjp.skeletalmuscle.app.util

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　: 时间工具类
 */

object DateTimeUtil {

    val DATE_PATTERN = "yyyy-MM-dd"
    val DATE_PATTERN2 = "yyyy年MM月dd日"
    val DATE_PATTERN3 = "yyyy年MM月"
    var DATE_PATTERN_SS = "yyyy-MM-dd HH:mm:ss"
    var DATE_PATTERN_MM = "yyyy-MM-dd HH:mm"

    /**
     * 获取现在时刻
     */
    val now: Date
        get() = Date(Date().time)

    /**
     * 获取现在时刻
     */
    val nows: Date
        get() = formatDate(DATE_PATTERN, now)


    fun getCurDate2Str(): String {
        return formatDate(System.currentTimeMillis(), DATE_PATTERN)
    }

    /**
     * Date to Strin
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date?, formatStyle: String): String {
        return if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            sdf.format(date)
        } else {
            ""
        }

    }

    /**
     * Date to Strin
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Long, formatStyle: String): String {
        val sdf = SimpleDateFormat(formatStyle)
        return sdf.format(Date(date))

    }

    fun formatDate(formatStyle: String, formatStr: String): Date {
        val format = SimpleDateFormat(formatStyle, Locale.CHINA)
        return try {
            return format.parse(formatStr)
        } catch (e: Exception) {
            println(e.message)
            nows
        }

    }

    /**
     * Date to Date
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(formatStyle: String, date: Date?): Date {
        if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            val formatDate = sdf.format(date)
            try {
                return sdf.parse(formatDate)
            } catch (e: ParseException) {
                e.printStackTrace()
                return Date()
            }

        } else {
            return Date()
        }
    }


    /**
     * 将时间戳转换为时间
     */
    fun stampToDate(s: String): Date {
        val lt = s.toLong()
        return Date(lt)
    }

    /**
     * 获得指定时间的日期
     */
    fun getCustomTime(dateStr: String): Date {
        return formatDate(DATE_PATTERN, dateStr)
    }

    fun formSportTime(time: Long): String {
        return String.format("%.1f", (time / 60f))
    }

    fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val remainingSecondsAfterHours = seconds % 3600
        val minutes = remainingSecondsAfterHours / 60
        val remainingSecondsAfterMinutes = remainingSecondsAfterHours % 60

        return when {
            hours > 0 -> "$hours H"
            minutes > 0 -> "$minutes Min"
            else -> "$remainingSecondsAfterMinutes sec"
        }
    }

    fun getCurWeek(): String {
        val today = LocalDate.now()
        val startOfWeek = getStartOfWeek(today)
        val endOfWeek = getEndOfWeek(today)

        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN2)
        val startDateFormatted = startOfWeek.format(formatter)
        val endDateFormatted = endOfWeek.format(formatter)
        return startDateFormatted + endDateFormatted
    }

    fun getCurMonth(): String {
        val date = Date()
        val formatter = SimpleDateFormat(DATE_PATTERN3)
        return formatter.format(date)
    }

    fun getPreYear(year:Int,month:Int): Int {
        val specifiedDate = LocalDate.of(year, month, 15)
        // 获取上一年的日期
       return specifiedDate.minusYears(1).year
    }

    fun getNextYear(year:Int,month:Int): Int {
        val specifiedDate = LocalDate.of(year, month, 15)
        // 获取上一年的日期
        return specifiedDate.plusYears(1).year
    }

    private fun getStartOfWeek(currentDate: LocalDate): LocalDate {
        val dayOfWeek = currentDate.dayOfWeek
        return currentDate.minusDays((dayOfWeek.value - 1).toLong())
    }

    private fun getEndOfWeek(currentDate: LocalDate): LocalDate {
        val dayOfWeek = currentDate.dayOfWeek
        return currentDate.plusDays((DayOfWeek.SATURDAY.value - dayOfWeek.value).toLong())
    }
}
