package com.example.chat_feature.interfaces

import android.app.Application
import javax.inject.Inject

class HomeActivityCallerClass @Inject constructor(private val homeActivityCaller: HomeActivityCaller) {
    fun startHomeActivity(){
        homeActivityCaller.callHomeActivity()
    }
}