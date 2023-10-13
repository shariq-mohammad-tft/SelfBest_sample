package com.example.chat_feature.data

import com.example.chat_feature.utils.getCurrentTime
import com.google.gson.annotations.SerializedName

data class SimplifyPathMessage(
    val senderId: String = ((1..2).random()).toString(),
    val message: String,
    val organisationId: String = ((1..2).random()).toString()
)

data class SimplifyHistoryMessage(
    @SerializedName("message")
    val message: String,
    @SerializedName("sentby")
    val sentby: String,
    @SerializedName("timestamp")
    val timestamp: String = getCurrentTime()
)


