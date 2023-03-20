package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class GoogleLoginRequest2(
    @SerializedName("Type")
    val Type: String,
    @SerializedName("idtoken")
    val idToken: String,
    @SerializedName("reactivate")
    val reactivate: Boolean
)
