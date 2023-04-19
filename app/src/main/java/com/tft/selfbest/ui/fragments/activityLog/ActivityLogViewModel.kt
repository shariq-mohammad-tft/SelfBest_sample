package com.tft.selfbest.ui.fragments.activityLog

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.FilterOptions
import com.tft.selfbest.models.SelectedCategory
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.StatisticsRepository
import com.tft.selfbest.ui.fragments.statistics.StatisticsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivityLogViewModel()
//@Inject constructor(
//    private val statisticsRepository: StatisticsRepository,
//    val preference: SelfBestPreference,
//) : ViewModel(), LifecycleObserver {
//    val filterCriteria = MutableLiveData<FilterOptions>()
//    private val categories = MutableLiveData<List<SelectedCategory>>()
//    private val selectedCategories = MutableLiveData<List<SelectedCategory>>()
//
//    fun applyFilters(filters: FilterOptions) {
//        // Call API with filter criteria and update filteredData in StatisticsViewModel
//        viewModelScope.launch {
//            val id = preference.getLoginData?.id ?: return@launch
//            statisticsRepository.getActivityLogs(id, filters.platform, filters.duration, filters.startDate, filters.endDate).collect {
//                if(it is NetworkResponse.Success) {
//                    statisticsVM.filteredData.postValue(it.data!!)
//                }
//                else
//                    Log.e("Activity Log", it.toString())
//            }
//        }
//    }
//
//    fun setCategories(categoryList: List<SelectedCategory>) {
//        categories.value = categoryList
//    }
//
//    fun getCategories(): LiveData<List<SelectedCategory>> {
//        return categories
//    }
//
//    fun getSelectedCategories(): LiveData<List<SelectedCategory>> {
//        return selectedCategories
//    }
//
//    fun selectCategory(category: SelectedCategory) {
//        val selectedList = selectedCategories.value?.toMutableList() ?: mutableListOf()
//        selectedList.add(category)
//        selectedCategories.value = selectedList
//    }
//
//    fun deselectCategory(category: SelectedCategory) {
//        val selectedList = selectedCategories.value?.toMutableList() ?: mutableListOf()
//        selectedList.remove(category)
//        selectedCategories.value = selectedList
//    }
//
//}