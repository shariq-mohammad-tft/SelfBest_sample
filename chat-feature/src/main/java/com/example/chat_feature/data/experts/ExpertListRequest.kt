package com.example.chat_feature.data.experts

import com.example.chat_feature.data.response.expert_chat.ChatJson
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

data class BotUnseenCountRequest(
    @SerializedName("type")
    val type:String="bot_unseen_count",
    @SerializedName("sentBy")
    val sentBy:String
)
data class TotalUnseenCountRequest(
    @SerializedName("type")
    val type:String="unseen_count",
    @SerializedName("sentBy")
    val sentBy:String
)

data class BotSeenRequest(
    @SerializedName("sentBy")
    val sentBy: String
)

data class ChatData (

    @SerializedName("bot_message_count" ) var botMessageCount : Int? = null

)