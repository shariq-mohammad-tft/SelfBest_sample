package com.tft.selfbest.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.QueryResponse
import java.text.SimpleDateFormat
import java.util.*


class QueryResponseAdapter(
    val context: Context,
    val list: List<QueryResponse>,
    private val changeStatusListener: ChangeStatusListener
) : RecyclerView.Adapter<QueryResponseAdapter.QueryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        return QueryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.query_table_entry, parent, false)
        )
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
        var check = false
        var isExpanded = false
        val status = listOf("Done", "In Progress")
        val query = list[position]
        holder.queryNumber.text = "Query " + ((position + 1).toString())
        holder.queryHeading.text = query.subject
        holder.descriptiveText.text = query.question
        //Log.e("TimeStamp", query.timestamp)
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date = df.parse(query.timestamp)
        val formatter =
            SimpleDateFormat("dd-MM-yyyy '|' hh:mm a", Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val dateStr = formatter.format(date!!)
        holder.time.text = dateStr
        val spinAdapter = ArrayAdapter(
            context,
            R.layout.query_spinner,
            status
        )
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
        holder.spinner.adapter = spinAdapter
        val stat = if (query.query_status == true) 0 else 1
        holder.spinner.setSelection(stat)

        if (query.query_status == true) {
            //if done
            holder.viewQueryHeading.visibility = View.GONE
            holder.viewQuery.visibility = View.GONE
            holder.spinner.isEnabled = false
            holder.spinnerContainer.setBackgroundResource(R.drawable.done_query_bg)
            holder.rateExpertHeading.visibility = View.VISIBLE
            holder.rating.visibility = View.VISIBLE
            holder.rating.rating = if(query.rating != "") query.rating.toInt().toFloat() else 0f
        } else {
            //if in progress
            holder.viewQueryHeading.visibility = View.VISIBLE
            holder.viewQuery.visibility = View.VISIBLE
            holder.spinner.isEnabled = true
            holder.rateExpertHeading.visibility = View.GONE
            holder.rating.visibility = View.GONE
            holder.spinnerContainer.setBackgroundResource(R.drawable.asked_query_bg)
            holder.viewQuery.setOnClickListener {
                View.OnClickListener {
                    //TODO
                }
            }
        }

        holder.rating.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                changeStatusListener.updateRating(query.id, rating.toInt())
            }

        holder.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    Log.e("Status ", "1")
                    val selectedItem = holder.spinner.selectedItem as String
                    if (check) {
                        val stat = if (selectedItem == "Done") 1 else 0
                        if(selectedItem == "Done"){
                            holder.viewQueryHeading.visibility = View.GONE
                            holder.viewQuery.visibility = View.GONE
                            holder.spinner.isEnabled = false
                            holder.spinnerContainer.setBackgroundResource(R.drawable.done_query_bg)
                            holder.rateExpertHeading.visibility = View.VISIBLE
                            holder.rating.visibility = View.VISIBLE
                            holder.rating.rating = if(query.rating != "") query.rating.toInt().toFloat() else 0f
                        }
                        changeStatusListener.changeStatus(query.id, stat)
                    }
                    check = true
                }
            }

//        if(stat == 0) {
//            holder.spinnerContainer.setBackgroundResource(R.drawable.asked_query_bg)
//        }
//        else {
//            holder.spinnerContainer.setBackgroundResource(R.drawable.inprogress_query_bg)
//            holder.spinner.(context.resources.getColor(R.color.tool_bar_color))
//        }
        holder.description.setOnClickListener(View.OnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                holder.descriptiveText.visibility = View.VISIBLE
                holder.description.setImageResource(R.drawable.drop_up_query)
            } else {
                holder.descriptiveText.visibility = View.GONE
                holder.description.setImageResource(R.drawable.dropdown_query)
            }
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ChangeStatusListener {
        fun changeStatus(id: Int, status: Int)
        fun updateRating(id: Int, rating: Int)
    }

    inner class QueryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val queryNumber: TextView = view.findViewById(R.id.query_number)
        val queryHeading: TextView = view.findViewById(R.id.query_skill)
        val time: TextView = view.findViewById(R.id.time_stamp)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val description: ImageView = view.findViewById(R.id.description)
        val descriptiveText: TextView = view.findViewById(R.id.descriptive)
        val spinnerContainer: FrameLayout = view.findViewById(R.id.spinner_container)
        val viewQueryHeading: TextView = view.findViewById(R.id.view_query_heading)
        val viewQuery: TextView = view.findViewById(R.id.view_query)
        val rateExpertHeading: TextView = view.findViewById(R.id.rate_expert_heading)
        val rating: RatingBar = view.findViewById(R.id.rating)
    }
}

