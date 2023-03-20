package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ChangeSkillRequestStatus (
    @SerializedName("ReplaceSkill")
    val replaceSkill: String,
    @SerializedName("SkillId")
    val skillId: List<Int>,
    @SerializedName("Status")
    val status: String
)