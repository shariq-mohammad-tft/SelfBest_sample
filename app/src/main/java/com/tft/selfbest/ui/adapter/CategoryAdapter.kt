package com.tft.selfbest.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.SelectedCategory
import com.tft.selfbest.models.mycourse.AddCourse
import java.text.DecimalFormat
import java.util.*

class CategoryAdapter(
    val context: Context,
    val list: List<SelectedCategory>,
    private val listener: PieChartListener
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private val colors = arrayListOf(
        Color.parseColor("#7FC7BC"),
        Color.parseColor("#41AC9C"),
        Color.parseColor("#007BB6"),
        Color.parseColor("#0059C1"),
        Color.parseColor("#AA95DB"),
        Color.parseColor("#FFE0CF"),
        Color.parseColor("#FFC43D"),
        Color.parseColor("#00A2A4")
    )
    private var currentlyOpenItem: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.category_single_item_activity_log, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val category = list[position]
        val drawable = holder.circleImage.drawable
        drawable.colorFilter = PorterDuffColorFilter(colors[position], PorterDuff.Mode.SRC_ATOP)
        holder.circleImage.setImageDrawable(drawable)
        holder.categoryName.text = category.category
        holder.duration.text = getTimeInFormat(category.duration)
        if (category.insights != null) {
            holder.noInsights.visibility = View.GONE
            holder.insightSection.visibility = View.VISIBLE
            holder.insightsHeading.text = category.insights[0].heading
            val childMembersAdapter = InsightsChildAdapter(category.insights[0].insight!!)
            holder.insights.layoutManager = LinearLayoutManager(context)
            holder.insights.adapter = childMembersAdapter
        } else {
            holder.noInsights.visibility = View.VISIBLE
            holder.insightSection.visibility = View.GONE
        }

        holder.clickableSlide.setOnClickListener {
            if (currentlyOpenItem == position) {
                closeInsightSection(currentlyOpenItem)
            } else {
                if (currentlyOpenItem != -1) {
                    closeInsightSection(currentlyOpenItem)
                }

                openInsightSection(position)
            }
        }
        if (position == currentlyOpenItem) {
            holder.insightSection.visibility = View.VISIBLE
        } else {
            holder.insightSection.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val state: CheckBox = view.findViewById(R.id.state)
        val circleImage: ImageView = view.findViewById(R.id.circle)
        val categoryName: TextView = view.findViewById(R.id.category)
        val duration: TextView = view.findViewById(R.id.duration)
        val insightSection: LinearLayout = view.findViewById(R.id.insight_section)
        val clickableSlide: LinearLayout = view.findViewById(R.id.clickable_slide)
        val insightsHeading: TextView = view.findViewById(R.id.heading)
        val insights: RecyclerView = view.findViewById(R.id.insights_list)
        val noInsights: TextView = view.findViewById(R.id.no_insight)
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

    fun getItemPosition(category: String?): Int {
        for (i in list.indices) {
            if (list[i].category.equals(category)) {
                return i
            }
        }
        return -1
    }

    fun openInsightSection(position: Int) {
        currentlyOpenItem = position
        listener.highlight(position+3, list[position].category, list[position].duration)
        notifyDataSetChanged()
    }

    // Method to close the insight section of a specific item
    fun closeInsightSection(position: Int) {
        currentlyOpenItem = -1
        listener.unhighlight()
        notifyDataSetChanged()
    }

    interface PieChartListener {
        fun highlight(position: Int, label: String, duration: Double)
        fun unhighlight()
    }

}