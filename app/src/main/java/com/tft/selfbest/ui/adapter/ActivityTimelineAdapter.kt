package com.tft.selfbest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.ActivityTimelineResponse

class ActivityTimelineAdapter(
    val list: List<ActivityTimelineResponse>
) : RecyclerView.Adapter<ActivityTimelineAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_timeline_row, parent, false)
        return ActivityViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val res = list[position]
        val ss = "00" + res.points.toString()
        holder.points.text = ss
//        when(res.focusTime){
//            is Duration.IntValue -> {
//                holder.focus.text = getTimeInFormat(res.focusTime.value.toDouble())
//            }
//            is Duration.DoubleValue -> {
//                holder.focus.text = getTimeInFormat(res.focusTime.value)
//            }
//        }
//        when(res.distractionTime){
//            is Duration.IntValue -> {
//                holder.distraction.text = getTimeInFormat(res.distractionTime.value.toDouble())
//            }
//            is Duration.DoubleValue -> {
//                holder.distraction.text = getTimeInFormat(res.distractionTime.value)
//            }
//        }
        holder.focus.text = getTimeInFormat(res.focusTime)
        holder.distraction.text = getTimeInFormat(res.distractionTime)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val points = view.findViewById(R.id.recent_dayText) as TextView
        val focus = view.findViewById(R.id.focused_time) as TextView
        val distraction = view.findViewById(R.id.distracted_time) as TextView
    }

    private fun getTimeInFormat(time: Double): String {
        val ans: String
        val mins = (time.toInt()) / 60
        ans = "$mins mins "
        return ans
    }
}