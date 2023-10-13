package com.example.chat_feature.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.formatToString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}