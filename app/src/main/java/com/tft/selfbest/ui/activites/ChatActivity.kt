package com.tft.selfbest.ui.activites

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.chat_feature.ComposeActivity
import com.tft.selfbest.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private val myRunnable by lazy {
        Runnable {
            val intent = Intent(this, ComposeActivity::class.java)
            intent.putExtra("STARTING_POINT", "Selfbest")
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(myRunnable, 3000)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(myRunnable)
    }
}