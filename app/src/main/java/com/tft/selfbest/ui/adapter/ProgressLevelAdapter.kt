package com.tft.selfbest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R

class ProgressLevelAdapter(levelList: List<String>, currentPosition: Int) :
    RecyclerView.Adapter<ProgressLevelAdapter.LevelViewHolder>() {
    val list = levelList
    val selectedPosition = currentPosition

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.level_items, parent, false)
        return LevelViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val item = list[position]

        if (position == selectedPosition-1) {
            holder.selectedItem.visibility = View.VISIBLE
            holder.item.visibility = View.GONE
            holder.selectedItem.text = item
        } else {
            holder.selectedItem.visibility = View.GONE
            holder.item.visibility = View.VISIBLE
            holder.item.text = item
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    inner class LevelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var selectedItem: TextView = view.findViewById(R.id.selected_level) as TextView
        var item: TextView = view.findViewById(R.id.level) as TextView
    }
}