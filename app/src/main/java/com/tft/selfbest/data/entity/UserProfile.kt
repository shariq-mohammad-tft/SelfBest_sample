package com.tft.selfbest.data.entity

import androidx.room.Entity


@Entity(tableName = "user_profile", primaryKeys = ["id"])
data class UserProfile(
    val id: Int,
    val email: String?
)