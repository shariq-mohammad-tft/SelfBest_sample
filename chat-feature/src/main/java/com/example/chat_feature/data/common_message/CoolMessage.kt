package com.example.chat_feature.data.common_message

import java.util.*


sealed class CoolMessage {
    data class PlainMessage(
        val messageId: String = UUID.randomUUID().toString(),
        val senderId: Int = (1..2).random(),
        val receiverId: Int,
        val message: String,
    ) : CoolMessage()

    data class ImageMessage(
        val messageId: String = UUID.randomUUID().toString(),
        val senderId: Int = (1..2).random(),
        val receiverId: Int,
        val message: String? = null,
        val imageUrl: String,
        val isFilePath: Boolean = false,
    ) : CoolMessage()


    data class ButtonMessage(
        val messageId: String = UUID.randomUUID().toString(),
        val senderId: Int = (1..2).random(),
        val receiverId: Int,
        val buttons: List<String>? = listOf("Hello", "World"),
        val message: String? = null,
    ) : CoolMessage()
}

class A {
    val plainMessage: CoolMessage = CoolMessage.PlainMessage(
        senderId = 1,
        receiverId = 2,
        message = "Plain Message",
    )
    val buttonMessage: CoolMessage = CoolMessage.ButtonMessage(
        senderId = 1,
        receiverId = 2,
        message = "Plain Message",
        buttons = listOf("Hello", "World"),
    )
    val imageMessage: CoolMessage = CoolMessage.ImageMessage(
        senderId = 1,
        receiverId = 2,
        imageUrl = "http://some_url.com",
        message = "Optional caption message",
        isFilePath = false
    )

    // OR

    val imageMessage2: CoolMessage = CoolMessage.ImageMessage(
        senderId = 1,
        receiverId = 2,
        imageUrl = "file_path_will_be_here",
        message = "Optional caption message",
        isFilePath = true
    )
}