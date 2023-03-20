package com.tft.selfbest.repository

import android.util.Log
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.data.dao.DistractedAppDao
import com.tft.selfbest.data.dao.DistractedAppUsageDao
import com.tft.selfbest.data.dao.ProfileDao
import com.tft.selfbest.models.*
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import com.tft.selfbest.utils.DataConversation.convert
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SelfBestRepository @Inject constructor(
    private val networkClient: SelfBestApiClient,
    private val distractedAppDao: DistractedAppDao,
    private val profileDao: ProfileDao,
    private val distractedAppUsageDao: DistractedAppUsageDao,
    private val preference: SelfBestPreference
) {
    private val SERVER_FORMAT: String = "EEE, dd MMM yyyy HH:mm:ss z"
    private val COUNTRY_ID: String = "GMT"
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun login(loginRequest: LoginRequest) = flow {
        emit(NetworkResponse.Loading())
        val response = NetworkRequest.process {
            networkClient.apis.login(loginRequest)
        }
        if (response is NetworkResponse.Success) {
            preference.setLoginData(response.data)
        }
        emit(response)
    }.flowOn(defaultDispatcher)

    suspend fun getNotificationInfo() = flow {
        val email = preference.getLoginData?.email ?: return@flow
        emit(NetworkRequest.process {
            networkClient.apis.getNotificationInfo(email, "mobile", "GMT")
        })
    }.flowOn(defaultDispatcher)

    suspend fun getGoHourStatus() = flow {
        val id = preference.getLoginData?.id ?: return@flow
        emit(NetworkRequest.process {
            networkClient.apis.getGoHourStatus(id, "Asia/Calcutta")
        })
    }.flowOn(defaultDispatcher)

    suspend fun syncData(scope: CoroutineScope) = flow {
        val userID = preference.getLoginData?.id ?: return@flow
        val profileAsync = scope.async {
            NetworkRequest.process {
                networkClient.apis.getProfile(userID)
            }
        }

        /* val distractedAppsAsync = scope.async {
             NetworkRequest.process {
                 networkClient.apis.getDistractedApps(userID)
             }
         }*/
        //sendDistractedUsage()
        val profileResponse = profileAsync.await()
        //val distractedAppsResponse = distractedAppsAsync.await()

        /* if (distractedAppsResponse is NetworkResponse.Success) {
             distractedAppDao.deleteAll()
             distractedAppDao.insertALl(distractedAppsResponse?.data?.list ?: arrayListOf())
         }*/

        if (profileResponse is NetworkResponse.Success) {
            profileResponse?.data?.profileData?.image?.let { preference.setProfilePicture(it) }
            emit(NetworkResponse.Success(true))
            profileDao.deleteAll()
            val profile = profileResponse?.data?.profileData?.convert() ?: null
            if (profile != null)
                profileDao.insert(profile)
        } else {
            emit(NetworkResponse.Error("Sync fail", false))
        }

        /*if (distractedAppsResponse is NetworkResponse.Success &&
            profileResponse is NetworkResponse.Success
        ) {
            emit(NetworkResponse.Success(true))
        } else {
            emit(NetworkResponse.Error("Sync fail", false))
        }*/
    }.flowOn(defaultDispatcher)


    private suspend fun sendDistractedUsage() = coroutineScope {
        val userID = preference.getLoginData?.id ?: return@coroutineScope
        val email = preference.getLoginData?.email ?: return@coroutineScope

        val list = distractedAppUsageDao.getUnSyncUsageData().map { app ->
            // val startTime = getServerTimeZone(app.startTime)
            // val endTime = getServerTimeZone(app.endTime)
            // DistractedAppModel(email, startTime, endTime, app.site)
        }

        if (list.isEmpty()) return@coroutineScope
        /*val response = NetworkRequest.process {
            networkClient.apis.sendDistractedAppUsage(userID, SendUsedDistractedApps(list))
        }
        if (response is NetworkResponse.Success) {
            distractedAppUsageDao.deleteAll()
        }*/
    }

    suspend fun sendDeviceObservation(url: String?, startTime: String, lastUrl: String?, lastStartTime: String,lastEndTime: String) = flow {
        val userId = preference.getLoginData?.id ?: return@flow
        val deviceObservationRequest =
            DeviceObservationRequest(
                "Mobile",
                lastUrl,
                lastStartTime,
                lastEndTime,
                url,
                startTime
            )
        emit(NetworkRequest.process {
            networkClient.apis.sendDeviceObservation(userId, deviceObservationRequest)
        })
    }.flowOn(defaultDispatcher)

    suspend fun getGoHourStatusWithStartTime() = flow {
        val id = preference.getLoginData?.id ?: return@flow
        val offlineTime = preference.getOfflineTime ?: return@flow
        emit(NetworkRequest.process {
            networkClient.apis.getGoHourStatusWithStartTime(
                id,
                "Asia/Calcutta",
                offlineTime
            )
        })
    }

    suspend fun sendOfflineDeviceObservations(getGoHourStatusResponse: GoHourStatusResponse?) =
        coroutineScope {
            val userId = preference.getLoginData?.id ?: return@coroutineScope
            val offlineStarTime = preference.getOfflineTime ?: return@coroutineScope
            val list = distractedAppUsageDao.getUnSyncUsageData().map { app ->
                DeviceObservationModel(app.site, app.startTime, app.endTime)
            }
            if (list.isEmpty()) {
                preference.clearOfflineTime()
                return@coroutineScope
            }
            val filterList =
                filterOfflineObservations(list, getGoHourStatusResponse, offlineStarTime)
            val offlineObservationData = SendOfflineDeviceObservations(filterList, "Mobile")
            Log.e("Offline Status", filterList.toString())
            val response = NetworkRequest.process {
                networkClient.apis.sendOfflineDeviceObservations(
                    userId,
                    offlineObservationData
                )
            }
            if (response is NetworkResponse.Success) {
                distractedAppUsageDao.deleteAll()
                preference.clearOfflineTime()
            }
        }

    private fun filterOfflineObservations(
        deviceObservationList: List<DeviceObservationModel>,
        logs: GoHourStatusResponse?,
        offlineTime: String
    ): List<DeviceObservationModel> {
        var filterList: MutableList<DeviceObservationModel> =
            mutableListOf()
        var filterFromPauseLog: MutableList<DeviceObservationModel> = mutableListOf()
        var startPauseTime = offlineTime
        if (logs == null)
            return filterList
        for (onlineStatus in logs.activeLogs) {
            for (offlineDeviceData in deviceObservationList) {
                Log.e("Online Status", onlineStatus.toString())
                Log.e("Offline Status", offlineDeviceData.toString())
                if ((onlineStatus.endTime == "0001-01-01T00:00:00Z" || onlineStatus.endTime == null) && compareDates(
                        offlineDeviceData.StartTime,
                        onlineStatus.startTime
                    ) == 1
                ) {
                    Log.e("Status", "if")
                    filterList.add(offlineDeviceData)
                } else if (compareDates(
                        offlineDeviceData.StartTime,
                        onlineStatus.startTime
                    ) == -1 && compareDates(offlineDeviceData.EndTime, onlineStatus.startTime) == 1
                ) {
                    if (compareDates(offlineDeviceData.EndTime, onlineStatus.endTime) == -1) {
                        Log.e("Status", "else if if")
                        filterList.add(
                            DeviceObservationModel(
                                offlineDeviceData.Url,
                                onlineStatus.startTime,
                                offlineDeviceData.EndTime
                            )
                        )
                    }
                    else {
                        Log.e("Status", "else if else")
                        filterList.add(
                            DeviceObservationModel(
                                offlineDeviceData.Url,
                                onlineStatus.startTime,
                                onlineStatus.endTime
                            )
                        )
                    }
                } else if (compareDates(
                        offlineDeviceData.StartTime,
                        onlineStatus.startTime
                    ) == 1 && compareDates(offlineDeviceData.EndTime, onlineStatus.endTime) == -1
                ) {
                    Log.e("Status", "else if 2")
                    filterList.add(offlineDeviceData)
                } else if (compareDates(
                        offlineDeviceData.StartTime,
                        onlineStatus.endTime
                    ) == -1 && compareDates(offlineDeviceData.EndTime, onlineStatus.endTime) == 1
                ) {
                    if (compareDates(offlineDeviceData.StartTime, onlineStatus.startTime) == 1) {
                        Log.e("Status", "else if3 if")
                        filterList.add(
                            DeviceObservationModel(
                                offlineDeviceData.Url,
                                offlineDeviceData.StartTime,
                                onlineStatus.endTime
                            )
                        )
                    }
                    else {
                        Log.e("Status", "else if3 else")
                        filterList.add(
                            DeviceObservationModel(
                                offlineDeviceData.Url,
                                onlineStatus.startTime,
                                onlineStatus.endTime
                            )
                        )
                    }
                }
            }
        }
        if (logs.pauseLogs == null)
            return filterList
        else {
            for (pauseData in logs.pauseLogs) {
                for (deviceObservation in filterList) {
                    if (pauseData.endTime == "0001-01-01T00:00:00Z") {
                        if (compareDates(deviceObservation.StartTime, pauseData.startTime) == 1)
                            continue
                        else {
                            if (compareDates(
                                    deviceObservation.StartTime,
                                    pauseData.startTime
                                ) == -1 && compareDates(
                                    deviceObservation.StartTime,
                                    startPauseTime
                                ) == 1
                            )
                                filterFromPauseLog.add(
                                    DeviceObservationModel(
                                        deviceObservation.Url,
                                        deviceObservation.StartTime,
                                        pauseData.endTime
                                    )
                                )
                        }
                    } else if (compareDates(
                            deviceObservation.StartTime,
                            pauseData.startTime
                        ) == 1 && compareDates(deviceObservation.EndTime, pauseData.endTime) == -1
                    ) {
                        continue
                    } else if (compareDates(
                            deviceObservation.StartTime,
                            startPauseTime
                        ) == 1 && compareDates(
                            deviceObservation.StartTime,
                            pauseData.startTime
                        ) == -1
                    ) {
                        if (compareDates(deviceObservation.EndTime, pauseData.startTime) == 1)
                            filterFromPauseLog.add(
                                DeviceObservationModel(
                                    deviceObservation.Url,
                                    deviceObservation.StartTime,
                                    pauseData.startTime
                                )
                            )
                        else
                            filterFromPauseLog.add(deviceObservation)
                    }


                }
                startPauseTime = pauseData.endTime
            }
        }
        return filterFromPauseLog
    }

    private fun compareDates(firstDate: String, secondDate: String): Int {
        if(secondDate != null) {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            format.timeZone = TimeZone.getDefault()
            val date1 = format.parse(firstDate)
            val date2 = format.parse(secondDate)
            if (date1 != null) {
                return date1.compareTo(date2)
            }
        }
        return 1
    }

}