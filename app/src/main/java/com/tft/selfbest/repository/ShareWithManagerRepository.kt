package com.tft.selfbest.repository

import com.tft.selfbest.models.SelectedTimeSheetData
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShareWithManagerRepository @Inject constructor(private val client: SelfBestApiClient) {
    suspend fun getManagerList(userID: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getManagers(userID) })
    }

    suspend fun getStats(userID: Int, startDate: String, endDate: String) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.observations(userID, startDate, endDate) })
    }
    suspend fun sendTimeSheetData(userID: Int, selectedTimeSheetData: SelectedTimeSheetData) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.sendTimeSheetToManager(userID,selectedTimeSheetData) })
    }
}