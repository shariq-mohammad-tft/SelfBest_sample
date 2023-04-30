package com.tft.selfbest.ui.adapter
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.SelectedCategory
import com.tft.selfbest.ui.dialog.ActivityLogFiltersDialog
import com.tft.selfbest.ui.fragments.activityLog.ForCategories
import java.util.*

class SelectCategoryAdapter(
    val context: Context,
    val list: List<SelectedCategory>,
    val selectedCategory: MutableList<String>,
    val selectionOfcategoriesListener: SelectionOfCategories) :
    RecyclerView.Adapter<SelectCategoryAdapter.SelectCategoryViewHolder>() {
    var numberOfSelectedCategories = 3
    val selectedCategories = selectedCategory

//    init {
//        for (cat in list.sortedByDescending { it.duration }.take(3)){
//            selectedCategories.add(cat.category)
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCategoryViewHolder {
        return SelectCategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.select_category_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SelectCategoryViewHolder, position: Int) {

        val category = list[position]
        holder.state.isChecked = category.category in selectedCategories
        holder.circleImage.setColorFilter(Color.rgb(0, 255, 0))
        holder.categoryName.text = category.category
        holder.duration.text = getTimeInFormat(category.duration)
        Log.e("Activity Log 0", holder.state.isChecked.toString())
        holder.state.setOnClickListener(View.OnClickListener {
            if(category.category in selectedCategories){
                holder.state.isChecked = false
                selectionOfcategoriesListener.deSelectCategory(category)
                selectedCategories.remove(category.category)
//                currentCategories.remove(category)
//                Log.e("Activity Log 0", category.category)
//                setSelectedCategories(currentCategories)
            }else{
                if(selectedCategories.size >= 3){
                    holder.state.isChecked = false
                    Log.e("Activity Log 1", category.category)
                    Toast.makeText(context, "Please deselect any previous categories to select new categories", Toast.LENGTH_SHORT).show()
                }else{
                    holder.state.isChecked = true
                    selectionOfcategoriesListener.selectCategory(category)
                    selectedCategories.add(category.category)
//                    currentCategories.add(category)
//                    Log.e("Activity Log 2", category.category)
//                    setSelectedCategories(currentCategories)
                }
            }
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class SelectCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val state: CheckBox = view.findViewById(R.id.state)
        val circleImage: ImageView = view.findViewById(R.id.circle)
        val categoryName: TextView = view.findViewById(R.id.category)
        val duration: TextView = view.findViewById(R.id.duration)
    }

    private fun getTimeInFormat(time: Double): String {
        val ans: String
        val hours = time.toInt() / 3600
        val remainder = time.toInt() - hours * 3600
        val mins = remainder / 60
        if (hours == 0) {
            ans = "$mins mins"
        } else {
            ans = "$hours hrs $mins mins"
        }
        return ans
    }

    interface SelectionOfCategories{
        fun selectCategory(category: SelectedCategory)
        fun deSelectCategory(category: SelectedCategory)
    }

//    interface CategoryFilter{
//        fun filterCategory()
//    }

}