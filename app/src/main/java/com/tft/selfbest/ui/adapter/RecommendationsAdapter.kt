package com.tft.selfbest.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import java.util.*

class RecommendationsAdapter(val context: Context,
                             val list: List<String>,
                             val skillAddedListener: SkillAddedListener) : RecyclerView.Adapter<RecommendationsAdapter.RSkillViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RSkillViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recommended_skill, parent, false)
        return RSkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: RSkillViewHolder, position: Int) {
        val skill = list[position]
        holder.skillName.text = skill
        holder.skillContainer.setOnClickListener(View.OnClickListener {
            skillAddedListener.skillAdded(skill)
        })
    }

    interface SkillAddedListener{
        fun skillAdded(skill: String)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class RSkillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var skillName : TextView = view.findViewById(R.id.r_skill_text)
        val skillContainer : ConstraintLayout = view.findViewById(R.id.r_skill)
    }

}