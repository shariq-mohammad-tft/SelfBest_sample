package com.tft.selfbest.repository

import android.util.Log
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StatisticsRepository @Inject constructor(
    private val client : SelfBestApiClient) {

    suspend fun getActivityLogs(userId : Int, type : String, event : String) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getActivityLogs(userId, type, event) })
    }

    suspend fun getActivityLogs(userId : Int, type : String, event : String, startDate: String, endDate: String) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getActivityLogs(userId, type, event, startDate, endDate) })
    }

    suspend fun getStats(userId: Int, startDate : String, endDate: String) = flow{
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.observations(userId, startDate, endDate)
        })
    }

//    suspend fun getQuery(id: Int, startDate: String, type: String) = flow{
//        emit(NetworkResponse.Loading())
//        emit(NetworkRequest.process {
//            client.apis.getQuery(id, startDate, type)
//        })
//    }

    suspend fun getQuery(id: Int, startDate: String, endDate: String, type: String) = flow{
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.getQuery(id, startDate, endDate, type)
        })
    }

    suspend fun getAnsweredQuery(email: String, team_id: String, start_date: String, end_Date: String, type: String) = flow{
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.getAnsweredQuery(email, team_id, start_date, end_Date, type)
        })
    }

    suspend fun updateStatus(id: Int, status: Int) = flow{
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.updateStatus(id, status)
        })
    }

    suspend fun updateRelevance(id: Int, relevance: Int, db_detail: String) = flow{
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.updateRelevance(id, relevance, db_detail)
        })
    }

    suspend fun getPoints(userId : Int, event : String, startDate : String, endDate: String) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getPoints(userId, "Asia/Calcutta", event, startDate, endDate) })
    }

//    suspend fun getDistractedTime(userId: Int) = flow {
//        emit(NetworkResponse.Loading())
//        emit(NetworkRequest.process{
//            client.apis.getDistractedTime(userId)
//        })
//    }

//    suspend fun getTotalGetGoHours(userId: Int) = flow {
//        emit(NetworkResponse.Loading())
//        emit(NetworkRequest.process{
//            client.apis.getTotalGetGoHours(userId)
//        })
//    }

}