package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ManagerEmails(
    @SerializedName("TeamID") val teamId: Int,
    @SerializedName("UserID") val userId: Int,
    @SerializedName("Email") val email: String,
    @SerializedName("FirstName") val firstName: String,
)