package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ObservationDetail(
    @SerializedName("Url")
    val url: String,
    @SerializedName("Duration")
    val duration: Double,
    @SerializedName("Type")
    var type: String,
    @SerializedName("Category")
    val category: String,
    @SerializedName("EndAt")
    val endAt: String,
    @SerializedName("StartAt")
    val startAt: String,
    @Transient
    var isSelected: Boolean = false,
    @Transient
    var comment: String,
    @Transient
    var modifiedDuration: Double
)