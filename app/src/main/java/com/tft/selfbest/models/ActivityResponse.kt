package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ActivityResponse(
    @SerializedName("Activities")
    val activities: List<ActivitySingleResponse>,
    @SerializedName("Interval")
    val interval: Int,
    @SerializedName("Observations")
    val observations: Any,
    @SerializedName("Progress")
    val progress: List<SubProgressResponse>?,
    @SerializedName("IsPaused")
    val isPaused:Boolean,
    @SerializedName("CategoryList")
    val categoryList: Any
)

data class SubProgressResponse(
    @SerializedName("x-axis-lable")
    val xAxisLabel: Int,
    @SerializedName("points")
    val point: Float
)

data class ActivitySingleResponse(
    @SerializedName("Category")
    val category: String,
    @SerializedName("Duration")
    val duration: Double,
    @SerializedName("EndAt")
    val endAt: String,
    @SerializedName("StartAt")
    val startAt: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Url")
    val url: String
)