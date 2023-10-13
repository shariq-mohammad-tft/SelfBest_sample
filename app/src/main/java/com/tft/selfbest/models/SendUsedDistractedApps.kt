package com.tft.selfbest.models

data class SendUsedDistractedApps(val data: List<DistractedAppModel>)

data class DistractedAppModel(
    val Email: String?,
    val Starttime: String?,
    val Endtime: String?,
    val Url: String?,
    val Type: String = "Mobile"
)