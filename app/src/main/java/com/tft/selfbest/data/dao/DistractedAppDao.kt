package com.tft.selfbest.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tft.selfbest.data.entity.DistractedApp
import kotlinx.coroutines.flow.Flow

@Dao
interface DistractedAppDao {

    @Query("SELECT * FROM distractedApp")
    fun loadDistractedApps(): Flow<List<DistractedApp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(apps: DistractedApp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALl(apps: List<DistractedApp>)

    @Query("DELETE FROM distractedApp")
    fun deleteAll()
}