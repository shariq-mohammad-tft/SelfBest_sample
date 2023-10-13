package com.tft.selfbest.ui.fragments.overview

import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.ObservationsResponse
import com.tft.selfbest.models.SelectedTimeSheetData
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.ShareWithManagerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareWithManagerViewModel @Inject constructor(
    private val shareWithManagerRepository: ShareWithManagerRepository,
    val preference: SelfBestPreference,
) : ViewModel(), LifecycleObserver {
    private val confirmationMessage = MutableLiveData<String>()
    val showMessageObserver: LiveData<String> = confirmationMessage
    private val observationsLiveData = MutableLiveData<NetworkResponse<ObservationsResponse>>()
    val observationsObserver: LiveData<NetworkResponse<ObservationsResponse>> = observationsLiveData

    fun getManagersData() {
  /*      viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            shareWithManagerRepository.getManagerList(id).collect {
                if (it is NetworkResponse.Success) {
                    managersLiveData.postValue(it)
                }
            }
        }*/
    }

    fun getObservationsData(startDate: String, endDate: String) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            shareWithManagerRepository.getStats(id, startDate, endDate).collect {
                if (it is NetworkResponse.Success)
                    observationsLiveData.postValue(it)
            }
        }
    }

    fun sendTimeSheetToManager(selectedTimeSheetData: SelectedTimeSheetData) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            shareWithManagerRepository.sendTimeSheetData(id, selectedTimeSheetData).collect {
                if (it is NetworkResponse.Success)
                    confirmationMessage.postValue("Data has been sent to manager email")
            }
        }
    }
}