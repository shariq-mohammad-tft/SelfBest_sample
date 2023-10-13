package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class GoHourResponse(
    @SerializedName("Isactive")
    val isActive: Boolean,
    @SerializedName("Ispaused")
    val isPause: Boolean
)