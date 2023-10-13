package com.tft.selfbest.ui.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.calendarEvents.EventDetail
import java.text.SimpleDateFormat
import java.util.*


class UpcomingEventsAdapter(val list: List<EventDetail>) :
    RecyclerView.Adapter<UpcomingEventsAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.upcoming_event_row, parent, false)
        return EventViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val eventDetail = list[position]
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("hh aa", Locale.US)
        val strDate =  mdformat.format(calendar.time).substring(0, 2)
        holder.time.text = eventDetail.startTime
        val strDate2 = eventDetail.startTime.substring(0, 2)
        Log.e(strDate, strDate2)
        if(strDate == strDate2){
            holder.eventContainer.setBackgroundResource(R.drawable.event_detail_bg)
            holder.eventTitle.setTextColor(Color.parseColor("#FFFFFF"))
            holder.eventTimeInterval.setTextColor(Color.parseColor("#FFFFFF"))
        }
        holder.eventTitle.text = eventDetail.eventTitle
        holder.eventTimeInterval.text = "${eventDetail.startTime} to ${eventDetail.endTime}"
        holder.eventContainer.visibility = if (eventDetail.isShowEvent) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time = view.findViewById(R.id.event_time) as TextView
        val eventTitle = view.findViewById(R.id.event_title) as TextView
        val eventTimeInterval = view.findViewById(R.id.event_time_interval) as TextView
        val eventContainer = view.findViewById(R.id.event_container) as LinearLayout
    }
}