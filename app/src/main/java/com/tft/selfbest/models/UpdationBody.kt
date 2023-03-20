package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class UpdationBody (
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
        )