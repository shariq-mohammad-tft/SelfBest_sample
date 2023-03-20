package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class PointGraphResponse (
    @SerializedName("x-axis-lable")
    val xAxisLabel: String,
    @SerializedName("points")
    val points: Int
    )