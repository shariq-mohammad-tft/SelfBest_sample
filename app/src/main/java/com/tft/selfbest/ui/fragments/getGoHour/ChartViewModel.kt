package com.tft.selfbest.ui.fragments.getGoHour

import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.GetChartData
import com.tft.selfbest.models.ObservationsResponse
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.ChartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChartViewModel @Inject constructor(private val chartRepository: ChartRepository,
                                         val preference: SelfBestPreference
) : ViewModel(), LifecycleObserver {
    private  val chartlogs= MutableLiveData<NetworkResponse<GetChartData>>()
    val chartLogObserver: LiveData<NetworkResponse<GetChartData>> = chartlogs

    private val observationsLiveData = MutableLiveData<NetworkResponse<ObservationsResponse>>()
    val observationsObserver: LiveData<NetworkResponse<ObservationsResponse>> = observationsLiveData



    fun getChart(location:String){
        viewModelScope.launch {
            val id=preference.getLoginData?.id?:return@launch
            chartRepository.getChartLogs(id, location).collect {
                if(it is NetworkResponse.Success)
                    chartlogs.postValue(it)
            }
        }
    }
}