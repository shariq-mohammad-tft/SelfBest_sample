package com.tft.selfbest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R

class DaysInWeekAdapter(
    private val context: Context,
    private val daysInWeek: ArrayList<String>,
    var dayOfMonth: Int,
    private val totalDayInMonth: Int,
    private val selectedDate: SelectedDate,
) :
    RecyclerView.Adapter<DaysInWeekAdapter.DayViewHolder>() {
    var selectedPosition: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.calender_event_day, parent, false)
        return DayViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = daysInWeek[position]
        holder.dayName.text = day
        if (dayOfMonth + position > totalDayInMonth)
            dayOfMonth %= totalDayInMonth
        holder.dayNumber.text = (dayOfMonth + position).toString()
        if (position == selectedPosition) {
            holder.container.setBackgroundResource(R.drawable.selected_day_bg)
            holder.dayNumber.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.dayName.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.container.setBackgroundResource(R.drawable.day_bg)
            holder.dayNumber.setTextColor(ContextCompat.getColor(context,
                R.color.privacy_bg_colour))
            holder.dayName.setTextColor(ContextCompat.getColor(context, R.color.privacy_bg_colour))
        }
        holder.container.setOnClickListener(View.OnClickListener {
            if (position == selectedPosition)
                return@OnClickListener
            selectedPosition = position
            selectedDate.getSelectedDay(position)
            notifyDataSetChanged()
        })

    }

    override fun getItemCount(): Int {
        return daysInWeek.count()
    }

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayName = view.findViewById(R.id.day_name) as TextView
        val dayNumber = view.findViewById(R.id.day_number) as TextView
        val container = view.findViewById(R.id.container) as ConstraintLayout
    }

    interface SelectedDate {
        fun getSelectedDay(day: Int)
    }
}