package com.tft.selfbest.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tft.selfbest.R
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
fun Context.dpToPx(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.isOnline(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}
fun Context.isInternetAvailable(): Boolean {
    // register activity with the connectivity manager service
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    } else {
        // if the android version is below M
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}

/*fun EditText.showIconErrorOnly() {
    val errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_error)
    errorIcon?.setBounds(0, 0, errorIcon.intrinsicWidth, errorIcon.intrinsicHeight)
    setCompoundDrawables(null, null, errorIcon, null)
}*/

fun EditText.showIconErrorOnly() {
    val errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_error)
    errorIcon?.setBounds(0, 0, errorIcon.intrinsicWidth, errorIcon.intrinsicHeight)
    setCompoundDrawables(null, null, errorIcon, null)

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (!s.isNullOrEmpty()) {
                setCompoundDrawables(null, null, null, null)
            } else {
                setCompoundDrawables(null, null, errorIcon, null)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

