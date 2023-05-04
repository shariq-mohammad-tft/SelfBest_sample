package com.tft.selfbest.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap
import com.tft.selfbest.R
import java.util.*
import kotlin.collections.ArrayList

class ProfileSkillsAdapter(val context: Context, val hashMap: LinkedTreeMap<String, Int>, private val changeRatingListener: ProfileSkillsAdapter.ChangeRatingListener) :
    RecyclerView.Adapter<ProfileSkillsAdapter.SkillViewHolder>() {
    var list = ArrayList(hashMap.entries)
    val skillLevel = listOf("Beginner", "Intermediate", "Expert")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.profile_skills_row, parent, false)
        return SkillViewHolder(view)
    }

   /* override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val mapObject = list[position]
        holder.skillName.setText(mapObject.key)
        holder.skillLevel.rating = mapObject.value.toFloat()
        holder.deleteIcon.setOnClickListener(View.OnClickListener {
            val adapterPosition = holder.absoluteAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val removedMapObject = list.removeAt(adapterPosition)
                hashMap.remove(removedMapObject.key)
                this.notifyItemRemoved(adapterPosition)
            }
        })

        holder.initial.text = mapObject.key.toString().substring(0, 1)

        holder.skillLevel.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, _, _ ->
                changeRatingListener.changeRating(
                    holder.skillLevel.rating,
                    holder.skillName.text.toString()
                )
                notifyDataSetChanged()
            }
    }*/

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        if (list.isNotEmpty() && position < list.size) {
            val mapObject = list[position]
            if (mapObject != null) {
                holder.skillName.text = mapObject.key
                holder.skillLevel.rating = mapObject.value.toFloat()
                holder.deleteIcon.setOnClickListener {
                    val adapterPosition = holder.absoluteAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        val removedMapObject = list.getOrNull(adapterPosition)
                        if (removedMapObject != null) {
                            list.removeAt(adapterPosition)
                            hashMap.remove(removedMapObject.key)
                            this.notifyItemRemoved(adapterPosition)
                        }
                    }
                }

                if (mapObject.key != null && mapObject.key.isNotEmpty()) {
                    holder.initial.text = mapObject.key.substring(0, 1)
                } else {
                    holder.initial.text = ""
                }

                holder.skillLevel.onRatingBarChangeListener =
                    RatingBar.OnRatingBarChangeListener { _, _, _ ->
                        val skillName = holder.skillName.text?.toString()
                        if (skillName != null) {
                            changeRatingListener.changeRating(
                                holder.skillLevel.rating,
                                skillName
                            )
                            notifyDataSetChanged()
                        }
                    }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface ChangeRatingListener : RatingBar.OnRatingBarChangeListener {
        fun changeRating(level: Float, skill: String)
    }

    inner class SkillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val initial = view.findViewById<TextView>(R.id.initial)
        //val skillName = view.findViewById<EditText>(R.id.skill)
        val skillName = view.findViewById<TextView>(R.id.skill)
        //var skillLevel = view.findViewById<Spinner>(R.id.skill_level_spinner)
        var skillLevel = view.findViewById<RatingBar>(R.id.level)
        val deleteIcon = view.findViewById<ImageView>(R.id.delete_skill)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSkill(skillName: String, skillLevel: Int) {
        hashMap[skillName] = skillLevel
        list = ArrayList(hashMap.entries)
        this.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteSkill(skillName: String) {
        hashMap.remove(skillName)
        list = java.util.ArrayList(hashMap.entries)
        this.notifyDataSetChanged()
    }
}