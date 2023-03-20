package com.example.chat_feature.data.experts

import com.google.gson.annotations.SerializedName

data class Expert(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("last_message")
    val lastMessage: LastMessage?,
    @SerializedName("query_text")
    val queryText: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("query_id")
    val queryId:String,
    @SerializedName("useen_count")
    val unSeenCount:Int,
    @SerializedName("query_status")
    val queryStatus:Boolean
)
