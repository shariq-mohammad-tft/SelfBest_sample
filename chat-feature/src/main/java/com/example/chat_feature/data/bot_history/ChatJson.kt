package com.example.chat_feature.data.bot_history

import com.example.chat_feature.data.response.Button
import com.google.gson.annotations.SerializedName

data class ChatJson(
    @SerializedName("buttons") val buttons: List<Button>?= listOf(
        Button("1001", "Yes", "Yes", "Yes"),
        Button("1002", "No", "No", "No"),
    ),

    @SerializedName("channel_id") val channel_id: String?=null,

    @SerializedName("event_message") val event_message: String?=null,

    @SerializedName("event_name") val event_name: String?=null,

    @SerializedName("event_type") val event_type: String?=null,

    @SerializedName("full_name") val full_name: String?=null,

    @SerializedName("links") val links: List<Any>?=null,

    @SerializedName("message") val message: String?=null,

    @SerializedName("query_id") val query_id: String,

    @SerializedName("query_name") val query_name: String?=null,

    @SerializedName("sender_id") val sender_id: Int?=null,

    @SerializedName("sentBy") val sentBy: String?=null,

    @SerializedName("sentTo") val sentTo: Any?=null,

    @SerializedName("timestamp") val timestamp: String?=null,

    @SerializedName("type") val type: String?=null
)
