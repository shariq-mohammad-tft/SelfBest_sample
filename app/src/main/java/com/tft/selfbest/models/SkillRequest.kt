package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class SkillRequest (
    @SerializedName("Skills")
    val skills: List<SkillResponse>
)

data class SkillResponse(
    @SerializedName("Email")
    val email: String,
    @SerializedName("ID")
    val id: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("OrganisationId")
    val organisationId: Int,
    @SerializedName("RequesterDate")
    val requesterDate: String,
    @SerializedName("RequesterId")
    val requesterId: Int,
    @SerializedName("Skill")
    val skill: String,
    @SerializedName("Status")
    val status: String
)