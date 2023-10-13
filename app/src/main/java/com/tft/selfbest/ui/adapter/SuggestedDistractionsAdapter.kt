package com.tft.selfbest.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.AddDistraction
import com.tft.selfbest.models.SuggestedDistraction

class SuggestedDistractionsAdapter(
    val list: List<SuggestedDistraction>,
    private val addDistractionListener: AddDistractionListener,
    private val collapseListListener: CollapseListListener,
    private val expandListListener: ExpandListListener,
    private val deleteDistractionListener: DeleteSugDistraction
) : RecyclerView.Adapter<SuggestedDistractionsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuggestedDistractionsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_suggested_distraction, parent, false)
        return SuggestedDistractionsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SuggestedDistractionsViewHolder, position: Int) {
        if (position == if (list.size <= 4) 4 else list.size) {
            if(position == 4) {
                //Log.e("Distraction", "more " + list.size)
                holder.distractionName.text = "+More"
                holder.distractionName.setTextColor(Color.parseColor("#1D71D4"))
                holder.distractionIcon.visibility = View.GONE
                holder.distraction.setBackgroundColor(Color.WHITE)
                holder.distraction.setOnClickListener(View.OnClickListener {
                    expandListListener.expandList(list as ArrayList<SuggestedDistraction>)
                    notifyItemRangeChanged(position, 4)
                })
            }else{
                //Log.e("Distraction", "Less " + list.size)
                holder.distractionName.text = "Less"
                holder.distractionName.setTextColor(Color.parseColor("#1D71D4"))
                holder.distractionIcon.visibility = View.GONE
                holder.distraction.setBackgroundColor(Color.WHITE)
                holder.distraction.setOnClickListener(View.OnClickListener {
                    collapseListListener.collapseList(list as ArrayList<SuggestedDistraction>)
                })
            }

            }
        else {
            //Log.e("Distraction", "" + position + " " + list.size)
            val distraction = list[position]
            holder.distractionName.text = distraction.name
            holder.distractionIcon.setImageResource(distraction.Icon)
            if(distraction.state) {
                holder.distractionName.setTextColor(Color.WHITE)
                holder.distraction.setBackgroundResource(R.drawable.current_dist_bg)
            }
            holder.distraction.setOnClickListener(View.OnClickListener {
                if(!distraction.state) {
                    holder.distraction.setBackgroundColor(Color.parseColor("#63A1FF"))
                    holder.distraction.setBackgroundResource(R.drawable.current_dist_bg)
                    holder.distractionName.setTextColor(Color.WHITE)
                    val addDistractionObject = AddDistraction(distraction.url, distraction.name)
                    addDistractionListener.getAddDistraction(addDistractionObject)
                }else{
                    holder.distraction.setBackgroundColor(Color.parseColor("#EEEEEE"))
                    holder.distraction.setBackgroundResource(R.drawable.suggested_distraction_bg)
                    holder.distractionName.setTextColor(Color.BLACK)
                    list[position].state = false
                    deleteDistractionListener.deleteSugDistraction(distraction.id)
                }})
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }


    interface AddDistractionListener {
        fun getAddDistraction(addDistraction: AddDistraction)
    }

    interface CollapseListListener {
        fun collapseList(list: ArrayList<SuggestedDistraction>)
    }

    interface ExpandListListener {
        fun expandList(list: ArrayList<SuggestedDistraction>)
    }

    interface DeleteSugDistraction {
        fun deleteSugDistraction(id : Int)
    }

}

class SuggestedDistractionsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var distractionName: TextView = view.findViewById(R.id.s_distraction_name)
    var distractionIcon: ImageView = view.findViewById(R.id.s_distraction_icon)
    var distraction: LinearLayout = view.findViewById(R.id.container)
}