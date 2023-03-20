package com.tft.selfbest.models


import com.google.gson.annotations.SerializedName
import com.tft.selfbest.data.entity.DistractedApp

data class DistractionAppResponse(
    @SerializedName("List")
    val list: List<DistractedApp>?,
    @SerializedName("Userid")
    val userid: Int?
)