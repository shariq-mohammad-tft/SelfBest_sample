package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ProgressDetails(
    @SerializedName("x-axis-lable")
    val xis:Int,
    @SerializedName("points")
    val point:Double
)