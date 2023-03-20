package com.tft.selfbest.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tft.selfbest.R
import com.tft.selfbest.databinding.LayoutForCompose2Binding

class ChatActivity2 : AppCompatActivity() {
    private lateinit var binding: LayoutForCompose2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setTheme(R.style.Theme_ComposePlayground)
        binding = LayoutForCompose2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //init()
    }

//    private fun init() {
//
//        binding.button.setOnClickListener {
//            binding.tvTitle.text = "Edited From Compose!"
//        }
//
//        binding.composeView.setContent {
//            DialogScreen()
//        }
//    }
}