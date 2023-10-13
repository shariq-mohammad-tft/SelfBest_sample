package com.tft.selfbest.models.mycourse

import com.google.gson.annotations.SerializedName

data class AddCourse(
    @SerializedName("CourseId")
    val courseId: String,
    @SerializedName("CourseName")
    val courseName: String,
    @SerializedName("CourseType")
    val courseType: String,
    @SerializedName("Courseurl")
    val courseUrl: String,
    @SerializedName("Duration")
    val duration: String,
    @SerializedName("Imageurl")
    val imageUrl: String,
    @SerializedName("Modules")
    val modules: Int
)