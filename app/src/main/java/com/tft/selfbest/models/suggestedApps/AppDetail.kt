package com.tft.selfbest.models.suggestedApps

import com.google.gson.annotations.SerializedName

data class AppDetail(
    @SerializedName("Name") val appName: String,
    @SerializedName("Type") val type: String,
)