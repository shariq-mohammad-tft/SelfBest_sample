package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class DeviceTokenRequest(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_device_token")
    val token: String
)