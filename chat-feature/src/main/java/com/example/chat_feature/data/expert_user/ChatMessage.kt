package com.example.chat_feature.data.expert_user

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("key")
    val key: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("sentBy")
    val sentBy: String,
    @SerializedName("sentTo")
    val sentTo: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("type")
    val type: String
)