package com.tft.selfbest.ui.fragments.detailPage

import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.suggestedApps.AppDetail
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.SuggestedAppsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuggestedAppsViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val repository: SuggestedAppsRepository,
    private val preference: SelfBestPreference,
) : ViewModel(), LifecycleObserver {
    private val getSuggestedApps = MutableLiveData<NetworkResponse<List<AppDetail>>>()
    val suggestedAppsObserver: LiveData<NetworkResponse<List<AppDetail>>> = getSuggestedApps
    fun getSuggestedApps() {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            repository.getSuggestedApps(id).collect {
                getSuggestedApps.postValue(it)
            }
        }
    }

    fun addSelectedAppInSuggestedList(appDetail: AppDetail) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            repository.addInSuggestedApps(id, appDetail).collect {
                if (it is NetworkResponse.Success){

                }
            }
        }
    }
}