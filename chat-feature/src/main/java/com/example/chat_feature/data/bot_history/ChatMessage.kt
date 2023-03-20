package com.example.chat_feature.data.bot_history

import com.example.chat_feature.data.Message
import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("chat_json")
    val chatJson: ChatJson,

    @SerializedName("id")
    val id: Int,

    @SerializedName("message_status")
    val message_status: Boolean,
) {
    fun convertToMessage(): Message {
        return chatJson.let { chatMap ->
            Message(
                senderId = chatMap.sender_id.toString(),
                receiverId = chatMap.sentTo.toString(),
                message = chatMap.message.toString(),
                buttons = chatMap.buttons,
                eventMessage = chatMap.event_message.toString()
            )
        }

    }
}