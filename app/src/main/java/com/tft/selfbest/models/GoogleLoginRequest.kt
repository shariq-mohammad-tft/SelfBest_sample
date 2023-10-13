package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class GoogleLoginRequest(
    @SerializedName("Type")
    val Type: String,
    @SerializedName("idtoken")
    val idToken: String

)
