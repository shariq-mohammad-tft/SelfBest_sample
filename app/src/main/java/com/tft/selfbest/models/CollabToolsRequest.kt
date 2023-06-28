package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class CollabToolsRequest(
    @SerializedName("Calendar")
    val calendar: String,
    @SerializedName("SelectedBot")
    val selectedBot: String,
    @SerializedName("SlackWorkspaceID")
    val slackId: String,
    @SerializedName("SolutionPoint")
    val solutionPoint: Boolean,
    @SerializedName("WorkDen")
    val workDen: Boolean
)
