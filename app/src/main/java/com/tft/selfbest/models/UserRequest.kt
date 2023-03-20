package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class UserRequest (
    @SerializedName("Email")
    val email: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Status")
    val status: String,
    @SerializedName("Url")
    val url: String,
    @SerializedName("UserID")
    val userid: Int,
    @SerializedName("UserType")
    val userType: String
    )