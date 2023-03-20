package com.tft.selfbest.repository

import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRequestRepository @Inject constructor(private val client: SelfBestApiClient) {
    /*suspend fun userRequestData(UserID:Int)= flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getUserRequests(UserID) })
    }.flowOn(Dispatchers.IO)*/
}