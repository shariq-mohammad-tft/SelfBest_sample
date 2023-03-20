package com.tft.selfbest.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tft.selfbest.data.entity.DistractedApp
import com.tft.selfbest.data.entity.SelfBestNotification
import com.tft.selfbest.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT * FROM user_profile where id = :userID")
    fun getUserProfile(userID: Int): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(apps: UserProfile)

    @Query("DELETE FROM user_profile")
    fun deleteAll()

}