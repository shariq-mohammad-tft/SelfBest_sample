package com.example.chat_feature.data.expert_user

import com.example.chat_feature.data.response.expert_chat.ExpertChatResponse
import com.google.gson.annotations.SerializedName

data class ChatBetweenUserAndExpertResponse(
    @SerializedName("chat_messages")
    val chatMessages: List<ExpertChatResponse>,
    @SerializedName("id")
    val messageId: Int,
    @SerializedName("message_status")
    val messageStatus: String

)
