package com.tft.selfbest.models.mycourse

import com.google.gson.annotations.SerializedName

data class FilterSearchCourse(
    @SerializedName("Keyword")
    var Keyword: String,
    @SerializedName("Length")
    var length: ArrayList<String>,
    @SerializedName("MinAmount")
    var minAmount: Int,
    @SerializedName("MaxAmount")
    var maxAmount: Int,
    @SerializedName("Providers")
    var providers: ArrayList<String>
)