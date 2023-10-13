package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.*

data class GetGoHourResponse (
    @SerializedName("GetStarted")
    val getStarted : Boolean,
    @SerializedName("IsActive")
    val isActive: Boolean,
    @SerializedName("IsPaused")
    val isPaused : Boolean,
    @SerializedName("Ended")
    val ended : Boolean,
    @SerializedName("PauseStartTime")
    val pauseStartTime : String,
    @SerializedName("TotalPauseTime")
    val totalPauseTime : Long,
    @SerializedName("Id")
    val id : Int,
    @SerializedName("TimeInterval")
    val timeInterval : Int,
    @SerializedName("ResetsLeft")
    val resetsLeft : Int,
    @SerializedName("UserId")
    val userId : Int,
    @SerializedName("Hydrated")
    val hydrated : Int,
    @SerializedName("HydrateNotifed")
    val hydratedNotifed : Int,
    @SerializedName("StartTime")
    val startTime : String,
    @SerializedName("EndTime")
    val endTime : String,
    @SerializedName("CreatedAt")
    val createdAt : String,
    @SerializedName("TotalTime")
    val totalTime : Long,
    @SerializedName("Rating")
    val rating : Int,
    @SerializedName("EndNotified")
    val endNotified : Boolean,
//    @SerializedName("FocusNotified")
//    val focusNotifies : Int,
//    @SerializedName("FocusNotifiedTime")
//    val focusNotifiedTime : String,
//    @SerializedName("GoingToEndNotified")
//    val goingToEndNotified : Boolean,
//    @SerializedName("GetGoHourStartedNotified")
//    val getGoHourStartedNotified: Boolean,
//    @SerializedName("DistractionNotifiedTime")
//    val distractionNotifiedTime: String

)
