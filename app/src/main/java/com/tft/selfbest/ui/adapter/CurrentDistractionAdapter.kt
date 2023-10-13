package com.tft.selfbest.ui.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.data.entity.DistractedApp

class CurrentDistractionAdapter(
    val context: Context,
    val list: List<DistractedApp>?,
    private val deleteDistractionListener: DeleteDistractionListener,
    private val toggleDistractionListener: ToggleDistractionListener
) : RecyclerView.Adapter<CurrentDistractionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentDistractionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_current_distraction, parent, false)
        return CurrentDistractionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrentDistractionViewHolder, position: Int) {
        holder.name.text = list!![position].url
        holder.state.isChecked = list[position].state == true
        holder.delete.setImageResource(R.drawable.icon_delete)
//        if(!holder.state.isChecked) {
//            holder.name.setTextColor(Color.parseColor("#C8C8C8"))
//            holder.delete.setColorFilter(Color.parseColor("#C8C8C8"))
//            holder.container.setBackgroundResource(R.drawable.current_dist_disable_bg)
//        }
        holder.delete.setOnClickListener(View.OnClickListener {
            list[position].state = false
            deleteDistractionListener.deleteDistraction(list[position].id)
        })

        holder.state.setOnClickListener(View.OnClickListener {
            //Log.e("Toggle ", "Distraction 1")
            toggleDistractionListener.toggleDistraction(
                list[position].id,
                !(list[position].state)!!
            )
        })
    }

    override fun getItemCount(): Int {
        return list?.count() ?: 0
    }

    interface DeleteDistractionListener {
        fun deleteDistraction(id: Int)
    }

    interface ToggleDistractionListener {
        fun toggleDistraction(id: Int, state: Boolean?)
    }
}

class CurrentDistractionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val state: CheckBox = view.findViewById(R.id.state)
    val name: TextView = view.findViewById(R.id.cur_distraction_name)
    val delete: ImageView = view.findViewById(R.id.delete_button)
    val container: ConstraintLayout = view.findViewById(R.id.container)
}

