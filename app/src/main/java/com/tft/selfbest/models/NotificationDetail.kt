package com.tft.selfbest.models

import com.github.mikephil.charting.components.Description
import com.google.gson.annotations.SerializedName

data class NotificationDetail (
    @SerializedName("Description")
    val description: String
        )