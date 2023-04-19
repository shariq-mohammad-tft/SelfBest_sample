package com.tft.selfbest.ui.activites

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.chat_feature.ComposeActivity
import com.tft.selfbest.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        Handler().postDelayed({
            startActivity(Intent(this, ComposeActivity::class.java))
            finish()
        }, 3000)
    }
}