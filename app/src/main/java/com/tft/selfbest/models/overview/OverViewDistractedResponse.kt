package com.tft.selfbest.models.overview

import com.google.gson.annotations.SerializedName

data class OverViewDistractedResponse(
    @SerializedName("Total Time Saved")
    val totalDistractedTime: String
)