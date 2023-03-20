package com.tft.selfbest.models


import com.google.gson.annotations.SerializedName

data class DistractedObservationResponse(
    @SerializedName("Flag")
    val flag: Boolean?,
    @SerializedName("Response")
    val response: String?,
    @SerializedName("UserId")
    val userId: Int?
)