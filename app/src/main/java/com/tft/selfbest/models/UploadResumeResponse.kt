package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class UploadResumeResponse(
    @SerializedName("skills")
    val skills:List<String>
)
