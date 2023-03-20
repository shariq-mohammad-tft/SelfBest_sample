package com.tft.selfbest.models.mycourse

import com.google.gson.annotations.SerializedName

data class EnrolledCourseResponse(
    @SerializedName("Courses")
    val course: List<EnrolledCourse>
)