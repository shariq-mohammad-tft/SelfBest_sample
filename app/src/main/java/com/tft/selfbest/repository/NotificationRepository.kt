package com.tft.selfbest.repository

import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val client: SelfBestApiClient) {
    suspend fun getAllNotification(userId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.getNotification(userId)
        })
    }
}