package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ProfileImageResponse(
    @SerializedName("Image")
    val imageUrl: String,
)