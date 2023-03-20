package com.tft.selfbest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.NotificationDetail
import com.tft.selfbest.models.mycourse.EnrolledCourse

class NotificationAdapter(val list: List<NotificationDetail>,
                          val context: Context) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val viewHolder =
            LayoutInflater.from(context).inflate(R.layout.notification_card_row, parent, false)
        return NotificationViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val number = position
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}