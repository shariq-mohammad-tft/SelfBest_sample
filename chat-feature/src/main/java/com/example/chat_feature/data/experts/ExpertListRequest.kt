package com.example.chat_feature.data.experts

import com.google.gson.annotations.SerializedName

data class ExpertListRequest(
    @SerializedName("sentBy")
    val senderId: String
)

data class ChatBetweenUserAndExpertRequest(
    @SerializedName("sentBy")
    val senderId: String,
//    @SerializedName("sentTo")
//    val receiverId: String,
    @SerializedName("queryId")
    val queryId:String
)