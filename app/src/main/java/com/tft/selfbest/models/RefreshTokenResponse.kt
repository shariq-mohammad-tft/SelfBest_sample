package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse (
    @SerializedName("AccessToken")
    val accessToken: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("FirstName")
    val firstName: String,
    @SerializedName("RefreshToken")
    val refreshToken: String,
    @SerializedName("UserID")
    val userid: Int
    )