package com.tft.selfbest.repository

import com.tft.selfbest.models.StartTime
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.Instant
import javax.inject.Inject

class OverviewRepository @Inject constructor(private val networkClient: SelfBestApiClient) {
    suspend fun getOverViewActivity(userId: Int) =
        flow {
            emit(NetworkResponse.Loading())
            emit(NetworkRequest.process {
                networkClient.apis.overViewActivity(
                    userId,
                    "Asia/Calcutta"
                )
            })
        }.flowOn(Dispatchers.IO)

    suspend fun getNotifications(userId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            networkClient.apis.getNotification(userId)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun getOverviewLevel(userId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.overViewLevel(userId) })
    }.flowOn(Dispatchers.IO)

    suspend fun getOverviewDistractedTime(userId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            networkClient.apis.getOverViewDistractedTime(
                userId,
                "Asia/Calcutta"
            )
        })
    }.flowOn(Dispatchers.IO)

    suspend fun getOverviewCompletedTime(userId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            networkClient.apis.getOverviewCompletedTime(userId)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun getOverviewCourses(userId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            networkClient.apis.getCourses(userId)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun getGoHour(userId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            networkClient.apis.getGoHour(userId)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun getStarted(userId: Int, startTime: StartTime) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            networkClient.apis.getStarted(userId, startTime)
        })
    }.flowOn(Dispatchers.IO)

}