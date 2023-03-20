package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class Skills (
    @SerializedName("Skills")
    val skills: List<String>
        )