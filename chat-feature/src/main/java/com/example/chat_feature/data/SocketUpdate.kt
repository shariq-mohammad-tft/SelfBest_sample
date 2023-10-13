package com.example.chat_feature.data

import okio.ByteString

sealed class SocketUpdate {
    data class Success(
        val text: String? = null,
        val byteString: ByteString? = null,
    ) : SocketUpdate()

    data class Failure(
        val exception: Throwable? = null
    ) : SocketUpdate()
}