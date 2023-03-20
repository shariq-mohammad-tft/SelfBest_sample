package com.tft.selfbest.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tft.selfbest.data.entity.DistractedAppUsage


@Dao
interface DistractedAppUsageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(apps: DistractedAppUsage)

    @Query("SELECT * FROM distracted_app_usage")
    fun getUnSyncUsageData(): List<DistractedAppUsage>

    @Query("DELETE FROM distracted_app_usage")
    fun deleteAll()
}