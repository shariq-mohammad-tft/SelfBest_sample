package com.tft.selfbest.models.calendarEvents

data class RecursiveDays(
    val day: String,
    var recursiveDate: String,
    var isSelected: Boolean = true,
)