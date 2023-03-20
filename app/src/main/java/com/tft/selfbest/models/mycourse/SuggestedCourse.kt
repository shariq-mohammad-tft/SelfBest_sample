package com.tft.selfbest.models.mycourse

import com.google.gson.annotations.SerializedName

data class SuggestedCourse(
    @SerializedName("CourseId")
    val courseId: Int,
    @SerializedName("Duration")
    val duration: String,
    @SerializedName("Image")
    val courseImageUrl: String,
    @SerializedName("Modules")
    val modules: Int,
    @SerializedName("Price")
    val price: Int,
    @SerializedName("Provider")
    val provider: String,
    @SerializedName("Title")
    val courseName: String,
    @SerializedName("URL")
    val URL: String,
)