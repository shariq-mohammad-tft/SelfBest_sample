package com.tft.selfbest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.UserRequest

class UserManagementSkillAdapter (val context: Context, val list: List<UserRequest>) : RecyclerView.Adapter<UserManagementSkillAdapter.SkillRequestViewHolder>() {

    val roles = listOf("Admin", "User")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillRequestViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pending_layout_item, parent, false)
        return SkillRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillRequestViewHolder, position: Int) {
        val request = list[position]
        holder.gmail.text = request.email
        holder.linkedin.text = request.email
        val spinAdapter = ArrayAdapter(
            context,
            R.layout.spinner_main_item_style,
            roles
        )
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
        holder.role.adapter = spinAdapter
        val stat = if (request.status.equals("Admin")) 0 else 1
        holder.role.setSelection(stat)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class SkillRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gmail: TextView = view.findViewById(R.id.gmail_id)
        val linkedin: TextView = view.findViewById(R.id.linkedin_id)
        val role: Spinner = view.findViewById(R.id.spinner)
    }
}
