package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ChangeRequestStatus (
    @SerializedName("Organisation")
    val org : Boolean,
    @SerializedName("Requests")
    val requests: List<RequestStatus>
    )


data class RequestStatus(
    @SerializedName("Status")
    val status: String,
    @SerializedName("UserID")
    val userId: Int,
    @SerializedName("UserType")
    val userType: String
)