package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class BarChartEntry(
    @SerializedName("Category")
    val cat: String,
    @SerializedName("Duration")
    val duration: Double
)
