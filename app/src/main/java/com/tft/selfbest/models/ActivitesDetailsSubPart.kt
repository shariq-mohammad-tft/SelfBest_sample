package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ActivitiesDetailsSubPart(
    @SerializedName("Category")
    val Cate :String,
    @SerializedName("Duration")
    val duration : Double,
)
