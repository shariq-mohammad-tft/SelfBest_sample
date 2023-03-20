package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class DeviceObservationRequest(
    @SerializedName("ApplicationType")
    val applicationType: String,
    @SerializedName("LastUrl")
    val lastUrl: String?,
    @SerializedName("LastStartTime")
    val lastStartTime: String,
    @SerializedName("LastEndTime")
    val lastEndTime: String,
    @SerializedName("CurrentUrl")
    val currentUrl: String?,
    @SerializedName("CurrentStartTime")
    val currentStartTime: String
)