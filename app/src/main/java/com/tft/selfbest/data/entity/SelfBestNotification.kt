package com.tft.selfbest.data.entity


import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "learning_hr", primaryKeys = ["url"])
data class SelfBestNotification(
    @SerializedName("Url")
    val url: String,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("Notification")
    val notification: String?,
    @SerializedName("NotificationID")
    val notificationID: Int?,
    @SerializedName("RewardActivityID")
    val rewardActivityID: Int?,
    @SerializedName("StartedLearningHour")
    val startedLearningHour: Boolean?,
    @SerializedName("StartedWorkingHour")
    val startedWorkingHour: Boolean?,
    @SerializedName("Timestamp")
    val timestamp: String?,
    @SerializedName("UserId")
    val userId: Int?
)