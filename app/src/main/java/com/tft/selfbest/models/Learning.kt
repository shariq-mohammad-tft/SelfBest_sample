package com.tft.selfbest.models


import com.google.gson.annotations.SerializedName

data class Learning(
    @SerializedName("CreatedAt")
    val createdAt: String?,
    @SerializedName("Day")
    val day: String?,
    @SerializedName("EndTime")
    val endTime: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("StartTime")
    val startTime: String?,
    @SerializedName("Type")
    val type: String?,
    @SerializedName("UpdatedAt")
    val updatedAt: String?,
    @SerializedName("Userid")
    val userid: Int?
)