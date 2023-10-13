package com.tft.selfbest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tft.selfbest.data.dao.DistractedAppDao
import com.tft.selfbest.data.dao.DistractedAppUsageDao
import com.tft.selfbest.data.dao.ProfileDao
import com.tft.selfbest.data.entity.DistractedApp
import com.tft.selfbest.data.entity.DistractedAppUsage
import com.tft.selfbest.data.entity.UserProfile


@Database(
    entities = [UserProfile::class, DistractedApp::class, DistractedAppUsage::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class SelfBestDatabase : RoomDatabase() {
    abstract fun distractedAppDao(): DistractedAppDao
    abstract fun profileDao(): ProfileDao
    abstract fun distractedAppUsageDao(): DistractedAppUsageDao
}