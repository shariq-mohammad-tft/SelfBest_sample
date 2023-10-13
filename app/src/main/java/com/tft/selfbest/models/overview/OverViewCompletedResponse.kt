package com.tft.selfbest.models.overview

import com.google.gson.annotations.SerializedName

data class OverViewCompletedResponse(
    @SerializedName("CodeTime") val codeTime: Float,
    @SerializedName("LearningTime") val learningTime: Float,
    @SerializedName("DocumentationTime")
    val documentationTime: Float
)