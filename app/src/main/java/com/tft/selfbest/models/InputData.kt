package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class InputData(
    @SerializedName("GetGoHourId")
    var getGoHourId:Int,
    @SerializedName("Observations")
    var observations:List<Observations>,
    @SerializedName("Rating")
    var rating:Int
)

data class Observations(
    @SerializedName("Category")
    var category:String,
    @SerializedName("Duration")
    var duration:Double,
    @SerializedName("Type")
    var type:String,
    @SerializedName("Url")
    var url:String
)
