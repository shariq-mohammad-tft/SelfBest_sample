package com.tft.selfbest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R

open class InsightsChildAdapter(var memberData: List<String>) :
    RecyclerView.Adapter<InsightsChildAdapter.DataViewHolder>() {

    private var membersList: List<String> = ArrayList()

    init {
        this.membersList = memberData
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val insight: TextView = itemView.findViewById(R.id.single_insight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.insight_single_item, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.insight.text = membersList[position]
    }

    override fun getItemCount(): Int = membersList.size


}