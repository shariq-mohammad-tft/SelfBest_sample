package com.tft.selfbest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.ActivitySingleResponse
import com.tft.selfbest.models.BargraphListEntry
import com.tft.selfbest.models.QueryAnsweredResponse
import java.text.SimpleDateFormat
import java.util.*

class BarGraphListAdapter(
    val context: Context,
    val list: List<ActivitySingleResponse>,
) : RecyclerView.Adapter<BarGraphListAdapter.BGListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BGListViewHolder {
        return BGListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.daily_recent, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BGListViewHolder, position: Int) {
        val entry = list[position]
        holder.title.text = entry.url
        holder.category.text = entry.category
        holder.time.text = getTimeInFormat(entry.duration)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class BGListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val category: TextView = view.findViewById(R.id.category)
        val time: TextView = view.findViewById(R.id.duration)
    }

    private fun getTimeInFormat(time: Double): String {
        val ans: String
        val mins = (time.toInt()) / 60
        ans = "$mins mins "
        return ans
    }

}

