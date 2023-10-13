package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.*

data class StartBody (
    @SerializedName("StartTime")
    val StartTime : String,
    @SerializedName("Hydrated")
    val Hydrated : Int
    )