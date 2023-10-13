package com.tft.selfbest.models.notifications

import com.google.gson.annotations.SerializedName
import com.tft.selfbest.models.NotificationDetail

data class NotificationResponse(
    @SerializedName("NotificationList") val notificationList: List<NotificationDetail>,
)