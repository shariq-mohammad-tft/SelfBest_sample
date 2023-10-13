package com.tft.selfbest.repository

import com.tft.selfbest.models.EndTime
import com.tft.selfbest.models.StartBody
import com.tft.selfbest.models.StartTime
import com.tft.selfbest.models.TimeInterval
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.LocalDateTime
import javax.inject.Inject

class GetGoHourRepository @Inject constructor(
    private val client : SelfBestApiClient) {

    suspend fun start(userId : Int, startStatus : StartBody) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.start(userId, "Asia/Calcutta", startStatus) })
    }

    suspend fun pause(userId : Int, startTime : StartTime) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.pause(userId, startTime) })
    }

    suspend fun reset(userId : Int, startTime : StartTime) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.reset(userId, "Asia/Calcutta", startTime) })
    }

    suspend fun resume(userId : Int, endTime : EndTime) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.resume(userId, endTime) })
    }

    suspend fun end(userId : Int, endTime : EndTime) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.end(userId, "Asia/Calcutta", endTime) })
    }

    suspend fun timeInterval(userId: Int, timeInterval: TimeInterval) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.timeInterval(userId,"Asia/Calcutta", timeInterval) })
    }

    suspend fun getActivity(userId : Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getActivity(userId) })
    }

    suspend fun getTimeline(userId : Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getTimelineData(userId, "Asia/Kolkata") })
    }
}