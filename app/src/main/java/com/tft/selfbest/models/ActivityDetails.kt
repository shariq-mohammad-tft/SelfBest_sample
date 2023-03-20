package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ActivitiesDetails(
    @SerializedName("Url")
    val url: String,
    @SerializedName("Category")
    val Cate :String,
    @SerializedName("Duration")
    val duration : Double,
    @SerializedName("Type")
    var type: String,
)
