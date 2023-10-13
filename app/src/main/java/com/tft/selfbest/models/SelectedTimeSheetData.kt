package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class SelectedTimeSheetData(
    @SerializedName("EndDate")
    val endData: String,
    @SerializedName("ManagerEmail")
    val managerEmail: String,
    @SerializedName("Observations")
    val observationsList: List<SelectedObservationDetail>,
    @SerializedName("Remark")
    val remark: String,
    @SerializedName("StartDate")
    val startDate: String
)