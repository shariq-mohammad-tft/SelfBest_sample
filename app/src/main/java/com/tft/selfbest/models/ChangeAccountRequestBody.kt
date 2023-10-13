package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ChangeAccountRequestBody (
    @SerializedName("action")
    val action: String,
    @SerializedName("request")
    val request: List<AccountRequestBody>
    )


data class AccountRequestBody(
    @SerializedName("Email")
    val email: String,
    @SerializedName("Type")
    val type: String
)