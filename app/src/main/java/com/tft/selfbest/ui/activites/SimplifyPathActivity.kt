package com.tft.selfbest.ui.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.chat_feature.ComposeActivity
import com.tft.selfbest.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimplifyPathActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private val myRunnable by lazy {
        Runnable {
            val intent = Intent(this, ComposeActivity::class.java)
            intent.putExtra("STARTING_POINT", "Simplify")
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simplify_path_activity)

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(myRunnable, 3000)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(myRunnable)
    }
}