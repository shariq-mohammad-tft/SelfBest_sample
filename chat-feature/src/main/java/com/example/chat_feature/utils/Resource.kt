package com.example.chat_feature.utils

sealed class Resource<out T> {

    data class Success<out T>(val value: T) : Resource<T>()

    object Loading : Resource<Nothing>()

    data class Failure(
        val errorCode: Int? = null,
        val isNetworkError: Boolean? = null,
    ) : Resource<Nothing>()


}
