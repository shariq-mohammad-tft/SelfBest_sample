package com.tft.selfbest.ui.fragments.inputProgress

import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.GetChartData
import com.tft.selfbest.models.InputData
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.ChartRepository
import com.tft.selfbest.repository.InputProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class InputProgressViewModel @Inject constructor(private  val inputProgressRepository: InputProgressRepository,
                                                 val preference: SelfBestPreference): ViewModel(), LifecycleObserver {

    private val rating=MutableLiveData<NetworkResponse<Unit>>()
    val ratinObserver:LiveData<NetworkResponse<Unit>> = rating

    fun getInput(inputData:InputData){
        viewModelScope.launch {
            val id=preference.getLoginData?.id?:return@launch
            inputProgressRepository.inputProgressComplete(id, inputData ).collect{
                if(it is NetworkResponse.Success){
                    rating.postValue(it)
                }
            }
        }
    }
}