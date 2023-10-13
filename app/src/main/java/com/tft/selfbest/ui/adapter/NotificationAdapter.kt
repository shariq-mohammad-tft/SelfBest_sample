package com.tft.selfbest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.NotificationDetail
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(val list: List<NotificationDetail>,
                          val context: Context) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val viewHolder =
            LayoutInflater.from(context).inflate(R.layout.notification_card_row, parent, false)
        return NotificationViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = list[position]
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date = df.parse(item.time)
        val formatter =
            SimpleDateFormat("dd-MM-yyyy '|' hh:mm a", Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val dateStr = formatter.format(date!!)
        holder.title.text = dateStr
        holder.content.text = item.content
    }

//    private fun getTitle(type: String, time: String): String? {
//        val date =
//            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault()).parse(time)
//        return date?.let { SimpleDateFormat("dd-MM-yyyy' | 'HH:mm", Locale.getDefault()).format(it) }
//    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.notification_header)
        val content = view.findViewById<TextView>(R.id.notification_detail)
    }
}