package com.tft.selfbest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.ActivitySingleResponse

class InputProgressAdapter(
    val list: List<ActivitySingleResponse>,
    val categories: List<String>,
    val context: Context
) : RecyclerView.Adapter<InputProgressViewHolder>() {
    val category = listOf("Documentation", "Course", "Code", "Others")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InputProgressViewHolder {
        return InputProgressViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.input_progress, parent, false)
        )
    }

    override fun onBindViewHolder(holder: InputProgressViewHolder, position: Int) {
        val activity = list[position]
        holder.appUsed.text = activity.url
        val time = getTimeInFormat(activity.duration)
        holder.duration.text = "Duration: $time"
        val spinAdapter = ArrayAdapter(
            context,
            R.layout.relevance_spinner_style,
            categories.ifEmpty { category }
        )
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
        holder.category.adapter = spinAdapter
        holder.category.setSelection(category.indexOf("Others"))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getTimeInFormat(time: Double): String {
        val ans: String
        val hours = time.toInt() / 3600
        val remainder = time.toInt() - hours * 3600
        val mins = remainder / 60
        if (hours == 0) {
            ans = "$mins m"
        } else {
            ans = "$hours h $mins m"
        }
        return ans
    }
}

class InputProgressViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val appUsed: TextView = view.findViewById(R.id.app_used)
    val duration: TextView = view.findViewById(R.id.duration)
    val category: Spinner = view.findViewById(R.id.category_spinner)

}