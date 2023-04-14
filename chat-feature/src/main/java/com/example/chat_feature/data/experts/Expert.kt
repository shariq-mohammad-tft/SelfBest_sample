package com.example.chat_feature.data.experts

import com.google.gson.annotations.SerializedName

data class Expert(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("last_message")
    val lastMessage: LastMessage?,
    @SerializedName("query_name")
    val queryText: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("query_id")
    val queryId:String,
    @SerializedName("useen_count")
    val unSeenCount:Int,
    @SerializedName("query_status")
    val queryStatus:Boolean,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("expert_name")
    val expertName: String,
    @SerializedName("receiver_name")
    val receiverName: String,
    @SerializedName("updated_at")
    val updatedAt:String

)

//"query_id": 1418,
//"full_name": "swift query",
//"user_name": "Deepak Pandey",
//"expert_name": "Deepak Pandey",
//"query_text": "swift",
//"status": false,
//"query_status": true,
//"useen_count": 0,
//"created_at": "2023-03-23 08:53:49.722300+00:00",
//"updated_at": "2023-03-23 08:53:49.722321+00:00",
//"receiver_name": "Muskan Dhingra"
