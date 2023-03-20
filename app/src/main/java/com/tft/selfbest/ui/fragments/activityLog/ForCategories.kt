package com.tft.selfbest.ui.fragments.activityLog

import android.util.Log
import com.tft.selfbest.models.SelectedCategory

interface ForCategories{
    var defaultCategories: MutableList<SelectedCategory>
    fun getSelectedCategories(): MutableList<SelectedCategory>{
        return this.defaultCategories
    }

    fun setSelectedCategories(categories: MutableList<SelectedCategory>){
        Log.e(" Set Categories", categories.toString())
        this.defaultCategories = categories
    }
}