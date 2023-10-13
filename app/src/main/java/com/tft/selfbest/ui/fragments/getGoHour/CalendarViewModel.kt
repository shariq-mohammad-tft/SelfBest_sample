package com.tft.selfbest.ui.fragments.getGoHour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.calendarEvents.EventDetail
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CalenderViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val preference: SelfBestPreference) :ViewModel() {

    private val upcomingEventLiveData = MutableLiveData<NetworkResponse<List<EventDetail>>>()
    val upComingEventsObserver: LiveData<NetworkResponse<List<EventDetail>>> = upcomingEventLiveData
    private val eventStatusData = MutableLiveData<String>()
    val addEventObserver: LiveData<String> = eventStatusData
    fun getAllUpcomingEvents(day: Int, month: String, year: Int) {
        if (upcomingEventLiveData.value is NetworkResponse.Loading)
            return
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            eventsRepository.getAllEvents(id, day, month, year).collect {
                upcomingEventLiveData.postValue(it)
            }
        }
    }

    fun addRecursiveDayEvent(eventDetail: EventDetail) {
        viewModelScope.launch {
            val id = preference.getLoginData?.id ?: return@launch
            eventsRepository.addOneDayEvent(id, eventDetail).collect {
                if (it is NetworkResponse.Success) {
                    // getAllUpcomingEvents()
                    eventStatusData.value = "Event added successfully"
                }
                if (it is NetworkResponse.Error)
                    eventStatusData.value = it.msg
            }
        }
    }
}