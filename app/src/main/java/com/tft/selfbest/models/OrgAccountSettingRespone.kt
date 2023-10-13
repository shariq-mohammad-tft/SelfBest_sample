package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class OrgAccountSettingResponse(
    @SerializedName("Email")
    val email: String,
    @SerializedName("type")
    val type: String
)
