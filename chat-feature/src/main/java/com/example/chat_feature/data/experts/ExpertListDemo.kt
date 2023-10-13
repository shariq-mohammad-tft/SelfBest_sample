package com.example.chat_feature.data.experts

import com.google.gson.annotations.SerializedName

data class ExpertListDemo(
    @SerializedName("experts_list")
    val expertsList: List<Expert>
)
