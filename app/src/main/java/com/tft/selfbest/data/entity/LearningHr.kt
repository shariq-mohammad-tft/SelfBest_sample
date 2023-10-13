package com.tft.selfbest.data.entity

import com.tft.selfbest.utils.Constants.Companion.DAY_LIST
import java.text.SimpleDateFormat
import java.util.*

data class LearningHr(
    val day: String?,
    val startTime: String?,
    val endTime: String?
) {

    fun isInLearningHr(): Boolean {
        startTime ?: return false
        endTime ?: return false
        val format = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        try {
            val sdf = SimpleDateFormat(format)

            if (!isSameDay()) return false
            val totalStartHr = getTotalHour(sdf.parse(startTime))
            val totalEndHr = getTotalHour(sdf.parse(endTime))
            val totalCurrentHr = getTotalHour(Date(), true)
            if (totalCurrentHr in totalStartHr..totalEndHr) {
                return true
            }
        } catch (exe: Exception) {
            exe.printStackTrace()
        }
        return false
    }

    private fun isSameDay(): Boolean {
        day ?: return false
        return (DAY_LIST[Calendar.getInstance()
            .get(Calendar.DAY_OF_WEEK) - 1] == day)
    }

    private fun getTotalHour(date: Date, addIst: Boolean = false): Float {
        val calender = Calendar.getInstance()
        calender.time = date
        if (addIst)
            calender.timeZone = TimeZone.getTimeZone("IST")
        val hr = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)
        return hr + (minute / 60F)
    }
}