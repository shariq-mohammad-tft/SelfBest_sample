package com.tft.selfbest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.calendarEvents.EventDetail
import java.text.SimpleDateFormat

class MyCalendarEventsAdapter(val list: ArrayList<EventDetail>) :
    RecyclerView.Adapter<MyCalendarEventsAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.my_calendar_event_row, parent, false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val eventDetail = list[position]
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val timeFormat = SimpleDateFormat("hh:mmaa")
        val startTime = dateFormat.parse(eventDetail.startTime)
        val endTime = dateFormat.parse(eventDetail.endTime)
        val sHMInAmPm = timeFormat.format(startTime).toLowerCase()
        val eHMInAmPm = timeFormat.format(endTime).toLowerCase()
        holder.eventTitle.text = eventDetail.eventTitle
        holder.eventTimeInterval.text = "${sHMInAmPm.toString()} to ${eHMInAmPm.toString()}"
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventTitle: TextView = view.findViewById(R.id.event_title)
        val eventTimeInterval: TextView = view.findViewById(R.id.event_time_interval)
    }
}