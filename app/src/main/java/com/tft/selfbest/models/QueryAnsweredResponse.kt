package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class QueryAnsweredResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("relevance")
    val relevance: String,
    @SerializedName("redirect_to")
    val redirect_to: String
)