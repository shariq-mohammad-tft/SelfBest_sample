package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class CurrentDistraction (
    @SerializedName("id")
    val id : Int,
    @SerializedName("url")
    val url : String,
    @SerializedName("state")
    val state : Boolean
)