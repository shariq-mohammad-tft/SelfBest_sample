package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class OrgAddSkillRequest(
    @SerializedName("reqID")
    val reqId: Int,
    @SerializedName("skill")
    val skill: String
)
