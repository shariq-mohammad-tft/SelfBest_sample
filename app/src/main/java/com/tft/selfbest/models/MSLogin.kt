package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body

data class MSLogin(
    @SerializedName("idtoken")
    val idtoken: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("reactivate")
    val reactivate: Boolean
)
