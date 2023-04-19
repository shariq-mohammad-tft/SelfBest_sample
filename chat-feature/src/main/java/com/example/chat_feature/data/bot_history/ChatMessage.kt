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
            val id=if (chatMap.sender_id==null) chatMap.sentBy.toString() else chatMap.sender_id.toString()
            Message(
                senderId = id,
                receiverId = chatMap.sentTo.toString(),
                message = chatMap.message.toString(),
                buttons = chatMap.buttons,
                eventMessage = chatMap.event_message.toString(),
                timeStamp = chatMap.timestamp,
                links = chatMap.links
            )
        }

    }
}