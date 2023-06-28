package com.tft.selfbest.repository

import com.tft.selfbest.models.CollabToolsRequest
import com.tft.selfbest.models.OrgAddSkillRequest
import com.tft.selfbest.models.SaveOrgDetails
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class CompanyProfileRepository @Inject constructor(
    private val client: SelfBestApiClient
) {
    suspend fun getCompanyDetails(userId: Int)= flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getCompanyDetail(userId) })
    }.flowOn(Dispatchers.IO)

    suspend fun getCompanyDomains(userId: Int)= flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getAllDomains(userId) })
    }.flowOn(Dispatchers.IO)

    suspend fun getOrgSkills(userId: Int)= flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getAllOrgSkills(userId) })
    }.flowOn(Dispatchers.IO)

    suspend fun saveCompanyDetails(userId: Int, request: SaveOrgDetails)= flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.saveOrgDetails(userId,request) })
    }.flowOn(Dispatchers.IO)

    suspend fun saveCollabTools(userId: Int, request: CollabToolsRequest)= flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.collabTools(userId, request) })
    }.flowOn(Dispatchers.IO)

    suspend fun uploadOrgSheet(userId: Int, file: File?)= flow {
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
        val requestImage = MultipartBody.Part.createFormData("employee_db", file.name, requestFile)
        val requestUserId =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId.toString())
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.uploadEnmployeeSheet(userId, requestImage) })
    }.flowOn(Dispatchers.IO)

    suspend fun getOrgSheet(userId: Int)= flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getEmployeeSheet(userId) })
    }.flowOn(Dispatchers.IO)

    suspend fun uploadOrgProfilePhoto(userId: Int, file: File?)= flow {
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
        val requestImage = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val requestUserId =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId.toString())
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.uploadOrgProfilePhoto(userId, requestImage) })
    }.flowOn(Dispatchers.IO)

    suspend fun addOrgSkills(userId: Int, request: OrgAddSkillRequest)= flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.addOrgSkill(userId, request) })
    }.flowOn(Dispatchers.IO)

    suspend fun deleteOrgAccount(userId: Int, type: String)= flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.deleteOrgAccount(userId, type) })
    }.flowOn(Dispatchers.IO)
}