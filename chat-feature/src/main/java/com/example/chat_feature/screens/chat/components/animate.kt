package com.example.chat_feature.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AnimatedBox() {
    var color by remember { mutableStateOf(Color.Red) }
    var text by remember { mutableStateOf("Hello") }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.size(20.dp)) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(2000)
                scope.launch {
                    color = listOf(Color.Red, Color.Green, Color.Blue).random()
                    text = "World"
                }
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color)) {
        Text(text)
    }
}
