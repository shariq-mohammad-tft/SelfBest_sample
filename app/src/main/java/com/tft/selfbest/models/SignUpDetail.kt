package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

data class SignUpDetail (
//    @SerializedName("Experience")
//    val experience: Int,
    @SerializedName("LinkedProfileUrl")
    val linkedProfileUrl: String,
    @SerializedName("Location")
    val location: String,
//    @SerializedName("Occupation")
//    val occupation: String,
    @SerializedName("Skills")
    val skills: LinkedTreeMap<String, Int>,
//    @SerializedName("WorkEndTime")
//    val workEndTime: String,
//    @SerializedName("WorkStartTime")
//    val workStartTime: String,
//    @SerializedName("WorkingDays")
//    val workingDays: List<String>
    )
