package com.rg.ireminders.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * Date utils
 */
//TODO: convert to extensions
object DateUtils {
    fun getDueDateTime(milliseconds: Long?): String {
        val date = Date(milliseconds!!)
        val dateFormat = DateFormat.getInstance()

        return dateFormat.format(date)
    }

    fun getDueTime(milliseconds: Long?): String {
        val date = Date(milliseconds!!)
        val dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

        return dateFormat.format(date)
    }

    fun getDueDate(milliseconds: Long?): String {
        val date = Date(milliseconds!!)
        val dateFormat = DateFormat.getDateInstance()

        return dateFormat.format(date)
    }

    fun getDaysCount(milliseconds: Long?): Int {
        val todayCalendar = Calendar.getInstance()
        val dueCalendar = Calendar.getInstance()
        dueCalendar.timeInMillis = milliseconds!!

        if (dueCalendar.timeInMillis >= todayCalendar.timeInMillis) {
            todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
            todayCalendar.set(Calendar.MINUTE, 0)
            todayCalendar.set(Calendar.SECOND, 1)

            dueCalendar.set(Calendar.HOUR_OF_DAY, 23)
            dueCalendar.set(Calendar.MINUTE, 59)
            dueCalendar.set(Calendar.SECOND, 59)
        } else {
            todayCalendar.set(Calendar.HOUR_OF_DAY, 23)
            todayCalendar.set(Calendar.MINUTE, 59)
            todayCalendar.set(Calendar.SECOND, 59)

            dueCalendar.set(Calendar.HOUR_OF_DAY, 0)
            dueCalendar.set(Calendar.MINUTE, 0)
            dueCalendar.set(Calendar.SECOND, 1)
        }

        val endOfDay = todayCalendar.timeInMillis
        val due = dueCalendar.timeInMillis

        return ((due - endOfDay) / (60 * 60 * 24 * 1000)).toInt()
    }

}
