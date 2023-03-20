package com.tft.selfbest.network

import androidx.annotation.Keep

sealed class NetworkResponse<T> {
    data class Success<T>(val data: T? = null) : NetworkResponse<T>()
    data class Error<T>(val msg: String, val data: T? = null) : NetworkResponse<T>()
    data class Loading<T>(val msg: String = "") : NetworkResponse<T>()
}