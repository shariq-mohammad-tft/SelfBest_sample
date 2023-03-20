package com.tft.selfbest.repository

import com.tft.selfbest.models.calendarEvents.EventDetail
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EventsRepository @Inject constructor(private val networkClient: SelfBestApiClient) {
    suspend fun getAllEvents(userId: Int, day: Int, month: String, year: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            networkClient.apis.getUpcomingEvents(userId,
                "Asia/Calcutta", day, month, year)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun addOneDayEvent(userId: Int, eventDetail: EventDetail) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.addOneDayEvent(userId, eventDetail) })
    }.flowOn(Dispatchers.IO)
}