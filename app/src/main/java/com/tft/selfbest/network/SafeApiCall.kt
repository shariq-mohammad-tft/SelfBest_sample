package com.tft.selfbest.network

import com.bumptech.glide.load.engine.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): NetworkResponse<T> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        NetworkResponse.Error("", null)
                    }
                    else -> {
                        NetworkResponse.Error("", null)
                    }
                }
            }
        }
    }
}