package com.example.chat_feature.data.response

import com.google.gson.annotations.SerializedName

data class BotUnseenCountResponse(
    @SerializedName("bot_message_count")
    val bot_unseen_count:Int?=null
)
