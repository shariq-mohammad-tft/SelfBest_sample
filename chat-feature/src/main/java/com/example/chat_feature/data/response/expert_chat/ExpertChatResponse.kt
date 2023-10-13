package com.example.chat_feature.data.response.expert_chat


import com.example.chat_feature.utils.Constants
import com.google.gson.annotations.SerializedName
import java.util.*

data class ExpertChatResponse(
    @SerializedName("key")
    val key: String = UUID.randomUUID().toString(),
    /* @SerializedName("message")
     val message: String,
     @SerializedName("sentBy")
     val senderId: String,
     @SerializedName("sentTo")
     val receiverId: String,
     @SerializedName("type")
     val type: String = Constants.CHAT,
     @SerializedName("timestamp")
     val timestamp: String = System.currentTimeMillis().toString(),
     @SerializedName("query_Id")
     val queryId:String*/
    @SerializedName("channel_id") val channel_id: String?=null,

    @SerializedName("event_message") val event_message: String?=null,

    @SerializedName("event_name") val event_name: String?=null,

    @SerializedName("event_type") val event_type: String?=null,

    @SerializedName("full_name") val full_name: String?=null,

    @SerializedName("links") val links: List<Any>?=null,

    @SerializedName("message") val message: String?=null,

    @SerializedName("query_id") val queryId: String,

    @SerializedName("query_name") val query_name: String?=null,

    @SerializedName("sender_id") val senderId: String,

    @SerializedName("sentBy") val sentBy: String?=null,

    @SerializedName("sentTo") val receiverId: Any?=null,

    @SerializedName("timestamp") val timestamp: String?=null,

    @SerializedName("type") val type: String?=null

)