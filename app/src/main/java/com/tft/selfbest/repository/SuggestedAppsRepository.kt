package com.tft.selfbest.repository

import com.tft.selfbest.models.suggestedApps.AppDetail
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SuggestedAppsRepository @Inject constructor(private val client: SelfBestApiClient) {
    suspend fun getSuggestedApps(userId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getSuggestedApps(userId) })
    }.flowOn(Dispatchers.IO)

    suspend fun addInSuggestedApps(userId: Int, appDetail: AppDetail) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.addInSuggestedAppList(userId, appDetail) })
    }.flowOn(Dispatchers.IO)
}