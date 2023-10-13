package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ObservationDetailSubPart (
    @SerializedName("Url")
    val url: String?,
    @SerializedName("Duration")
    val duration: Double
)