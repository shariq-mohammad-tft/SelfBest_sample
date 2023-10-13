package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class GoHourStatusResponse(
    @SerializedName("ActiveLogs")
    val activeLogs: List<ObservationTimeLogs>,
    @SerializedName("IsActive")
    val isActive: Boolean,
    @SerializedName("IsPaused")
    val isPause: Boolean,
    @SerializedName("PauseLogs")
    val pauseLogs: List<ObservationTimeLogs>
)

data class ObservationTimeLogs(
    @SerializedName("EndTime")
    val endTime: String,
    @SerializedName("StartTime")
    val startTime: String
)
