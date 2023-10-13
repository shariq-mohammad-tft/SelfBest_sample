package com.tft.selfbest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.utils.ColorTemplate
import com.tft.selfbest.R
import com.tft.selfbest.models.ActivityLogValues
import com.tft.selfbest.models.ObservationDetail

class ActivityLogAdapter(val context : Context, val list: List<ActivityLogValues>): RecyclerView.Adapter<ActivityLogAdapter.ActivityLogHolder>() {

    private val colors = ArrayList<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityLogHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_log_single_item, parent, false)
//        for (color in ColorTemplate.MATERIAL_COLORS) {
//            colors.add(color)
//        }
        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }
        return ActivityLogHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ActivityLogHolder, position: Int) {
        val observationDetail = list[position]
        holder.app.text = observationDetail.url
        holder.duration.text = getTimeInFormat(observationDetail.duration)
        holder.bullet.setColorFilter(colors[position])
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    private fun getTimeInFormat(time: Double) : String{
        val ans : String
        val hours = time.toInt() / 3600
        val remainder = time.toInt() - hours * 3600
        val mins = remainder / 60
        if(hours == 0){
            ans = "$mins mins"
        }else{
            ans = "$hours hr $mins mins"
        }
        return ans
    }


    inner class ActivityLogHolder(view: View) : RecyclerView.ViewHolder(view) {
        val app = view.findViewById(R.id.al_name) as TextView
        val duration = view.findViewById(R.id.al_duration) as TextView
        val bullet = view.findViewById(R.id.al_bullet) as ImageView
    }
}