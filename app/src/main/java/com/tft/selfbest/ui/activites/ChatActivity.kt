package com.tft.selfbest.ui.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.chat_feature.ComposeActivity
import com.example.chat_feature.screens.chat.ChatScreen
import com.tft.selfbest.R
import com.tft.selfbest.ui.login.LoginActivity
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