package com.tft.selfbest.models


import com.google.gson.annotations.SerializedName

data class LogedInData(
    @SerializedName("AccessToken")
    val accessToken: String,
    @SerializedName("Approved")
    val approved: Int?,
    @SerializedName("Email")
    val email: String??,
    @SerializedName("FirstName")
    val firstName: String,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("LastName")
    val lastName: String?,
    @SerializedName("Redirect")
    val redirect: String?,
    @SerializedName("RefreshToken")
    val refreshToken: String,
    @SerializedName("Roles")
    val roles: List<String>,
    @SerializedName("UserName")
    val userName: String?,
    @SerializedName("new_user")
    val isNewUser: Boolean
){
    val fullName: String
        get() = firstName + lastName
}