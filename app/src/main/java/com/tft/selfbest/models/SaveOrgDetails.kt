package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class SaveOrgDetails(
    @SerializedName("CompanyDomain")
    val companyDomain: String,
    @SerializedName("CompanyLinkedIn")
    val companyLinkedIn: String,
    @SerializedName("CompanyName")
    val companyName: String,
    @SerializedName("CompanySize")
    val companySize: Long,
    @SerializedName("CompanyUrl")
    val companyUrl: String,
    @SerializedName("Distractions")
    val distractions: List<String>,
    @SerializedName("Email")
    val email: String,
    @SerializedName("UserId")
    val userId: Int
)
