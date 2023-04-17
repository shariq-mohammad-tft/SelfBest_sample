package com.tft.selfbest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.QueryAnsweredResponse
import java.text.SimpleDateFormat
import java.util.*

class AnsweredQueryAdapter(
    val context: Context,
    val list: List<QueryAnsweredResponse>,
    private val changeStatusListener: ChangeStatusListener
) : RecyclerView.Adapter<AnsweredQueryAdapter.AnsweredQueryViewHolder>() {
    //val status = listOf("Done", "In Progress")
    val relevance = listOf("Easy", "Medium", "High")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnsweredQueryViewHolder {
        return AnsweredQueryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.answered_query_entry, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AnsweredQueryViewHolder, position: Int) {
        var isExpanded = false
        var check = false
        var checkr = false
        val query = list[position]
        holder.queryNumber.text = "Query " + ((position+1).toString())
        holder.queryHeading.text = query.subject
        holder.descriptiveText.text = query.question
        //Log.e("TimeStamp", query.timestamp)
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date = df.parse(query.timestamp)
        val formatter =
            SimpleDateFormat("dd-MM-yyyy '|' hh:mm a", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        val dateStr = formatter.format(date!!)
        holder.time.text = dateStr
//        holder.spinner.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {}
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    pos: Int,
//                    id: Long
//                ) {
//                    val selectedItem = holder.spinner.selectedItem as String
//                    if(check) {
//                        val status = if (selectedItem == "Done") 1 else 0
//                        changeStatusListener.changeStatus(query.id, status)
//                    }
//                    check = true
//                }
//            }
//
//        val spinAdapter = ArrayAdapter(
//            context,
//            R.layout.query_spinner,
//            status
//        )
//        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
//        holder.spinner.adapter = spinAdapter
//        val stat = if (query.query_status) 0 else 1
//        holder.spinner.setSelection(stat)

        holder.rSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val selectedItem = holder.rSpinner.selectedItem as String
                    if(checkr) {
                        changeStatusListener.changeRelevance(query.id, pos+1)
                    }
                    checkr = true
                }
            }

        val spinAdapter2 = ArrayAdapter(
            context,
            R.layout.relevance_spinner_style,
            relevance
        )
        spinAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_style)
        holder.rSpinner.adapter = spinAdapter2
        if(query.relevance == "") {
            holder.rSpinner.setSelection(1)
        }else{
            holder.rSpinner.setSelection(relevance.indexOf(query.relevance))
        }

        holder.description.setOnClickListener(View.OnClickListener {
            isExpanded = !isExpanded
            if(isExpanded){
                holder.descriptiveText.visibility = View.VISIBLE
                holder.description.setImageResource(R.drawable.drop_up_query)
            }else{
                holder.descriptiveText.visibility = View.GONE
                holder.description.setImageResource(R.drawable.dropdown_query)
            }
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ChangeStatusListener {
        fun changeRelevance(id: Int, relevance: Int)
    }

    inner class AnsweredQueryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val queryNumber: TextView = view.findViewById(R.id.query_number)
        val queryHeading: TextView = view.findViewById(R.id.query_skill)
        val time: TextView = view.findViewById(R.id.time_stamp)
        //val spinner: Spinner = view.findViewById(R.id.spinner)
        val rSpinner : Spinner = view.findViewById(R.id.relevance_spinner)
        val description: ImageView = view.findViewById(R.id.description)
        val descriptiveText: TextView = view.findViewById(R.id.descriptive)
    }

//    @Override
//    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
//        val view: View = super.getView(position, convertView, parent)
//        view.setPadding(0, view.paddingTop, view.paddingRight, view.paddingBottom)
//        return view
//    }
}

