package com.tft.selfbest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.ActivityTimelineResponse
import java.text.SimpleDateFormat
import java.util.*

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
        if(res.points >= 0) {
            holder.distraction_container.visibility = View.GONE
            holder.neg_points.visibility = View.GONE
            holder.focused_container.visibility = View.VISIBLE
            holder.pos_points.visibility = View.VISIBLE
            val ss = "+ " + res.points.toString() + " Pts"
            holder.pos_points.text = ss

            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(res.createdAt)
            val formatter =
                SimpleDateFormat("hh:mm a", Locale.getDefault())
            formatter.timeZone = TimeZone.getDefault()
            val dateStr = formatter.format(date!!)
            holder.pos_time.text = dateStr
            holder.pos_description.text = res.description
        }else{
            holder.distraction_container.visibility = View.VISIBLE
            holder.neg_points.visibility = View.VISIBLE
            holder.focused_container.visibility = View.GONE
            holder.pos_points.visibility = View.GONE
            val ss = res.points.toString() + " Pts"
            holder.neg_points.text = ss

            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(res.createdAt)
            val formatter =
                SimpleDateFormat("hh:mm a", Locale.getDefault())
            formatter.timeZone = TimeZone.getDefault()
            val dateStr = formatter.format(date!!)
            holder.neg_time.text = dateStr
            holder.neg_description.text = res.description
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val pos_points = view.findViewById(R.id.points) as TextView
        val pos_description = view.findViewById(R.id.description) as TextView
        val pos_time = view.findViewById(R.id.time_created) as TextView
        val focused_container = view.findViewById(R.id.focused) as LinearLayout

        val neg_points = view.findViewById(R.id.neg_points) as TextView
        val neg_description = view.findViewById(R.id.neg_description) as TextView
        val neg_time = view.findViewById(R.id.neg_time_created) as TextView
        val distraction_container = view.findViewById(R.id.distracted) as LinearLayout
    }

    private fun getTimeInFormat(time: Double): String {
        val ans: String
        val mins = (time.toInt()) / 60
        ans = "$mins mins "
        return ans
    }
}