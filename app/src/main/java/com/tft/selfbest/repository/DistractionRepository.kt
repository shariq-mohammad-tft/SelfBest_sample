package com.tft.selfbest.repository

import com.tft.selfbest.models.AddDistraction
import com.tft.selfbest.models.ToggleDistraction
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DistractionRepository @Inject constructor(private val networkClient : SelfBestApiClient) {
    fun getDistraction (userID: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.getDistractedApps(userID) })
    }.flowOn(Dispatchers.IO)


    fun addDistraction(userID: Int, addDistraction: AddDistraction) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.addDistraction(userID, addDistraction) })
    }.flowOn(Dispatchers.IO)

    fun deleteDistraction(userID: Int, id : Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.deleteDistraction(userID, id) })
    }.flowOn(Dispatchers.IO)

    fun toggleDistraction(userID: Int, togDist : ToggleDistraction) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.toggleDistraction(userID, togDist) })
    }.flowOn(Dispatchers.IO)
}