package com.example.chat_feature.data

import com.google.gson.annotations.SerializedName

data class socketIoRequest(
    @SerializedName("message")
    val message: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("OrganisationId")
    val organisationId: Int
)
