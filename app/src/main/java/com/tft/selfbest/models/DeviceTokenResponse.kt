package com.tft.selfbest.models

import com.google.firebase.messaging.RemoteMessage
import com.google.gson.annotations.SerializedName

data class DeviceTokenResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)
