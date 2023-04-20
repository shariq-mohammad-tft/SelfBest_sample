package com.example.chat_feature.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val TAG = "Extension"

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun String.normalText(): String {
    return this.trim()
}


fun Int.translateErrorCode(): String {
    return when (this) {
        404 -> "Route Not Found"
        401 -> "Invalid Session"
        500 -> "Internal Server Error"
        else -> "Something went wrong!"
    }
}


fun String.formatDateTime(): String =

    try {
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

        val date =
            dateFormat.parse(this) //You will get date object relative to server/client timezone wherever it is parsed

        val formatter = SimpleDateFormat(
            "dd-MM-yyyy hh:mm:ss a",
            Locale.getDefault()
        ) //If you need time just put specific format for time like 'HH:mm:ss'

        formatter.format(date ?: "")

    } catch (e: Exception) {
        Log.d(TAG, ":${e.message} ")
        ""
    }


fun String.createSocketUrl(id: String): String {
    // Id might be self or expert
    // wss://webbot.self.best/ws/chat/676/

    return "$this$id/"
}



fun Context.getFileFromUri(
    uri: Uri,
    fileName: String = System.currentTimeMillis().toString(),
): File? {
    return try {
        val inputStream: InputStream? = this.contentResolver.openInputStream(uri)
        val file = File(
            this.getExternalFilesDir(cacheDir.absolutePath),
            fileName // random name of file
        )
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        file
    } catch (e: Exception) {
        Log.d(TAG, "createFileFromAsset: ${e.message}")
        null
    }
}



fun File.toRequestBody(progressCallback: ((progress: Int) -> Unit)?): RequestBody {
    return object : RequestBody() {

        private var currentProgress = 0
        private var uploaded = 0L

        override fun contentType(): MediaType? {
            val fileType = name.substringAfterLast('.', "")
            return fileType.toMediaTypeOrNull()
        }

        @Throws(IOException::class)
        override fun writeTo(sink: BufferedSink) {
            source().use { source ->
                do {
                    val read = source.read(sink.buffer, 2048)
                    if (read == -1L) return // exit at EOF
                    sink.flush()
                    uploaded += read

                    /**
                     * The value of newProgress is going to be in between 0.0 - 2.0
                     */

                    var newProgress = ((uploaded.toDouble() / length().toDouble()))

                    /**
                     * To map it between 0.0 - 100.0
                     * Need to multiply it with 50
                     * (OutputMaxRange/InputMaxRange)
                     * 100 / 2 = 50
                     */
                    newProgress = (50 * newProgress)
//                    newProgress = (100 * newProgress)

                    if (newProgress.toInt() != currentProgress) {
                        progressCallback?.invoke(newProgress.toInt())
                    }
                    currentProgress = newProgress.toInt()
                } while (true)
            }
        }
    }
}
fun Context.getUserId():  Int{
    val sharedPrefManager=SharedPrefManager(this)
    return sharedPrefManager.getInt("id")
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.extractTime(timeString: String): String? {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSxxx")
    val utcDateTime = LocalDateTime.parse(timeString, formatter)
        .atZone(ZoneId.of("UTC"))
    val istDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"))
    val pattern = Regex("\\b([01]\\d|2[0-3]):([0-5]\\d)\\b")
    val matchResult = pattern.find(istDateTime.toLocalTime().toString())
    val time = matchResult?.value ?: return null
    val hour = time.substringBefore(":").toInt()
    val minute = time.substringAfter(":").toInt()
    val amPm = if (hour < 12) "AM" else "PM"
    val formattedHour = if (hour == 0 || hour == 12) 12 else hour % 12
    return String.format("%02d:%02d %s", formattedHour, minute, amPm)
}
