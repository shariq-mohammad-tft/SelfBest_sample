package com.tft.selfbest.ui.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.chat_feature.ComposeActivity
import com.example.chat_feature.SimplifyPathComposeActivity
import com.tft.selfbest.R
import com.tft.selfbest.databinding.SimplifyPathActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimplifyPathActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simplify_path_activity)
        Handler().postDelayed({
            startActivity(Intent(this, SimplifyPathComposeActivity::class.java))
            finish()
        }, 3000)
    }
}