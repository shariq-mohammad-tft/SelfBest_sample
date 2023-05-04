package com.tft.selfbest.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.*
import okhttp3.RequestBody

class DeleteRequestsAdapter(
    val context: Context,
    val list: List<DeleteAccountResponse>,
    val selectall: Boolean,
    private val changeAccountRequestListener: ChangeAccountRequestListener
) : RecyclerView.Adapter<DeleteRequestsAdapter.DeleteRequestViewHolder>(), Filterable {

    var fileteredList = list
    private val selectedItems = mutableListOf<DeleteAccountResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.delete_data_layout_item, parent, false)
        return DeleteRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeleteRequestViewHolder, position: Int) {
        val request = fileteredList[position]
        holder.email.text = request.email
        holder.role.text = request.type
        holder.selected.isChecked = selectall
        holder.acceptButton.setOnClickListener(View.OnClickListener {
            changeAccountRequestListener.changeAccountRequest(
                ChangeAccountRequestBody(
                    "Accept",
                    listOf(AccountRequestBody(request.email, request.type))
                )
            )
        })

        holder.rejectButton.setOnClickListener(View.OnClickListener {
            changeAccountRequestListener.changeAccountRequest(
                ChangeAccountRequestBody(
                    "Reject",
                    listOf(AccountRequestBody(request.email, request.type))
                )
            )
        })

        holder.selected.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
                selectedItems.add(request)
            else
                selectedItems.remove(request)
            changeAccountRequestListener.updateVisibility()
        }
    }

    override fun getItemCount(): Int {
        return fileteredList.size
    }

    interface ChangeAccountRequestListener {
        fun changeAccountRequest(request: ChangeAccountRequestBody)
        fun updateVisibility()
    }

    inner class DeleteRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val email: TextView = view.findViewById(R.id.gmail_id)
        val role: TextView = view.findViewById(R.id.role)
        val acceptButton: TextView = view.findViewById(R.id.accept)
        val rejectButton: TextView = view.findViewById(R.id.reject)
        val selected: CheckBox = view.findViewById(R.id.checkbox1)
    }

    fun getSelectedItems(): List<DeleteAccountResponse> {
        return selectedItems
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                Log.e("Query2", charString)
                fileteredList = if (charString.isEmpty()) list else {
                    val filteredList1 = mutableListOf<DeleteAccountResponse>()
                    list
                        .filter {
                            (it.email.contains(constraint!!, ignoreCase = true))
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
                    results.values as List<DeleteAccountResponse>
                notifyDataSetChanged()
            }
        }
    }
}