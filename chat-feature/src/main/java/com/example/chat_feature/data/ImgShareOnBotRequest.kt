package com.example.chat_feature.data

import com.example.chat_feature.utils.Constants
import java.io.File

data class ImgShareOnBotRequest(
    val file: File,
    val event_type: String,
    val sender_id: String,
    val event_message: String
) : MessageRequest {
    fun convertToMessage(): Message {
        return Message(
            senderId = sender_id.toString(),
            receiverId = Constants.BOT_ID,
            message = event_message.toString(),
            buttons = null,
            file = file.toString()
        )
    }
}