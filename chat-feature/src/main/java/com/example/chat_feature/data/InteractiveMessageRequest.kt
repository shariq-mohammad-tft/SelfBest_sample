package com.example.chat_feature.data


import com.example.chat_feature.utils.Constants
import com.example.chat_feature.utils.EventType
import com.google.gson.annotations.SerializedName

data class InteractiveMessageRequest(

    @SerializedName("sender_id")
    val senderId: String = Constants.USER_ID,

    @SerializedName("event_message")
    val message: String,

    @SerializedName("event_name")
    val eventName: String,

    @SerializedName("event_type")
    val eventType: String = EventType.INTERACTIVE.value,
){
    fun convertToMessage(): Message {
        return Message(
            senderId = Constants.USER_ID,
            receiverId = Constants.BOT_ID,
            message = message,
            buttons = emptyList(),
        )
    }
}