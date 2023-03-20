package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class DistractedAppRequest(
        @SerializedName("Email")
        val email: String,
        @SerializedName("Password")
        val password: String
)