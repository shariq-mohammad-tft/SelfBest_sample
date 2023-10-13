package com.tft.selfbest.models.overview

import com.google.gson.annotations.SerializedName

data class OverViewActivityResponse(
    @SerializedName("Code")
    val code: List<String>,
    @SerializedName("Documentation")
    val documentation: List<String>,
    @SerializedName("GetStarted")
    val isGetStarted: Boolean,
    @SerializedName("Others")
    val others: List<String>
)