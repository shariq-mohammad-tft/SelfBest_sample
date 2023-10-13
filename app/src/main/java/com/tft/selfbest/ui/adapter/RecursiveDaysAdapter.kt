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
import com.tft.selfbest.models.calendarEvents.RecursiveDays

class RecursiveDaysAdapter(val context: Context, val list: ArrayList<RecursiveDays>) :
    RecyclerView.Adapter<RecursiveDaysAdapter.RepeatDayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepeatDayViewHolder {
        val viewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.repeates_on_day, parent, false)
        return RepeatDayViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: RepeatDayViewHolder, position: Int) {
        val recursiveDay = list[position]
        holder.dayForSelection.text = recursiveDay.day.substring(0,1) //recursiveDay.recursiveDate
        holder.repeatDayContainer.setBackgroundResource(if (recursiveDay.isSelected) R.drawable.selected_repeate_on_day_bg else R.drawable.repeate_on_day_bg)
        holder.dayForSelection.setTextColor(
            ContextCompat.getColor(
                context,
                if (recursiveDay.isSelected) R.color.white else R.color.privacy_bg_colour
            )
        )
        holder.repeatDayContainer.setOnClickListener(View.OnClickListener {
            list[position].isSelected = !list[position].isSelected
            holder.repeatDayContainer.setBackgroundResource(
                if (list[position].isSelected) R.drawable.selected_repeate_on_day_bg else
                    R.drawable.repeate_on_day_bg
            )
            holder.dayForSelection.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (list[position].isSelected) R.color.white else R.color.privacy_bg_colour
                )
            )
        })
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    inner class RepeatDayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dayForSelection: TextView = view.findViewById(R.id.repeat_day)
        var repeatDayContainer: ConstraintLayout = view.findViewById(R.id.repeat_day_container)
    }

}