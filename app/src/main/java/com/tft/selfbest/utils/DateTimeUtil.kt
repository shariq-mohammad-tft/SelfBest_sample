package com.tft.selfbest.utils

import com.tft.selfbest.models.calendarEvents.EventDetail
import com.tft.selfbest.models.calendarEvents.RecursiveDays
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {
    companion object {
        var cal: Calendar = Calendar.getInstance()
        var dayofyear: Int = cal.get(Calendar.DAY_OF_YEAR)
        var year: Int = cal.get(Calendar.YEAR)
        var dayofweek: Int = cal.get(Calendar.DAY_OF_WEEK)
        var dayofmonth: Int = cal.get(Calendar.DAY_OF_MONTH)
        val totalDay: Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        private val dayNameInWeek = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
        val eventCategories = listOf("Reminder", "Available Help Hours", "Get Go Hours")

        fun getWeekDays(): ArrayList<String> {
            val weekDays: ArrayList<String> = ArrayList()
            for (dayName in dayNameInWeek) {
                if (dayofweek > 7)
                    dayofweek %= 7
                weekDays.add(dayNameInWeek[dayofweek - 1])
                dayofweek++
            }
            return weekDays
        }

        fun getEventsTime(
            allEvents: ArrayList<EventDetail>?,
            allEventsTime: ArrayList<EventDetail>,
        ): ArrayList<EventDetail> {
            if (allEvents == null) {
                return allEventsTime
            }
            allEvents.forEach { event ->
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val timeFormat = SimpleDateFormat("hh:mmaa")
                val startTime = dateFormat.parse(event.startTime)
                val endTime = dateFormat.parse(event.endTime)
                val sHMInAmPm = timeFormat.format(startTime).toLowerCase()
                val eHMInAmPm = timeFormat.format(endTime).toLowerCase()
                var isUpdatedEvent = false
                allEventsTime.forEach { eventDetail ->
                    if (eventDetail.startTime == sHMInAmPm) {
                        eventDetail.startTime = sHMInAmPm
                        eventDetail.endTime = eHMInAmPm
                        eventDetail.eventTitle = event?.eventTitle
                        eventDetail.isShowEvent = true
                        isUpdatedEvent = true
                        return@forEach
                    }
                }
                if (!isUpdatedEvent) {
                    val hourIndex = SimpleDateFormat("HH").format(startTime).toInt()
                    allEventsTime.add(
                        hourIndex,
                        EventDetail(sHMInAmPm, eHMInAmPm, event.eventTitle)
                    )
                }
            }
            return allEventsTime
        }

        fun get24HourList(): ArrayList<EventDetail> {
            val allEventsTime = ArrayList<EventDetail>()
            val listOfTimeInterval = listOf(
                "01:00am",
                "02:00am",
                "03:00am",
                "04:00am",
                "05:00am",
                "06:00am",
                "07:00am",
                "08:00am",
                "09:00am",
                "10:00am",
                "11:00am",
                "12:00pm",
                "01:00pm",
                "02:00pm",
                "03:00pm",
                "04:00pm",
                "05:00pm",
                "06:00pm",
                "07:00pm",
                "08:00pm",
                "09:00pm",
                "10:00pm",
                "11:00pm",
                "12:00am"
            )
            for (interval in listOfTimeInterval) {
                val eventDetail = EventDetail(interval, "", "", false)
                allEventsTime.add(eventDetail)
            }
            return allEventsTime
        }

        fun getDefaultRecursiveDays(): ArrayList<RecursiveDays> {
            val recursiveDays = ArrayList<RecursiveDays>()
            for (day in dayNameInWeek) {
                recursiveDays.add(RecursiveDays(day.toUpperCase(), day.substring(0, 1)))
            }
            recursiveDays[0].isSelected = false
            recursiveDays[6].isSelected = false
            return recursiveDays
        }
    }
}