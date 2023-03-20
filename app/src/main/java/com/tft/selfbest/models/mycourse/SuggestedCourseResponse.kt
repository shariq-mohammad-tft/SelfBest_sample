package com.tft.selfbest.models.mycourse

import com.google.gson.annotations.SerializedName

data class SuggestedCourseResponse(
    @SerializedName("Courses")
    val course: List<SuggestedCourse>
)