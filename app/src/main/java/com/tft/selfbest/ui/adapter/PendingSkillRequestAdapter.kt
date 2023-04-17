package com.tft.selfbest.ui.adapter

import android.content.Context
import android.graphics.text.TextRunShaper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.*

class PendingSkillRequestAdapter(
    val context: Context,
    val list: List<SkillResponse>,
    private val suggestedSkillList: List<String>,
    private val changeSkillRequestListener: ChangeSkillRequestListener)
    : RecyclerView.Adapter<PendingSkillRequestAdapter.SKillRequestViewHolder>(), Filterable {

    var fileteredList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SKillRequestViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pending_skill_layout_item, parent, false)
        return SKillRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: SKillRequestViewHolder, position: Int) {
        val request = fileteredList[position]
        holder.requestId.text = request.email
        holder.skill.text = request.skill
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            suggestedSkillList
        )
        holder.replaceSkill.setAdapter(adapter)
        holder.replaceSkill.threshold = 0

        holder.replaceSkill.setOnClickListener(View.OnClickListener {
            holder.replaceSkill.requestFocus()
        })

        holder.rejectButton.setOnClickListener(View.OnClickListener {
            changeSkillRequestListener.changeSkillRequest(
                ChangeSkillRequestStatus(
                    "",
                    listOf(request.id),
                    "Rejected"
                )
            )
            notifyItemRemoved(position)
        })
        holder.acceptButton.setOnClickListener(View.OnClickListener {
            changeSkillRequestListener.changeSkillRequest(
                ChangeSkillRequestStatus(
                    holder.replaceSkill.text.toString(),
                    listOf(request.id),
                    "Accepted"
                )
            )
            notifyItemRemoved(position)
        })
    }

    override fun getItemCount(): Int {
        return fileteredList.size
    }

    interface ChangeSkillRequestListener {
        fun changeSkillRequest(request: ChangeSkillRequestStatus)
    }


    inner class SKillRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val skill: TextView = view.findViewById(R.id.skill)
        val requestId: TextView = view.findViewById(R.id.request_id)
        val replaceSkill: AutoCompleteTextView = view.findViewById(R.id.replace_skill)
        val acceptButton: TextView = view.findViewById(R.id.accept)
        val rejectButton: TextView = view.findViewById(R.id.reject)
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
                            (it.skill.contains(constraint!!)|| it.email.contains(constraint))
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
