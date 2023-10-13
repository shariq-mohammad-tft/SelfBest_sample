package com.example.chat_feature.data.bot_history

import com.google.gson.annotations.SerializedName

data class Button(
    @SerializedName("key") val key: String,

    @SerializedName("name") val name: String,

    @SerializedName("text") val text: String,

    @SerializedName("value") val value: String
)
