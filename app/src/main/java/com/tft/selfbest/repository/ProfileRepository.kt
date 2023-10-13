package com.tft.selfbest.repository

import android.util.Log
import com.google.gson.Gson
import com.tft.selfbest.models.DeleteDeviceTokenRequest
import com.tft.selfbest.models.DeviceTokenRequest
import com.tft.selfbest.models.ProfileChangesData
import com.tft.selfbest.models.SignUpDetail
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val client: SelfBestApiClient) {
    suspend fun getProfileData(userId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getProfile(userId) })
    }.flowOn(Dispatchers.IO)

    suspend fun getProfile(userId: Int, platform: String) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getProfile(userId, platform) })
    }.flowOn(Dispatchers.IO)

    suspend fun getAllSkills() = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getAllSkills("", "") })
    }.flowOn(Dispatchers.IO)

    suspend fun getJobs() = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getJobs() })
    }.flowOn(Dispatchers.IO)

    suspend fun getPersonality() = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.getPersonality()
        })
    }.flowOn(Dispatchers.IO)

    suspend fun updateProfilePhoto(file: File?, userId: Int) = flow {
        if (file == null) {
            emit(NetworkResponse.Error("File is null"))
            return@flow
        }

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val requestImage = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val requestUserId =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId.toString())

        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.updateProfilePicture(requestImage, requestUserId)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun disconnectAllCalendar(userId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.disConnectCalendar(userId) })
    }

    suspend fun connectCalendar(
        userId: Int,
        code: String,
        redirectUrl: String,
        calendarType: String,
    ) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.connectCalendar(
                userId,
                code,
                "Asia/Calcutta",
                redirectUrl,
                calendarType
            )
        })
    }

    suspend fun getSkills() = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.getSkills()
        })
    }.flowOn(Dispatchers.IO)

    suspend fun saveProfileChanges(userId: Int, profileChangesData: ProfileChangesData) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.saveProfileChanges(userId, profileChangesData)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun saveData(userId: Int, signUpData: SignUpDetail) = flow{
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.saveData(userId, signUpData)
        })
    }

    suspend fun getRecommendation(userId: Int, q: String) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.getRecommendation(userId, q)
        })
    }.flowOn(Dispatchers.IO)


    suspend fun uploadResumeFile(file: File?, userID: Int) = flow {
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
        val requestImage = MultipartBody.Part.createFormData("resume", file.name, requestFile)
        val requestUserId =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userID.toString())

        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.uploadResume(userID, requestImage)
        })
    }

    suspend fun accountSetting(userId: Int, type: String) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.accountSetting(userId, type)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun sendRegistrationToken(token: String, id: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.sendRegistrationToken(DeviceTokenRequest(id, token, "android", true))
        })
    }.flowOn(Dispatchers.IO)

    suspend fun updateRegistrationToken(token: String, id: Int, isActive: Boolean) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            client.apis.updateRegistrationToken(DeviceTokenRequest(id, token, "android", isActive))
        })
    }.flowOn(Dispatchers.IO)

    suspend fun deleteNotificationToken(token: String, id: Int) = flow {
        emit(NetworkResponse.Loading())
        val abc = DeleteDeviceTokenRequest("android", token, id)
        val body = Gson().toJson(abc)
        Log.e("Delete body", body.toString())
        emit(NetworkRequest.process {
            Log.e("Delete Request", "${DeleteDeviceTokenRequest("android", token, id)}")
            client.apis.deleteNotificationToken(DeleteDeviceTokenRequest(  "android", token, id))
        })
    }.flowOn(Dispatchers.IO)

}
