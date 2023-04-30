package com.tft.selfbest.models

data class DurationFilter(
    val duration: String = "daily",
    val platform: String = "Mobile",
    val startDate: String? = null,
    val endDate: String?= null
)
