package com.tft.selfbest.repository

import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChartRepository @Inject constructor(
    private val client: SelfBestApiClient) {


    suspend fun getChartLogs(userID:Int, location:String)= flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getChartData(userID, location) })
    }


}