package com.tft.selfbest.models


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("Profile")
    val profileData: ProfileData?
)