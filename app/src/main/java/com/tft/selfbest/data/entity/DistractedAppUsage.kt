package com.tft.selfbest.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "distracted_app_usage")
data class DistractedAppUsage(
    val site: String?,
    val startTime: String,
    val endTime: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}