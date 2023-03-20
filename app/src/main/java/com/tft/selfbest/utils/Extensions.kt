package com.tft.selfbest.utils

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

fun CoroutineContext.logDetails(TAG: String, throwable: Throwable? = null) {
    val id = "Coroutine Exception Handler"
    val list = sequenceOf(
        Job,
        ContinuationInterceptor,
        CoroutineExceptionHandler,
        CoroutineName
    )
    val exception = list.mapNotNull { key -> this[key] }
        .map { "${it.key} = $it" }
        .reduce { acc, s -> "$acc \n $s" }

    val log = if (throwable != null) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
        "$id : $TAG \n $exception \n ${throwable.message}"
    } else
        "$id :$TAG \n $exception"

    FirebaseCrashlytics.getInstance().log(log)
    Log.e(TAG, log)
    throwable?.printStackTrace()
}
