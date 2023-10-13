package com.example.chat_feature.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = LightBlue,
    onPrimary = Navy,
    surface = Color.White,
    onSurface = Color.White,
)


private val LightColorPalette = lightColors(
    primary = LightBlue,
    onPrimary = Navy,
    surface = Color.White,
    onSurface = Color.White,


)

@Composable
fun ChatBotSampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        LightColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}