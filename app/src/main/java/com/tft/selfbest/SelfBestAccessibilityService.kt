package com.tft.selfbest

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.data.dao.DistractedAppDao
import com.tft.selfbest.data.dao.DistractedAppUsageDao
import com.tft.selfbest.data.dao.ProfileDao
import com.tft.selfbest.data.entity.DistractedApp
import com.tft.selfbest.data.entity.DistractedAppUsage
import com.tft.selfbest.data.entity.UserProfile
import com.tft.selfbest.models.GoHourResponse
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.SelfBestRepository
import com.tft.selfbest.utils.Constants.Companion.IGNORED_APPS
import com.tft.selfbest.utils.OfflineDeviceObservationUtil
import com.tft.selfbest.utils.logDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SelfBestAccessibilityService : AccessibilityService() {

    private val TAG = "SelfBestAccessibilityService"

    @Inject
    lateinit var repo: SelfBestRepository

    @Inject
    lateinit var distractedAppDao: DistractedAppDao

    @Inject
    lateinit var distractedAppUsageDao: DistractedAppUsageDao

    @Inject
    lateinit var profileDao: ProfileDao

    @Inject
    lateinit var preference: SelfBestPreference

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        coroutineContext.logDetails(TAG, throwable)
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

    private var currentAppUsage: CurrentAppUsage? = null
    private var lastAppUsage: LastAppUsage?= null

    private val appFlow = MutableSharedFlow<String>(replay = 0)

    private var isGoHourActive: Boolean = false

    private var currentApplication: String? = null
    private var lastApplication : String? = null

    private val SERVER_FORMAT: String = "EEE, dd MMM yyyy HH:mm:ss z"
    private val UPDATED_FORMAT: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private val COUNTRY_ID: String = "Asia/Calcutta"
    private val listOfNonUrlApp =
        listOf(
            "phone",
            "call",
            "messages",
            "camera",
            "calculator",
            "gallery",
            "contacts",
            "calendar",
            "clock"
        )


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.packageName ?: return
        if (event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED || event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED || event.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) return
        val packageName = event.packageName.toString()
        //val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        val applicationInfo = if (Build.VERSION.SDK_INT >= 33) {
            packageManager.getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(0))
        } else {
            packageManager.getApplicationInfo(packageName, 0)
        }
        val appLabel = packageManager.getApplicationLabel(applicationInfo)
        scope.launch { appFlow.emit(appLabel.toString().uppercase()) }
    }

    override fun onInterrupt() {
        //Log.d(TAG, "onInterrupt")
        FirebaseCrashlytics.getInstance().recordException(ServiceInteruptedException("onInterrupt"))
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"))
        //Log.d(TAG, "onServiceConnected")

        /* if (application is SelfBestApplication) {
             SyncDataWorker.submitSyncDataWorker(application as SelfBestApplication)
         }*/
        scope.launch {
            /* combine(
                 appFlow.distinctUntilChanged().filter { !IGNORED_APPS.contains(it) },
                 profileDao.getUserProfile(preference.getLoginData?.id ?: return@launch)
                     .filterNotNull(),
                 distractedAppDao.loadDistractedApps().filter { it.isNotEmpty() }
             ) { appName, userProfile, distractedApp ->
                 return@combine Triple(appName, userProfile, distractedApp)
             }.collect {*/
            appFlow.distinctUntilChanged().filter { !IGNORED_APPS.contains(it) }.collect { it ->
                //Log.d(TAG, it)
                //logEndAppUsage()
                // currentAppUsage = null
                if (currentApplication != null && currentApplication.equals(it.lowercase())) return@collect
                if(currentApplication != null) {
                    lastApplication = currentApplication
                    lastAppUsage =
                        LastAppUsage(
                            currentAppUsage?.distractedAppUrl,
                            currentAppUsage!!.startTime,
                            getTime()
                            //getServerTimeZone(System.currentTimeMillis())
                        )
                }
                if(currentApplication == null){
                    lastAppUsage =
                        LastAppUsage(
                            "",
                            "",
                            ""
                        )
                }
                currentApplication = it.lowercase()
                if (OfflineDeviceObservationUtil.isNetworkAvailable(applicationContext)) {
                    val nonUrlApp = listOfNonUrlApp.firstOrNull {
                        it == currentApplication
                    }
                    val completeUrl = "www.${currentApplication?.trim()}.com"
                    currentAppUsage =
                        CurrentAppUsage(
                            nonUrlApp ?: completeUrl,
                            getTime()
//                            getServerTimeZone(System.currentTimeMillis())
                        )
                    scope.launch {
                        repo.getGoHourStatus().collect { response ->
                            if (response is NetworkResponse.Success) {
                                response.data?.let {
                                    isGoHourActive = it.isActive && !it.isPause
                                    if (isGoHourActive) {
                                        showNotification1(
                                            getString(R.string.app_name),
                                            "You are using $currentApplication"
                                        )
                                        sendDeviceLogToServer()
                                    }
                                }
                            }
                        }
                    }
                    // checkDistraction(it.first, it.second, it.third)
                } else {
                    if (preference.getOfflineTime == null && isGoHourActive) {
//                        preference.setOfflineTime(getServerTimeZone(System.currentTimeMillis()))
                        preference.setOfflineTime(getTime())
                    }
                    currentAppUsage?.let {
                        it.endTime = getTime()
                            //getServerTimeZone(System.currentTimeMillis())
                        //delay(2000)
                        saveAppUsageInDb(it.copy())
                    }
                    currentAppUsage = null
                    val nonUrlApp = listOfNonUrlApp.firstOrNull {
                        it == currentApplication
                    }
                    val completeUrl = "www.${currentApplication?.trim()}.com"
                    currentAppUsage =
                        CurrentAppUsage(
                            nonUrlApp ?: completeUrl,
                            getTime()
//                            getServerTimeZone(System.currentTimeMillis())
                        )
                }
            }
        }
        callingGoHourApiInLoop()
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or
                AccessibilityEvent.TYPE_VIEW_CLICKED or
                AccessibilityEvent.TYPE_VIEW_FOCUSED

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
        info.flags = AccessibilityServiceInfo.DEFAULT
        info.notificationTimeout = 100
        this.serviceInfo = info
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private fun checkDistraction(
        appName: String,
        userProfile: UserProfile,
        distractedApps: List<DistractedApp>
    ) {
        val distractedApp = checkAppInDistractedAppList(appName, distractedApps) ?: return
        //if (!checkWorkingHr(userProfile)) return
        //  currentAppUsage = CurrentAppUsage(distractedApp, System.currentTimeMillis())
        pullingLearningInfo()
    }

    /*Log the distracted app data in db after user switch to other app*/
    private suspend fun sendDeviceLogToServer() =
        withContext(Dispatchers.IO) {
            //currentAppUsage?.let {
            //  it.endTime = System.currentTimeMillis()
            // delay(2000)
            // saveAppUsageInDb(it.copy())
            scope.launch {
                repo.sendDeviceObservation(
                    currentAppUsage?.distractedAppUrl,
                    currentAppUsage!!.startTime,
                    lastAppUsage?.url,
                    lastAppUsage!!.startTime,
                    lastAppUsage!!.endTime
                ).collect { response ->
                    if (response is NetworkResponse.Success)

                    Log.d("Data Send to Server ",response.data?.response.toString() )

                }
            }
            //}
        }

    private fun checkAppInDistractedAppList(
        appName: String,
        distractedApps: List<DistractedApp>
    ): DistractedApp? {
        return distractedApps.firstOrNull {
            (it.state == true &&
                    ((!TextUtils.isEmpty(it.url) && it.url?.uppercase()
                        ?.contains(appName.uppercase()) == true)))
        }
    }

    /*private fun checkWorkingHr(userProfile: UserProfile?): Boolean {
        userProfile?.learningHr?.let { learningHrList ->
            val learningHr = learningHrList.firstOrNull { it.isInLearningHr() }
            return (learningHr != null)
        }
        return false
    }*/

    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, description: String) {
        val builder = NotificationCompat.Builder(this, getString(R.string.app_name))
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(300L, 300L, 300L))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    private fun showNotification1(title: String, description: String) {
        val notificationId = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notification =
                NotificationCompat.Builder(this, getString(R.string.app_name))
                    .setOngoing(false)
                    .setSmallIcon(R.mipmap.ic_self_best_2_round)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setWhen(System.currentTimeMillis()).build()
            val notificationManger =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                getString(R.string.app_name),
                title,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManger.createNotificationChannel(notificationChannel)
            notificationManger.notify(notificationId, notification)
        } else {
            val notification = NotificationCompat.Builder(this, "SelfBest")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(description)
                .setOngoing(false)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis()).build()
            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, notification)
        }
    }

    private fun pullingLearningInfo() {
        scope.launch {
            repo.getGoHourStatus().collect { response ->
                if (response is NetworkResponse.Success) {
                    response.data?.let { checkWorkingHr(it)
                    }
                }
            }
        }
    }

    private fun checkWorkingHr(getGoHourDetails: GoHourResponse) {
        if (getGoHourDetails.isActive && !getGoHourDetails.isPause) {
            // if (!TextUtils.isEmpty(notificationInfo.description))
            showNotification1(
                getString(R.string.app_name),
                "Notification for distraction"
            )
        }
    }



    private fun saveAppUsageInDb(app: CurrentAppUsage) {
        Log.e("SaveAppUsage", app.toString())
        val usage =
            DistractedAppUsage(
                app.distractedAppUrl,
                app.startTime,
                app.endTime
            )
        scope.launch(Dispatchers.IO) {
            distractedAppUsageDao.insert(usage)
        }
    }

    private class ServiceInteruptedException(message: String) : Exception(message)

    private fun callingGoHourApiInLoop() {
        scope.launch {
            if (OfflineDeviceObservationUtil.isNetworkAvailable(applicationContext)) {
                repo.getGoHourStatusWithStartTime().collect { response ->
                    if (response is NetworkResponse.Success) {
                        Log.e("Offline Status Success", response.data.toString())
                        scope.launch(Dispatchers.IO) {
                            repo.sendOfflineDeviceObservations(response.data)
                        }
                    }
                }
                repo.getGoHourStatus().collect { response ->
                    if (response is NetworkResponse.Success) {
                        if (response.data != null) {
                            val isCurrentlyActive =
                                response.data.isActive && !response.data.isPause
                            if (isCurrentlyActive != isGoHourActive) {
                                isGoHourActive = isCurrentlyActive
                                if (isCurrentlyActive && currentApplication != null) {
                                    showNotification1(
                                        getString(R.string.app_name),
                                        "You are using $currentApplication"
                                    )
                                    sendDeviceLogToServer()
                                }
                            }
                        }
                    } else {
                        if (response is NetworkResponse.Error) {
                            isGoHourActive = false
                        }
                    }
                }
            } else {
                if (preference.getOfflineTime == null && isGoHourActive) {
                    Log.e("Offline Time", getTime())
                    preference.setOfflineTime(getTime())
                }
            }
            delay(30000)
            callingGoHourApiInLoop()
        }
    }

    data class CurrentAppUsage(
        val distractedAppUrl: String?,
        val startTime: String,
        var endTime: String = ""
    )

    data class LastAppUsage(
        val url: String? = "",
        val startTime: String = "",
        val endTime: String = ""
    )

    private fun getServerTimeZone(timeInMilliSec: Long): String {
        val dfTarget: DateFormat = SimpleDateFormat(UPDATED_FORMAT, Locale.getDefault())
        return dfTarget.format(Date(timeInMilliSec))
    }

    private fun getTime(): String{
        val gmtDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        gmtDateFormat.timeZone = TimeZone.getTimeZone("GMT");

        return gmtDateFormat.format(Date())
    }

}