package com.tft.selfbest.data

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.tft.selfbest.data.entity.LearningHr
import com.tft.selfbest.utils.JsonUtil.Companion.fromJson
import com.tft.selfbest.utils.JsonUtil.Companion.toJson

class Converters {

    @TypeConverter
    fun fromJobTagsToString(learningHr: List<LearningHr>?): String? {
        return try {
            learningHr.toJson()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @TypeConverter
    fun fromStringToJobTags(text: String?): List<LearningHr>? {
        return try {
            text.fromJson<List<LearningHr>>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}