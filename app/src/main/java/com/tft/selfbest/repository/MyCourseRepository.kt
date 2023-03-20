package com.tft.selfbest.repository

import com.tft.selfbest.models.mycourse.AddCourse
import com.tft.selfbest.models.mycourse.FilterSearchCourse
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

class MyCourseRepository @Inject constructor(private val networkClient: SelfBestApiClient) {
    suspend fun getEnrolledCourses(userID: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.getEnrolledCourses(userID) })
    }.flowOn(Dispatchers.IO)

    suspend fun getSuggestedCourses(userID: Int, skill: String, platform: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            networkClient.apis.getSuggestedCourses(userID, skill, platform)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun getSearchResult(userID: Int, filterSearchCourse: FilterSearchCourse) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            networkClient.apis.getSearchResult(userID, filterSearchCourse)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun addCourse(userID: Int, addCourse: AddCourse) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { networkClient.apis.addCourse(userID, addCourse) })
    }.flowOn(Dispatchers.IO)

    suspend fun uploadCertificate(userId: Int, courseId: String, file: File?) = flow {
        val requestFile =
            file?.let { RequestBody.create("multipart/form-data".toMediaTypeOrNull(), it) }
        val requestImage =
            requestFile?.let { MultipartBody.Part.createFormData("certificate", file?.name, it) }
        val requestCourseId =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), courseId)
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process {
            if (requestImage != null) {
                networkClient.apis.uploadCertificate(userId, requestImage, requestCourseId)
            }
        })
    }.flowOn(Dispatchers.IO)
}