package com.tft.selfbest.repository

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tft.selfbest.network.SafeApiCall
import com.tft.selfbest.network.SelfBestApi

abstract class BaseRepository(private val api: SelfBestApi) : SafeApiCall, AppCompatActivity() {

    fun logout() {
        Toast.makeText(this, "Your session has expired!! Please login again", Toast.LENGTH_SHORT).show()
    }
}