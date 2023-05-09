package com.tft.selfbest.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap
import com.tft.selfbest.R
import java.util.*


class SignUpSkillListAdapter(
    val context: Context, val hashMap: LinkedTreeMap<String, Int>,
    private val changeRatingListener: ChangeRatingListener
) : RecyclerView.Adapter<SignUpSkillListAdapter.SkillViewHolder>() {
    var list = ArrayList(hashMap.entries)
    //val skillLevel = listOf("Beginner", "Intermediate", "Expert")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.signup_skill_single_row, parent, false)
        return SkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
//        val rnd = Random()
//        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        if (list.isNotEmpty() && position < list.size) {
            val mapObject = list[position]
            //holder.state.isChecked = true
            if (mapObject != null) {
                holder.skillName.text = mapObject.key
                holder.skillLevel.rating = mapObject.value.toFloat()
//                holder.initial.text = mapObject.key.toString().substring(0, 1)
                holder.deleteIcon.setOnClickListener {
                    val adapterPosition = holder.absoluteAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        val removedMapObject = list.getOrNull(adapterPosition)
                        if (removedMapObject != null) {
                            list.removeAt(adapterPosition)
                            hashMap.remove(removedMapObject.key)
                            changeRatingListener.itemRemoved(removedMapObject.key)
                            this.notifyItemRemoved(adapterPosition)
                        }
                    }
                }

                if (mapObject.key != null && mapObject.key.isNotEmpty()) {
                    holder.initial.text = mapObject.key.substring(0, 1)
                } else {
                    holder.initial.text = ""
                }
//        val unwrappedDrawable: Drawable? =
//            AppCompatResources.getDrawable(context, R.drawable.name_bg)
//        val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
//        if (wrappedDrawable != null) {
//            DrawableCompat.setTint(wrappedDrawable, color)
//        }

//        holder.levelName.text = if(holder.skillLevel.rating.toInt() == 1) "Beginner" else if(holder.skillLevel.rating.toInt() == 2) "Intermediate" else "Expert"
//        holder.deleteIcon.setOnClickListener(View.OnClickListener {
//            list.removeAt(position)
//            hashMap.remove(mapObject.key)
//            this.notifyItemRemoved(position)
//        })
                holder.skillLevel.onRatingBarChangeListener =
                    OnRatingBarChangeListener { _, _, _ ->
//                Log.e("Skill Level Changed Skill : ", "$holder.skillName.toString()")
                        if(holder.skillName.text?.toString() != null) {
                            changeRatingListener.changeRating(
                                holder.skillLevel.rating,
                                holder.skillName.text.toString()
                            )
                            notifyDataSetChanged()
                        }
                    }
            }
        }

        //holder.skillLevel.onRatingBarChangeListener = OnRatingBarChangeListener
//            changeRatingListener.changeRating(holder.skillLevel.rating, holder.skillName.toString())
//            notifyDataSetChanged()
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

        //holder.skillLevel.adapter = spinAdapter
        //holder.skillLevel.setSelection(mapObject.value.toInt() - 1)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ChangeRatingListener : OnRatingBarChangeListener {
        fun changeRating(level: Float, skill: String)
        fun itemRemoved(skill: String)
    }

    inner class SkillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val skillName: TextView = view.findViewById(R.id.name_of_skill)
        var skillLevel: RatingBar = view.findViewById(R.id.level)
        val initial: TextView = view.findViewById(R.id.initial)
        val deleteIcon : ImageView = view.findViewById(R.id.delete)
        //var state : CheckBox = view.findViewById(R.id.state)
        //var levelName : TextView = view.findViewById(R.id.level_name)
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
        list = ArrayList(hashMap.entries)
        this.notifyDataSetChanged()
    }
}