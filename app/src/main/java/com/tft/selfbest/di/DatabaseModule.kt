package com.tft.selfbest.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.tft.selfbest.data.SelfBestDatabase
import com.tft.selfbest.data.dao.DistractedAppDao
import com.tft.selfbest.data.dao.DistractedAppUsageDao
import com.tft.selfbest.data.dao.ProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun distractedAppDao(appDatabase: SelfBestDatabase): DistractedAppDao {
        return appDatabase.distractedAppDao()
    }


    @Provides
    fun profileDao(appDatabase: SelfBestDatabase): ProfileDao {
        return appDatabase.profileDao()
    }

    @Provides
    fun distractedAppUsageDao(appDatabase: SelfBestDatabase): DistractedAppUsageDao {
        return appDatabase.distractedAppUsageDao()
    }

    @Provides
    @Singleton //tell Hilt that this is application context
    fun provideAppDatabase(@ApplicationContext appContext: Context): SelfBestDatabase {
        return Room.databaseBuilder(
            appContext,
            SelfBestDatabase::class.java,
            "selfbest"
        ).build()
    }

}