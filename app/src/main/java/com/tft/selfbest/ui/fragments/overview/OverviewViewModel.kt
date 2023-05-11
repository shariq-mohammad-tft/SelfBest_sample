package com.tft.selfbest.ui.fragments.overview

import android.util.Log
import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.GetGoHourResponse
import com.tft.selfbest.models.NotificationDetail
import com.tft.selfbest.models.StartTime
import com.tft.selfbest.models.notifications.NotificationResponse
import com.tft.selfbest.models.overview.OverViewLevelResponse
import com.tft.selfbest.models.overview.OverviewCourses
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.NotificationRepository
import com.tft.selfbest.repository.OverviewRepository
import com.tft.selfbest.ui.fragments.getGoHour.GetGoHour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val repository: OverviewRepository,
    private val nrepository: NotificationRepository,
    private val preference: SelfBestPreference,
) : ViewModel(), LifecycleObserver {
    private val overViewLevelLiveData = MutableLiveData<NetworkResponse<OverViewLevelResponse>>()
    val overViewLevelObserver: LiveData<NetworkResponse<OverViewLevelResponse>> =
        overViewLevelLiveData
    private val courseLiveData = MutableLiveData<NetworkResponse<OverviewCourses>>()
    val overViewCourseObserver: LiveData<NetworkResponse<OverviewCourses>> =
        courseLiveData

    private val getGoHourData = MutableLiveData<NetworkResponse<GetGoHourResponse>>()
    val getGoHourObserver: LiveData<NetworkResponse<GetGoHourResponse>> =
        getGoHourData

    private val getStarted = MutableLiveData<NetworkResponse<Unit>>()
    val getStartedObserver: LiveData<NetworkResponse<Unit>> =
        getStarted

    private val notify = MutableLiveData<NetworkResponse<NotificationResponse>>()
    val notifyObserver: LiveData<NetworkResponse<NotificationResponse>> = notify

    fun getNotifications() {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            nrepository.getAllNotification(userId).collect {
                notify.postValue(it)
            }
        }
    }

    fun getOverViewLevel() {
        if (overViewLevelLiveData.value is NetworkResponse.Loading)
            return
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.getOverviewLevel(userId).collect {
                overViewLevelLiveData.postValue(it)
            }
        }
    }

    fun getCourses() {
        if (overViewLevelLiveData.value is NetworkResponse.Loading)
            return
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.getOverviewCourses(userId).collect { response ->
                courseLiveData.postValue(response)
            }
        }
    }

    fun getGoHour() {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            repository.getGoHour(id).collect {
                if (it is NetworkResponse.Success)
                    getGoHourData.postValue(it)
                else if (it is NetworkResponse.Error)
                    Log.e("Token ", "${it.msg}")
            }
        }
    }

    fun getStarted(startTime: StartTime) {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.getStarted(userId, startTime).collect {
                if (it is NetworkResponse.Success)
                    getStarted.postValue(it)
            }
        }
    }
}