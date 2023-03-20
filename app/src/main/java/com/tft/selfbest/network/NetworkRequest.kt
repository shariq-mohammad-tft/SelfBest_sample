package com.tft.selfbest.network

import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object NetworkRequest {
    suspend fun <T> process(api: suspend () -> T): NetworkResponse<T> {
        try {
            return NetworkResponse.Success(api.invoke())
        } catch (exception: Exception) {
            var error = when (exception) {
                is HttpException -> {
                    var error = "Something went Wrong"
                    try {
                        exception.response()?.let { errorBody ->
                            errorBody.errorBody()?.let { responseBody ->
                                val jsonObject = JSONObject(responseBody.string())
                                val errorKey = "Error"
                                if (jsonObject.has(errorKey)) {
                                    error = jsonObject.getString(errorKey)
                                }
                            }
                        }
                    } catch (exe: java.lang.Exception) { }
                    error
                }

                is SocketTimeoutException -> {
                    "SocketTimeoutException"
                }

                is IOException -> {
                    "Check Your Internet Connection"
                }
                else -> {
                    "Something went Wrong"
                }
            }
            return NetworkResponse.Error(error)
        }
    }
}