package com.tft.selfbest.ui.fragments.ui.home

import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.overview.OverViewActivityResponse
import com.tft.selfbest.models.overview.OverViewCompletedResponse
import com.tft.selfbest.models.overview.OverViewDistractedResponse
import com.tft.selfbest.models.overview.OverViewLevelResponse
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.OverviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val pref: SelfBestPreference,
    private val repository: OverviewRepository
) : ViewModel(), LifecycleObserver {
    private val overViewActivityLiveData =
        MutableLiveData<NetworkResponse<OverViewActivityResponse>>()
    val overViewActivityObserver: LiveData<NetworkResponse<OverViewActivityResponse>> =
        overViewActivityLiveData
    private val overViewLevelLiveData = MutableLiveData<NetworkResponse<OverViewLevelResponse>>()
    val overViewLevelObserver: LiveData<NetworkResponse<OverViewLevelResponse>> =
        overViewLevelLiveData
    private val overviewCompletedLiveData =
        MutableLiveData<NetworkResponse<OverViewCompletedResponse>>()
    val overViewCompletedObserver: LiveData<NetworkResponse<OverViewCompletedResponse>> =
        overviewCompletedLiveData
    private val overViewDistractedLiveData =
        MutableLiveData<NetworkResponse<OverViewDistractedResponse>>()
    val overViewDistractedObserver: LiveData<NetworkResponse<OverViewDistractedResponse>> =
        overViewDistractedLiveData

    fun getOverviewActivity() {
        if (overViewActivityLiveData.value is NetworkResponse.Loading)
            return
        viewModelScope.launch {
            val userId = pref.getLoginData?.id ?: return@launch
            val token = pref.getLoginData?.accessToken ?: return@launch
            repository.getOverViewActivity(userId).collect {
                overViewActivityLiveData.postValue(it)
            }
        }
    }

    fun getOverViewLevel() {
        if (overViewLevelLiveData.value is NetworkResponse.Loading)
            return
        viewModelScope.launch {
            val userId = pref.getLoginData?.id ?: return@launch
            repository.getOverviewLevel(userId).collect {
                overViewLevelLiveData.postValue(it)
            }
        }
    }

    fun getTotalDistractedTime() {
        if (overViewDistractedLiveData.value is NetworkResponse.Loading)
            return
        viewModelScope.launch {
            val userId = pref.getLoginData?.id ?: return@launch
            repository.getOverviewDistractedTime(userId).collect {
                overViewDistractedLiveData.postValue(it)
            }
        }
    }

    fun getTotalCompletedTime() {
        if (overviewCompletedLiveData.value is NetworkResponse.Loading)
            return
        viewModelScope.launch {
            val userId = pref.getLoginData?.id ?: return@launch
            repository.getOverviewCompletedTime(userId).collect {
                overviewCompletedLiveData.postValue(it)
            }
        }
    }
}