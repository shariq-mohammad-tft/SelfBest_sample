package com.example.chat_feature.data.bot_history

import com.google.gson.annotations.SerializedName

data class BotHistoryResponse(
    @SerializedName("chat_messages")
    val chatMessages: List<ChatMessage>
)
