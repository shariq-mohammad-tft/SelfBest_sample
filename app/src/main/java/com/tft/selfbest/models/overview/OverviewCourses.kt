package com.tft.selfbest.models.overview

import com.google.gson.annotations.SerializedName

data class OverviewCourses(
    @SerializedName("Courses")
    val allCourses: List<CourseDetail>,
)