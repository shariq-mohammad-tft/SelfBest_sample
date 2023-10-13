package com.example.chat_feature.data.response


import com.google.gson.annotations.SerializedName

data class Button(
    @SerializedName("name")
    val id: String,

    @SerializedName("key")
    val key: String,

    @SerializedName("text")
    val text: String,

    @SerializedName("value")
    val value: String
)