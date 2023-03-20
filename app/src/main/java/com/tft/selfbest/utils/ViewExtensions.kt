package com.tft.selfbest.utils

import android.view.View
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun View.onClick(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        this.trySendBlocking(Unit)
    }
    awaitClose {
        setOnClickListener(null)
    }
}
