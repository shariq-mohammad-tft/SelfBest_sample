package com.example.chat_feature.data

import com.google.gson.annotations.SerializedName

data class SuggestionResponse(
    @SerializedName("app")
    val app: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("intent")
    val intent: String
)
