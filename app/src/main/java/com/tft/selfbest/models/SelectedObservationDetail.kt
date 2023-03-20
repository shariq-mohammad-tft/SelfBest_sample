package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class SelectedObservationDetail(
    @SerializedName("Category")
    val category: String,
    @SerializedName("Duration")
    val duration: Double,
    @SerializedName("EndAt")
    val endAt: String,
    @SerializedName("StartAt")
    val startAt: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Url")
    val url: String,
    @SerializedName("checked")
    val isSelected: Boolean,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("modifiedDuration")
    val modifiedDuration: Double,
)