package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class GetChartData(
    @SerializedName("x-axis-lale")
    val xAx:String?,
    @SerializedName ("poins")
    val point:Double,
    @SerializedName("Progress")
    val Progress:  List<ProgressDetails>,
    @SerializedName ("Activities")
    val Activity :List<ActivitiesDetails>
)
