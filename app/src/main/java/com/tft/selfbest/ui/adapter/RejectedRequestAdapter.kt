package com.tft.selfbest.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.ChangeRequestStatus
import com.tft.selfbest.models.RequestStatus
import com.tft.selfbest.models.UserRequest

class RejectedRequestAdapter(
    val context: Context, val list: List<UserRequest>, val selectAll: Boolean,
    private val acceptRequestListener: RejectedRequestAdapter.AcceptRequestListener
) : RecyclerView.Adapter<RejectedRequestAdapter.RequestViewHolder>(), Filterable {

    val roles = listOf("Admin", "User")
    var fileteredList = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rejected_layout_item, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = fileteredList[position]
        var check = false
        holder.gmail.text = request.email
        holder.select.isChecked = selectAll
        val spinAdapter = ArrayAdapter(
            context,
            R.layout.spinner_main_item_style,
            roles
        )
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
        holder.role.adapter = spinAdapter
        val stat = if (request.status.equals("Admin")) 0 else 1
        holder.role.setSelection(stat, false)
        holder.role.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if(check) {
                    acceptRequestListener.acceptRequest(
                        ChangeRequestStatus(
                            request.userType.equals("Admin"), listOf(
                                RequestStatus(request.status, request.userid, selectedItem)
                            )
                        )
                    )
                }
                check = true
                //notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        holder.acceptButton.setOnClickListener(View.OnClickListener {
            acceptRequestListener.acceptRequest(
                ChangeRequestStatus(
                    request.userType.equals("Admin"), listOf(
                        RequestStatus("accepted", request.userid, request.userType)
                    )
                )
            )
            notifyItemRemoved(position)
        })
    }

    override fun getItemCount(): Int {
        return fileteredList.size
    }

    interface AcceptRequestListener {
        fun acceptRequest(request: ChangeRequestStatus)
    }

    inner class RequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gmail: TextView = view.findViewById(R.id.gmail_id)
        val role: Spinner = view.findViewById(R.id.spinner)
        val acceptButton: TextView = view.findViewById(R.id.accept)
        val select: CheckBox = view.findViewById(R.id.checkbox1)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                Log.e("Query2", charString)
                fileteredList = if (charString.isEmpty()) list else {
                    val filteredList1 = mutableListOf<UserRequest>()
                    list
                        .filter {
                            (it.email.contains(constraint!!))
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
                    results.values as List<UserRequest>
                notifyDataSetChanged()
            }
        }
    }
}
