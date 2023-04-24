package com.tft.selfbest.di

import android.app.Application
import android.content.Context
import com.example.chat_feature.interfaces.HomeActivityCallerClass
import com.example.chat_feature.network.Api
import com.example.chat_feature.utils.Constants
import com.example.chat_feature.utils.SharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object apModule {


    @Provides
    @Singleton
    @Named("appContext")
    fun provContext(application: Application): Context {
        return application.applicationContext
    }





}