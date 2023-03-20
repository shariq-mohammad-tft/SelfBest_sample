package com.tft.selfbest.ui.fragments.settings

import android.util.Log
import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.AddDistraction
import com.tft.selfbest.models.CurrentDistraction
import com.tft.selfbest.models.DistractionAppResponse
import com.tft.selfbest.models.ToggleDistraction
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.DistractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DistractionViewModel @Inject constructor(
    val preference: SelfBestPreference,
    val repository: DistractionRepository,
) : ViewModel(), LifecycleObserver{
    private val distractions = MutableLiveData<NetworkResponse<DistractionAppResponse>>()
    val distractionObserver: LiveData<NetworkResponse<DistractionAppResponse>> = distractions
    private val deleteDistraction = MutableLiveData<String>()
    val deleteDistractionObserver: LiveData<String> = deleteDistraction
    private val addedDistraction = MutableLiveData<String>()
    val addDistractionObserver: LiveData<String> = addedDistraction


    fun getDistraction() {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.getDistraction(userId).collect {
                //Log.e("Distraction : ", "123")
                if (it is NetworkResponse.Success) {
                    //Log.e("Distraction : ", it.toString())
                    distractions.postValue(it)
                }
            }
        }
    }

    fun addDistraction(addDistraction: AddDistraction){
        viewModelScope.launch {
            val userId = preference.getLoginData?.id?: return@launch
            repository.addDistraction(userId, addDistraction).collect{
                if(it is NetworkResponse.Success) {
                    addedDistraction.postValue("Distraction is added")
                    getDistraction()
                }else if(it is NetworkResponse.Error){
                    addedDistraction.postValue(it.msg )
                }
            }
        }
    }

    fun deleteDistraction(id : Int){
        viewModelScope.launch {
            val userId = preference.getLoginData?.id?: return@launch
            repository.deleteDistraction(userId, id).collect{
                if(it is NetworkResponse.Success) {
                    //Log.e("Swipe "," Test 4")
                    deleteDistraction.postValue("Distraction is deleted")
                    getDistraction()
                }
            }
        }
    }

    fun toggleDistraction(togDist : ToggleDistraction){
        viewModelScope.launch {
            val userId = preference.getLoginData?.id?: return@launch
            repository.toggleDistraction(userId, togDist).collect{
                //Log.e("Distraction ", "Toggle 3")
                if(it is NetworkResponse.Success) {
                    //Log.e("Toggle", " Distraction 4")
                    getDistraction()
                }
            }
        }
    }
}