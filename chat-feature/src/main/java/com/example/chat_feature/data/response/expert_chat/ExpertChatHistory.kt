package com.example.chat_feature.data.response.expert_chat

import com.google.gson.annotations.SerializedName

//this  model class is responsible for getting history between user and expert
data class ExpertChatHistory(
    val chat_messages: List<ChatMessage>,
)

data class ChatMessage(
    @SerializedName("id") val id: Int,
    @SerializedName("chat_json") val chatJson: ChatJson,
    @SerializedName("message_status") val messageStatus: Boolean,
)

data class ChatJson(
    @SerializedName("type") val type: String? = null,
    @SerializedName("sentBy") val sentBy: String?,
    @SerializedName("sentTo") val sentTo: String?,
    @SerializedName("message") val message: String? = null,
    @SerializedName("queryId") val queryId: String? = null,
    @SerializedName("buttons") val buttons: List<Any>? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("image") val image: String? = null,
    private var _isFilePath: Boolean = false,
    private var _progress: Int = 100,
) {
    var isFilePath: Boolean
        get() = _isFilePath
        set(value) {
            _isFilePath = value
        }

    var progress: Int
        get() = _progress
        set(value) {
            _progress = value
        }
}