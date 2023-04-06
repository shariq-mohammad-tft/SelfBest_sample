package com.tft.selfbest.models

import com.github.mikephil.charting.components.Description
import com.google.gson.annotations.SerializedName

data class NotificationDetail (
    @SerializedName("Clicked")
    val clicked: Boolean,
    @SerializedName("Content")
    val content: String,
    @SerializedName("Time")
    val time: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Url")
    val url: String,
    @SerializedName("Userid")
    val userId: Int,
    @SerializedName("id")
    val id: Long
)