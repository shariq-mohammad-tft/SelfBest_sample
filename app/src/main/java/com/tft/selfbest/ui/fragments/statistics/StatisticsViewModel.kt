package com.tft.selfbest.ui.fragments.statistics

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.*
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.StatisticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository,
    val preference: SelfBestPreference
) : ViewModel(), LifecycleObserver {
    private val activityLogs = MutableLiveData<NetworkResponse<ActivityLogValuesHigh>>()
    val activityLogsObserver: LiveData<NetworkResponse<ActivityLogValuesHigh>> = activityLogs

    private val observationsLiveData = MutableLiveData<NetworkResponse<ObservationsResponse>>()
    val observationsObserver: LiveData<NetworkResponse<ObservationsResponse>> = observationsLiveData

    private val queryLiveData = MutableLiveData<NetworkResponse<QueryTableResponse>>()
    val queryObserver: LiveData<NetworkResponse<QueryTableResponse>> = queryLiveData

    private val queryAnsweredLiveData =
        MutableLiveData<NetworkResponse<List<QueryAnsweredResponse>>>()
    val queryAnsweredObserver: LiveData<NetworkResponse<List<QueryAnsweredResponse>>> =
        queryAnsweredLiveData

    private val updation = MutableLiveData<NetworkResponse<UpdationBody>>()
    val updationObserver: LiveData<NetworkResponse<UpdationBody>> = updation

    private val pg = MutableLiveData<NetworkResponse<List<PointGraphResponse>>>()
    val pgObserver: LiveData<NetworkResponse<List<PointGraphResponse>>> = pg

    fun getStats(startDate: String, endDate: String) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            statisticsRepository.getStats(id, startDate, endDate).collect {
                if (it is NetworkResponse.Success)
                    observationsLiveData.postValue(it)
            }
        }
    }

    fun getLogs(type: String, event: String) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            statisticsRepository.getActivityLogs(id, type, event).collect {
                activityLogs.postValue(it)
            }
        }
    }

    fun getLogs(type: String, event: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            statisticsRepository.getActivityLogs(id, type, event, startDate, endDate).collect {
                if (it is NetworkResponse.Success)
                    activityLogs.postValue(it)
                else
                    Log.e("Activity Log", it.toString())
            }
        }
    }

    fun getQuery(startDate: String, endDate: String, type: String) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            statisticsRepository.getQuery(id, startDate, endDate, type).collect {
                if (it is NetworkResponse.Success)
                    queryLiveData.postValue(it)
            }
        }
    }

    fun getAnsweredQuery(team_id: String, startDate: String, endDate: String, type: String) {
        viewModelScope.launch {
            val email = preference.getLoginData?.email ?: return@launch
            statisticsRepository.getAnsweredQuery(email, team_id, startDate, endDate, type)
                .collect {
                    if (it is NetworkResponse.Success)
                        queryAnsweredLiveData.postValue(it)
                }
        }
    }

    fun updateStatus(id: Int, status: Int) {
        viewModelScope.launch {
            statisticsRepository.updateStatus(id, status).collect {
                if (it is NetworkResponse.Success) {
                    updation.postValue(it)
                }
            }
        }
    }

    fun updateRelevance(id: Int, status: Int, db_detail: String) {
        viewModelScope.launch {
            statisticsRepository.updateRelevance(id, status, db_detail).collect {
                if (it is NetworkResponse.Success) {
                    updation.postValue(it)
                }
            }
        }
    }

    fun getPoints(event: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            statisticsRepository.getPoints(userId, event, startDate, endDate).collect {
                if (it is NetworkResponse.Success) {
                    pg.postValue(it)
                }
            }
        }
    }


}