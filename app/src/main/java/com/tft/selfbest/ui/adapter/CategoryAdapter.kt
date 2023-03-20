package com.tft.selfbest.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.SelectedCategory
import java.text.DecimalFormat
import java.util.*

class CategoryAdapter(
    val context: Context,
    val list : List<SelectedCategory>):
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_single_item_activity_log, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = list[position]
        //holder.state.isChecked = true
        val rnd = Random()
        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        holder.circleImage.setColorFilter(color)
        holder.categoryName.text = category.category
        holder.duration.text = getTimeInFormat(category.duration)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val state: CheckBox = view.findViewById(R.id.state)
        val circleImage: ImageView= view.findViewById(R.id.circle)
        val categoryName: TextView = view.findViewById(R.id.category)
        val duration: TextView = view.findViewById(R.id.duration)
    }

    private fun getTimeInFormat(time: Double): String {
        val ans: String
        val hours = time.toInt() / 3600
        val remainder = time.toInt() - hours * 3600
        val mins = remainder / 60
        val formatter = DecimalFormat("00");
        ans = "${formatter.format(hours)}h : ${formatter.format(mins)}m"
        return ans
    }

}