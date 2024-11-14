package me.hgj.jetpackmvvm.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar
import java.util.Date

/**
 *Author:Mr'x
 *Time:2024/11/12
 *Description:
 */
object  DateUtils {
     fun calculateAge(birthdayDate: Date, currentDate: Date): Int {
        val birthdayCalendar: Calendar = Calendar.getInstance()
         birthdayCalendar.time = birthdayDate
        val currentCalendar: Calendar = Calendar.getInstance()
         currentCalendar.time = currentDate
        val currentYear: Int = currentCalendar.get(Calendar.YEAR)
        val birthdayYear: Int = birthdayCalendar.get(Calendar.YEAR)
        var age = currentYear - birthdayYear

        // 如果当前日期的月份和日期小于生日日期的月份和日期，说明生日还未到，年龄需要减1
        if (currentCalendar.get(Calendar.MONTH) < birthdayCalendar.get(Calendar.MONTH) ||
            currentCalendar.get(Calendar.MONTH) === birthdayCalendar.get(Calendar.MONTH) && currentCalendar.get(Calendar.DAY_OF_MONTH) < birthdayCalendar.get(Calendar.DAY_OF_MONTH)) {
            age--
        }
        return age
    }

    fun formatDouble(value: Double): Double {
        return BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}