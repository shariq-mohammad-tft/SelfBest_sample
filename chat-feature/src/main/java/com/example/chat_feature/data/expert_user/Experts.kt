package com.example.chat_feature.data.expert_user

import com.google.gson.annotations.SerializedName

data class Experts(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("sent_to")
    val sentTo: String
)
