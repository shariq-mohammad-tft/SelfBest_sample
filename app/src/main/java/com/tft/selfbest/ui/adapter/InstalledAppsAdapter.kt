package com.tft.selfbest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R

class InstalledAppsAdapter(itemList: List<String>) :
    RecyclerView.Adapter<InstalledAppsAdapter.CourseItemViewHolder>() {
    private val list = itemList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.installed_suggested_app, parent, false)
        return CourseItemViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: CourseItemViewHolder, position: Int) {
        val item = list[position]
        holder.item.text = item
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    inner class CourseItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var item: TextView = view.findViewById(R.id.app_name) as TextView
    }
}