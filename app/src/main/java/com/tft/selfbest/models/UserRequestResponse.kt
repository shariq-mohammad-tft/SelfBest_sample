package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class UserRequestResponse(
    @SerializedName("UserID")
    val userID:Int,
    @SerializedName("Email")
    val emailID:String,
    @SerializedName("Status")
    val status:String,
    @SerializedName("Name")
    val name:String,
    @SerializedName("Url")
    val url:String,
    @SerializedName("UserType")
    val userType:String,
    )
