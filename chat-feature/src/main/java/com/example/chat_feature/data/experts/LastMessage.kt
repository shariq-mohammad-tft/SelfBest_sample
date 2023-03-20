package com.example.chat_feature.data.experts

import com.google.gson.annotations.SerializedName

data class LastMessage(
    @SerializedName("message")
    val message: String,
    @SerializedName("sentBy")
    val sentBy: String,
    @SerializedName("sentTo")
    val sentTo: String,
    @SerializedName("type")
    val type: String
)
