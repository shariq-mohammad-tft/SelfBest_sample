package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class AddDistraction(
    @SerializedName("Url")
    val url: String,
    @SerializedName("Name")
    val name: String
)