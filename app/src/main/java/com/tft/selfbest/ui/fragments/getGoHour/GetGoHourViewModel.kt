package com.tft.selfbest.ui.fragments.getGoHour

import android.util.Log
import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.*
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.GetGoHourRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetGoHourViewModel @Inject constructor(
    private val getGoHourRepository: GetGoHourRepository,
    //private val selfBestRepository: SelfBestRepository,
    val preference: SelfBestPreference
) : ViewModel(), LifecycleObserver {

    private val startGetGoHour = MutableLiveData<NetworkResponse<Unit>>()
    val startGetGoHourObserver: LiveData<NetworkResponse<Unit>> = startGetGoHour

    private val pauseGetGoHour = MutableLiveData<NetworkResponse<Unit>>()
    val pauseGetGoHourObserver: LiveData<NetworkResponse<Unit>> = pauseGetGoHour

    private val resetGetGoHour = MutableLiveData<NetworkResponse<Reset>>()
    val resetGetGoHourObserver: LiveData<NetworkResponse<Reset>> = resetGetGoHour

    private val resumeGetGoHour = MutableLiveData<NetworkResponse<TotalPauseTime>>()
    val resumeGetGoHourObserver: LiveData<NetworkResponse<TotalPauseTime>> = resumeGetGoHour

    private val endGetGoHour = MutableLiveData<NetworkResponse<Unit>>()
    val endGetGoHourObserver: LiveData<NetworkResponse<Unit>> = endGetGoHour

    private val timeCInterval = MutableLiveData<NetworkResponse<Unit>>()
    //val timeIntervalObserver: LiveData<NetworkResponse<Unit>> = timeCInterval

    private val activityLog = MutableLiveData<NetworkResponse<ActivityResponse>>()
    val activityLogObserver: LiveData<NetworkResponse<ActivityResponse>> = activityLog

    private val activityTimeline =
        MutableLiveData<NetworkResponse<List<ActivityTimelineResponse>?>>()
    val activityTimelineObserver: LiveData<NetworkResponse<List<ActivityTimelineResponse>?>> =
        activityTimeline


    //private val selfBestRep = MutableLiveData<NetworkResponse<GoHourResponse>>()
    //val selfBestRepositoryObserver: LiveData<NetworkResponse<GoHourResponse>> = selfBestRep

    fun start(startStatus: StartBody) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            getGoHourRepository.start(id, startStatus).collect {
                if (it is NetworkResponse.Success)
                    startGetGoHour.postValue(it)
            }
        }
    }

    fun pause(startTime: StartTime) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            getGoHourRepository.pause(id, startTime).collect {
                if (it is NetworkResponse.Success)
                    pauseGetGoHour.postValue(it)
                else
                    Log.e("Timer: Pause", "$it")
            }
        }
    }

    fun reset(startTime: StartTime) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            getGoHourRepository.reset(id, startTime).collect {
                Log.e("Timer: ", "$it")
                if (it is NetworkResponse.Success)
                    resetGetGoHour.postValue(it)
                else
                    Log.e("Timer: ", "$it")
            }
        }
    }

    fun resume(endTime: EndTime) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            getGoHourRepository.resume(id, endTime).collect {
                if (it is NetworkResponse.Success) {
                    resumeGetGoHour.postValue(it)
                    Log.e("Timer: Resume Success", "$it")
                } else
                    Log.e("Timer: Resume", "$it")
            }
        }
    }

    fun end(endTime: EndTime) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            getGoHourRepository.end(id, endTime).collect {
                if (it is NetworkResponse.Success)
                    endGetGoHour.postValue(it)
                else
                    Log.e("Timer: ", "$it")
            }
        }
    }

    fun timeInterval(timeInterval: TimeInterval) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            getGoHourRepository.timeInterval(id, timeInterval).collect {
                if (it is NetworkResponse.Success)
                    timeCInterval.postValue(it)
                else
                    Log.e("Timer: ", "$it")
            }
        }
    }

    fun getActivity() {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            getGoHourRepository.getActivity(id).collect {
                activityLog.postValue(it)
            }
        }
    }

//    fun checkGetGoPausedStatus(){
//        viewModelScope.launch {
//            val id=preference.getLoginData?.id?:return@launch
//            selfBestRepository.getGoHourStatus().collect{
//                if(it is NetworkResponse.Success){
//                    selfBestRep.postValue(it)
//                }
//            }
//
//        }
//    }

    fun getTimeline() {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            getGoHourRepository.getTimeline(id).collect {
                if (it is NetworkResponse.Success)
                    activityTimeline.postValue(it)
                else
                    Log.e("Timeline", it.toString())
            }
        }
    }
}