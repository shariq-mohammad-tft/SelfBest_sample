package com.tft.selfbest.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.ChangeSkillRequestStatus
import com.tft.selfbest.models.SkillResponse

class AcceptedSkillRequestAdapter(
    val context: Context,
    val list: MutableList<SkillResponse>,
    private val selectAll: Boolean,
    private val changeSkillRequestListener: ChangeSkillRequestListener
) : RecyclerView.Adapter<AcceptedSkillRequestAdapter.SKillRequestViewHolder>(), Filterable {

    var fileteredList = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SKillRequestViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.accepted_skill_layout_item, parent, false)
        return SKillRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: SKillRequestViewHolder, position: Int) {
        val request = fileteredList[position]
        holder.id.text = request.email
        holder.skill.text = request.skill
        holder.select.isChecked = selectAll
        holder.rejectButton.setOnClickListener {
            changeSkillRequestListener.changeSkillRequest(
                ChangeSkillRequestStatus(
                    "",
                    listOf(request.id),
                    "Rejected"
                )
            )
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return fileteredList.size
    }

    interface ChangeSkillRequestListener {
        fun changeSkillRequest(request: ChangeSkillRequestStatus)
    }

    inner class SKillRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.request_id)
        val skill: TextView = view.findViewById(R.id.skill_name)
        val rejectButton: TextView = view.findViewById(R.id.reject)
        val select: CheckBox = view.findViewById(R.id.checkbox1)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                Log.e("Query2", charString)
                fileteredList = if (charString.isEmpty()) list else {
                    val filteredList1 = mutableListOf<SkillResponse>()
                    list
                        .filter {
                            (it.email.contains(constraint!!, ignoreCase = true) || it.skill.contains(constraint, ignoreCase = true))
                        }
                        .forEach { filteredList1.add(it) }
                    filteredList1

                }
                return FilterResults().apply { values = fileteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                fileteredList = if (results?.values == null)
                    ArrayList()
                else
                    results.values as MutableList<SkillResponse>
                notifyDataSetChanged()
            }
        }
    }
}
