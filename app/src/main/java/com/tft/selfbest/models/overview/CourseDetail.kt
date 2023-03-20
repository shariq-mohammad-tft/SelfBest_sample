package com.tft.selfbest.models.overview

import com.google.gson.annotations.SerializedName

data class CourseDetail(
    @SerializedName("Name") val courseName: String,
    @SerializedName("Duration") val duration: String,
    @SerializedName("Modules") val modules: Int,
    @SerializedName("Modulescompleted") val completedModules: Int,
    @SerializedName("Status") val progressStatus: Int,
)