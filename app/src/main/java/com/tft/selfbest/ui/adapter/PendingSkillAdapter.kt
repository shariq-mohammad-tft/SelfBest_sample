package com.tft.selfbest.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap
import com.tft.selfbest.R

class PendingSkillAdapter(val context: Context, val hashMap: LinkedTreeMap<String, Int>) :
    RecyclerView.Adapter<PendingSkillAdapter.PendingSkillViewHolder>() {
    var list = ArrayList(hashMap.entries)
    val skillLevel = listOf("Beginner", "Intermediate", "Expert")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingSkillViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pending_profile_skills_row, parent, false)
        return PendingSkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendingSkillViewHolder, position: Int) {
        Log.e("Skill", "Pending Skill Adapter")
        val mapObject = list[position]
        holder.skillName.setText(mapObject.key)
        holder.skillLevel.rating = mapObject.value.toFloat()
        holder.skillLevel.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, "This skill is pending", Toast.LENGTH_SHORT).show()
        })

        holder.initial.text = mapObject.key.toString().substring(0, 1)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class PendingSkillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val initial: TextView = view.findViewById(R.id.initial)
        val skillName: TextView = view.findViewById(R.id.skill)
        var skillLevel: RatingBar = view.findViewById(R.id.level)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSkill(skillName: String, skillLevel: Int) {
        hashMap[skillName] = skillLevel
        list = ArrayList(hashMap.entries)
        this.notifyDataSetChanged()
    }

}