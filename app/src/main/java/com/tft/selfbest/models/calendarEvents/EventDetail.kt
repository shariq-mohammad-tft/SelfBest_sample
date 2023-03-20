package com.tft.selfbest.models.calendarEvents

import com.google.gson.annotations.SerializedName

data class EventDetail(
    @SerializedName("startDate") var startTime: String,
    @SerializedName("endDate") var endTime: String,
    @SerializedName("text") var eventTitle: String?,
    @Transient
    var isShowEvent: Boolean = true,
    @SerializedName("type") var type: String? = "",
    @SerializedName("recurrenceRule") var recurrenceRule: String? = "",
    @SerializedName("location") var location: String = "Asia/Calcutta",
)