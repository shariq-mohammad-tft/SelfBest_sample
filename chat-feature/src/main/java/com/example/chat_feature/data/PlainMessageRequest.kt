package com.example.chat_feature.data


import com.example.chat_feature.utils.Constants
import com.example.chat_feature.utils.EventType
import com.google.gson.annotations.SerializedName

data class PlainMessageRequest(
    @SerializedName("sender_id")
    val senderId: String = Constants.USER_ID,

    @SerializedName("event_message")
    val message: String,

    @SerializedName("event_type")
    val eventType: String = EventType.MESSAGE.value,
) :MessageRequest{
    fun convertToMessage(): Message {
        return Message(
            senderId = senderId,
            receiverId = Constants.BOT_ID,
            message = message,
            buttons = emptyList(),
        )
    }
}