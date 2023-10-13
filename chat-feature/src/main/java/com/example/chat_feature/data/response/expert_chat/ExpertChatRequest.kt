package com.example.chat_feature.data.response.expert_chat


import com.example.chat_feature.data.Message
import com.example.chat_feature.utils.Constants
import com.google.gson.annotations.SerializedName
import java.time.Instant

data class ExpertChatRequest(
    @SerializedName("message")
    val message: String,
    @SerializedName("sentBy")
    val senderId: String,
    @SerializedName("sentTo")
    val receiverId: String,
    @SerializedName("type")
    val type: String = "chat",
    @SerializedName("queryId")
    val queryId:String,
    @SerializedName("image")
    val imageLink:String?=null

) {
    /*fun convertToExpertChatResponse() = ExpertChatResponse(
        senderId = senderId,
        receiverId = receiverId,
        message = message,
        queryId =queryId
    )*/
    fun convertToChatJson() = ChatJson(
        sentBy = senderId,
        sentTo = receiverId,
        message = message,
        queryId =queryId,
        image = imageLink

    )
}