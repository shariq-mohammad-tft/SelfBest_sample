package com.tft.selfbest.models

data class SendOfflineDeviceObservations(
    val DeviceObservations: List<DeviceObservationModel>,
    val ApplicationType: String
)

data class DeviceObservationModel(
    val Url: String?,
    val StartTime: String,
    val EndTime: String
)