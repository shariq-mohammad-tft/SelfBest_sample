package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class QueryResponse (
    @SerializedName("id")
    val id: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("query_status")
    val query_status: Boolean,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("redirect_to")
    val redirect_to: String
    )

data class QueryTableResponse(
    @SerializedName("hours_saved")
    val hours_saved: String,
    @SerializedName("query_data")
    val query_data: List<QueryResponse>,
    @SerializedName("hours_saved_min")
    val hours_saved_min: Int
    )