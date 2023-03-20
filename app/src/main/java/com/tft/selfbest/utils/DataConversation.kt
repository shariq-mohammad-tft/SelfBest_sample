package com.tft.selfbest.utils

import com.tft.selfbest.data.entity.UserProfile
import com.tft.selfbest.models.ProfileData

object DataConversation {
    fun ProfileData.convert(): UserProfile? {
        this.id ?: return null
        //this.learning ?: return null
        return UserProfile(
            this.id,
            this.email
            // learning.map { LearningHr(it.day, it.startTime, it.endTime) }
        )
    }
}