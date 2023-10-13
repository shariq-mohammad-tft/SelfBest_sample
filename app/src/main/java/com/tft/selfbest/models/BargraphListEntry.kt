package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class BargraphListEntry(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Category")
    val category: String,
    @SerializedName("Duration")
    val duration: String
)
