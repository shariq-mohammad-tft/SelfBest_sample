package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ObservationsResponse(
    @SerializedName("Categories")
    val categories: Any,
    @SerializedName("Level")
    val level: Int,
    @SerializedName("Managers")
    val managers: List<String>,
    @SerializedName("Observations")
    val observations: List<ObservationDetail>,
    @SerializedName("TaskCompleted")
    val taskCompleted: Int,
)