package com.tft.selfbest.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.ObservationDetail
import com.tft.selfbest.models.SelectedObservationDetail
import kotlin.math.roundToInt

class ActivitiesPerformedListAdapter(val context: Context, val list: List<ObservationDetail>) :
    RecyclerView.Adapter<ActivitiesPerformedListAdapter.PerformanceViewHolder>() {
    private val statusTypes = listOf("Focus", "Neutral", "Distracted")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformanceViewHolder {
        return PerformanceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_performed_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PerformanceViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val observationDetail = list[position]
        holder.workTime.text = "${(observationDetail.duration / 60f).roundToInt()}m"
        val spinnerArray = ArrayAdapter(context, R.layout.observation_spinner_item, statusTypes)
        spinnerArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinner.adapter = spinnerArray
        holder.spinner.setSelection(statusTypes.indexOf(list[position].type))
        holder.taskObservation.text = observationDetail.url
        holder.parentContainer.setBackgroundResource(if (list[position].isSelected) R.drawable.full_white_with_blue_border else R.drawable.full_white_with_border)
        holder.selectionView.setBackgroundResource(if (list[position].isSelected) R.drawable.selected_circle else R.drawable.un_selected_circle)
        holder.selectionView.elevation = if (list[position].isSelected) 15.0f else 0.0f
        holder.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                list[position].type = statusTypes[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        holder.commentText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                list[position].comment = text.toString()
            }

            override fun afterTextChanged(comment: Editable?) {

            }

        })
        holder.selectionView.setOnClickListener(View.OnClickListener {
            list[position].isSelected = !list[position].isSelected
            val modifiedTime = holder.workTime.text.toString().replace("m", "").toDouble()
            list[position].modifiedDuration = modifiedTime
            this.notifyItemChanged(position)
        }
        )
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    inner class PerformanceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskObservation: TextView = view.findViewById(R.id.observation_type)
        val workTime: TextView = view.findViewById(R.id.work_hour)
        val selectionView: ImageView = view.findViewById(R.id.selection_view)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val commentText: EditText = view.findViewById(R.id.comment_container)
        var parentContainer: ConstraintLayout = view.findViewById(R.id.parent_container)
    }

    fun getAllSelectedList(): ArrayList<SelectedObservationDetail> {
        val selectedList = ArrayList<SelectedObservationDetail>()
        list.forEach {
            if (it.isSelected) {
                selectedList.add(
                    SelectedObservationDetail(
                        it.category,
                        it.duration,
                        it.endAt,
                        it.startAt,
                        it.type,
                        it.url,
                        it.isSelected,
                        it.comment,
                        it.modifiedDuration
                    )
                )
            }
        }
        return selectedList
    }
}