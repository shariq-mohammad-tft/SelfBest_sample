package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ToggleDistraction (
    @SerializedName("id")
    val id : Int,
    @SerializedName("state")
    val state : Boolean?
)