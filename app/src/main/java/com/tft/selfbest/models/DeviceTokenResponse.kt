package com.tft.selfbest.models

import com.google.firebase.messaging.RemoteMessage
import com.google.gson.annotations.SerializedName

data class DeviceTokenResponse(
    @SerializedName("message")
    val message: String
)
