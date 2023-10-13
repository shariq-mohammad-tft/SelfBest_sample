package com.example.chat_feature.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ErrorMessage(message: String = "Error Message") {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.error)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {

        Text(
            text = message,
            color = MaterialTheme.colors.onError,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold
        )

    }
}


@Composable
@Preview(showBackground = true)
fun ErrorMessagePreview() {
    ErrorMessage()
}