package com.tft.selfbest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.calendarEvents.EventDetail
import org.w3c.dom.Text

class RecentEventAdapter(val list: List<EventDetail>,):
    RecyclerView.Adapter<RecentEventAdapter.EventViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentEventAdapter.EventViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context).inflate(R.layout.recent_event_row,parent,false)
        return EventViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: RecentEventAdapter.EventViewHolder, position: Int) {
        val eventDetail=list[position]
        holder.eventTitle.text = eventDetail.eventTitle
        holder.eventTimeInterval.text = "${eventDetail.startTime} to ${eventDetail.endTime}"
        holder.recentTime.text="Today "
    }

    override fun getItemCount(): Int {
        return list.count()
    }



    inner class EventViewHolder(view: View) :RecyclerView.ViewHolder(view) {


        // val dayText=view.findViewById(R.id.recent_dayText) as TextView
        // val dateText= view.findViewById(R.id.dateText) as TextView
        val eventTitle = view.findViewById(R.id.event_title) as TextView
        val eventTimeInterval = view.findViewById(R.id.event_time_interval) as TextView
        val eventContainer = view.findViewById(R.id.event_container) as LinearLayout
        val recentTime= view.findViewById(R.id.recent_dayText) as TextView
    }
}