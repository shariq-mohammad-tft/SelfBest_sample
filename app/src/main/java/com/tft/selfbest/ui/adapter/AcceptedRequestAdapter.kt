package com.tft.selfbest.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.ChangeRequestStatus
import com.tft.selfbest.models.RequestStatus
import com.tft.selfbest.models.UserRequest

class AcceptedRequestAdapter(
    val context: Context,
    val list: List<UserRequest>,
    private val selectAll: Boolean,
    private val rejectRequestListener: RejectRequestListener
) : RecyclerView.Adapter<AcceptedRequestAdapter.RequestViewHolder>(), Filterable {

    private val roles = listOf("Admin", "User")
    var fileteredList = list
    private val selectedItems = mutableListOf<UserRequest>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.accepted_layout_item, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = fileteredList[position]
        var check = false
        holder.gmail.text = request.email
        holder.linkedin.text = request.linkedInUrl
        if(request.linkedInUrl.isEmpty())
            holder.linkedinContainer.visibility = View.GONE
        else
            holder.linkedinContainer.visibility = View.VISIBLE
        holder.select.isChecked = selectAll
        val spinAdapter = ArrayAdapter(
            context,
            R.layout.spinner_main_item_style,
            roles
        )
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
        holder.role.adapter = spinAdapter
        val stat = if (request.userType.equals("Admin")) 0 else 1
        holder.role.setSelection(stat)
//        if(check) {
//            holder.bind(request)
//        }
//        check = true
        holder.role.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                //holder.role.setSelection(position)
                if (check) {
                    rejectRequestListener.rejectRequest(
                        ChangeRequestStatus(
                            request.userType.equals("Admin"), listOf(
                                RequestStatus(request.status, request.userid, selectedItem)
                            )
                        )
                    )
                    //notifyDataSetChanged()
                }
                check = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        holder.rejectButton.setOnClickListener {
            rejectRequestListener.rejectRequest(
                ChangeRequestStatus(
                    request.userType.equals("Admin"), listOf(
                        RequestStatus("rejected", request.userid, request.userType)
                    )
                )
            )
            notifyItemRemoved(position)
        }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
                selectedItems.add(request)
            else
                selectedItems.remove(request)
            rejectRequestListener.updateVisibility()
        }

    }

    override fun getItemCount(): Int {
        return fileteredList.size
    }

    interface RejectRequestListener {
        fun rejectRequest(request: ChangeRequestStatus)
        fun updateVisibility()
    }

    fun getSelectedItems(): List<UserRequest> {
        return selectedItems
    }


    inner class RequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gmail: TextView = view.findViewById(R.id.gmail_id)
        val role: Spinner = view.findViewById(R.id.spinner)
        val rejectButton: TextView = view.findViewById(R.id.reject)
        val select: CheckBox = view.findViewById(R.id.checkbox1)
        val linkedin: TextView = view.findViewById(R.id.linkedin_id)
        val linkedinContainer: LinearLayout = view.findViewById(R.id.linked_in_container)
        val checkBox: CheckBox = view.findViewById(R.id.checkbox1)

        fun bind(request: UserRequest) {
            role.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    rejectRequestListener.rejectRequest(
                        ChangeRequestStatus(
                            request.userType.equals("Admin"), listOf(
                                RequestStatus(request.status, request.userid, selectedItem)
                            )
                        )
                    )
                    notifyDataSetChanged()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
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
                    results.values as List<UserRequest>
                notifyDataSetChanged()
            }
        }
    }
}
