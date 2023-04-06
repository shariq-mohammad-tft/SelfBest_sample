package com.tft.selfbest

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.ProfileRepository
import com.tft.selfbest.ui.activites.ChatActivity
import com.tft.selfbest.utils.logDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var repository: ProfileRepository

    @Inject
    lateinit var preferences: SelfBestPreference

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        coroutineContext.logDetails("TAG", throwable)
    }
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

    @SuppressLint("MissingPermission")
    override fun onMessageReceived(message: RemoteMessage) {

        message.notification?.let {
            //Message Services handle notification
            val resultIntent = Intent(this, ChatActivity::class.java)

            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(resultIntent)
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
                )
            }

            val notificationManger =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    getString(R.string.app_name),
                    message.from,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManger.createNotificationChannel(notificationChannel)
            }

            val builder = NotificationCompat.Builder(this, getString(R.string.app_name))
                .setOngoing(false)
                .setSmallIcon(R.drawable.selfbest)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(it.title)
                .setContentText(it.body)

            if (it.imageUrl != null) {
                builder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(
                        getBitmapfromUrl(
                            it.imageUrl.toString()
                        )
                    )
                )
            }

            val notification = builder.build()
            notification.flags = Notification.FLAG_AUTO_CANCEL
            Log.e("Notification", it.body.toString())
            notificationManger.notify(100, notification)
        }
    }

    override fun onNewToken(token: String) {
        //handle token
        scope.launch {
            repository.sendRegistrationToken(token, preferences.getLoginData?.id!!).collect {
                if (it is NetworkResponse.Success) {
                    Log.e("Notification", "Token stored successfully")
                }
            }
        }
        Log.e("New Token", token)
    }

    private fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Log.e("Notification", "Error in getting notification image: " + e.localizedMessage)
            null
        }
    }
}
