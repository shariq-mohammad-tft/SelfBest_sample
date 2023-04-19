package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class ActivityLogValues(
    @SerializedName("Url")
    val url: String?,
    @SerializedName("Type")
    val type: String?,
    @SerializedName("Duration")
    val duration: Double
)

class ActivityLogValuesHigh(
    @SerializedName("DefaultList")
    val defaultList: DefaultCategory,
    @SerializedName("FocusTime")
    val focusTime: Double = 0.0,
    @SerializedName("Logs")
    val logs: List<ActivityLogValues>? = null,
    @SerializedName("SelectedCategories")
    val selectedCategories: List<SelectedCategory>,
    @SerializedName("TimeSaved")
    val timeSaved: Double = 0.0,
    @SerializedName("TotalTimeDistracted")
    val distractedTime: Double = 0.0

)

data class SelectedCategory(
    @SerializedName("Category")
    val category: String,
    @SerializedName("Duration")
    val duration: Double = 0.0,
    @SerializedName("Selected")
    val selected: Boolean

): java.io.Serializable

//data class ActivityLogResponse(
//    @SerializedName("Logs")
//    val logs: List<ActivityLogValues>
//)

//data class ActivityLogResponse(
//    @SerializedName("Logs")
//    val logs: ActivityLogValuesHigh
//)

data class DefaultCategory(
    @SerializedName("Break")
    val breakTime: Double = 0.0,
    @SerializedName("Distraction")
    val distraction: Double = 0.0,
    @SerializedName("Others")
    val others: Double = 0.0
)

