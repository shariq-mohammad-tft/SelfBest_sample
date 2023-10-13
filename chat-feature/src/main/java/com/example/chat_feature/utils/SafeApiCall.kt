package com.example.chat_feature.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

interface SafeApiCall {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {

        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (exception: Exception) {
                exception.printStackTrace()

                when (exception) {
                    is HttpException -> {
                        /*val errorMessage =
                            JSONObject(exception.response()!!.errorBody()!!.charStream().readText())

                        Resource.Failure(
                            message = errorMessage.getString("message"),
                            errorCode = exception.code()
                        )*/
                        Resource.Failure(
                            errorCode = exception.code()
                        )
                    }

                    is IOException -> {
                        Resource.Failure(
                            errorCode = null,
                            isNetworkError = true
                        )
                    }

                    else -> Resource.Failure(
                        errorCode = null,
                        isNetworkError = true
                    )
                }

            }
        }
    }
}