package com.tft.selfbest.repository

import com.tft.selfbest.models.GoogleLoginRequest
import com.tft.selfbest.models.GoogleLoginRequest2
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepository @Inject constructor(private val networkClient: SelfBestApiClient) {

    suspend fun loginViaLinkedIn(code: String,  location: String, type: String, reactivate: Boolean) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.loginViaLinked(code, location, type, reactivate) })
        // networkClient.apis.login()
    }.flowOn(Dispatchers.IO)


    suspend fun loginViaGoogle(googleLoginRequest: GoogleLoginRequest) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.loginViaGoogle(googleLoginRequest) })
    }.flowOn(Dispatchers.IO)

    suspend fun loginViaGoogle(googleLoginRequest: GoogleLoginRequest2) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.loginViaGoogle(googleLoginRequest) })
    }.flowOn(Dispatchers.IO)
}
