package com.tft.selfbest.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
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

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
//        val rnd = Random()
//        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        val mapObject = list[position]
        holder.skillName.setText(mapObject.key)
        holder.skillLevel.rating = mapObject.value.toFloat()
        holder.deleteIcon.setOnClickListener(View.OnClickListener {
            list.removeAt(position)
            Log.e("Deleted Skills", mapObject.key.toString())
            hashMap.remove(mapObject.key)
            this.notifyItemRemoved(position)
        })

        holder.initial.text = mapObject.key.toString().substring(0, 1)
//        val unwrappedDrawable: Drawable? =
//            AppCompatResources.getDrawable(context, R.drawable.name_bg)
//        val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
//        if (wrappedDrawable != null) {
//            DrawableCompat.setTint(wrappedDrawable, color)
//        }

        holder.skillLevel.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, _, _ ->
//                Log.e("Skill Level Changed Skill : ", "$holder.skillName.toString()")
                changeRatingListener.changeRating(
                    holder.skillLevel.rating,
                    holder.skillName.text.toString()
                )
                notifyDataSetChanged()
            }
//        holder.skillLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                mapObject.setValue((p2 + 1))
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                // to do
//            }
//
//        }
//        val spinAdapter = ArrayAdapter(context,
//            R.layout.spinner_main_item_style, skillLevel)
//        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
//        holder.skillLevel.adapter = spinAdapter
//        holder.skillLevel.setSelection(mapObject.value.toInt() - 1)
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