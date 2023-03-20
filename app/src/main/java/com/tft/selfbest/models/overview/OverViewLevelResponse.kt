package com.tft.selfbest.models.overview

import com.google.gson.annotations.SerializedName

data class OverViewLevelResponse(
    @SerializedName("CurrentLevelProgress")
    val currentProgress: Double,
    @SerializedName("Level")
    val level: Int
)