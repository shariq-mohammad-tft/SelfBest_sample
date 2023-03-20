package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ActivityTimelineResponse (
    @SerializedName("DistractionTime")
    val distractionTime: Double = 0.0,
    @SerializedName("Duration")
    val duration: Double,
    @SerializedName("EndTime")
    val endTime: String,
    @SerializedName("FocusTime")
    val focusTime: Double = 0.0,
    @SerializedName("Points")
    val points: Int,
    @SerializedName("StartTime")
    val startTime: String
    )

//sealed class Duration<T>(val value: T) {
//    class IntValue(value: Int) : Duration<Int>(value)
//    class DoubleValue(value: Double) : Duration<Double>(value)
//}

