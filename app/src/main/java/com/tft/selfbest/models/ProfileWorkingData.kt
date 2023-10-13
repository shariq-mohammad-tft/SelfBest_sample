package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ProfileWorkingData(
    @SerializedName("CreatedAt")
    val createdAt: String,
    @SerializedName("Day")
    var dayName: String,
    @SerializedName("EndHour")
    var endHour: Int,
    @SerializedName("EndMinute")
    var endMinute: Int,
    @SerializedName("StartHour")
    var startHour: Int,
    @SerializedName("StartMinute")
    var startMinute: Int,
    @SerializedName("Type")
    val type: String,
    @SerializedName("UpdatedAt")
    val updatedAt: String,
)