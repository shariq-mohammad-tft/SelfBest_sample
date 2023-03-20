package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class DeleteAccountResponse(
    @SerializedName("Email")
    val email: String,
    @SerializedName("Type")
    val type: String
)