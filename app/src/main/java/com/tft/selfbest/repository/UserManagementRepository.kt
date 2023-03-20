package com.tft.selfbest.repository

import com.tft.selfbest.models.ChangeAccountRequestBody
import com.tft.selfbest.models.ChangeRequestStatus
import com.tft.selfbest.models.ChangeSkillRequestStatus
import com.tft.selfbest.models.SelectedTimeSheetData
import com.tft.selfbest.network.NetworkRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.SelfBestApiClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserManagementRepository  @Inject constructor(private val client: SelfBestApiClient) {
    suspend fun getUserRequests(userID: Int, status: String) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getUserRequests(userID, status) })
    }

    suspend fun getDeleteAccounts(userID: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getDeleteAccounts(userID) })
    }

    suspend fun getSkillRequests(userID: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getSkillRequests(userID) })
    }

    suspend fun changeRequestStatus(userID: Int, request: ChangeRequestStatus) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.changeRequestStatus(userID, request) })
    }

    suspend fun changeSkillRequestStatus(userID: Int, request: ChangeSkillRequestStatus) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.changeSkillRequestStatus(userID, request) })
    }

    suspend fun requestSkill(skill: String) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getAllSkills(skill, "") })
    }

    suspend fun getCertificateRequest(userID: Int, teamId: Int) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.getCertificateRequest(userID, teamId) })
    }

    suspend fun changeAccountRequest(userID: Int, request: ChangeAccountRequestBody) = flow {
        emit(NetworkResponse.Loading())
        emit(NetworkRequest.process { client.apis.changeAccountRequest(userID, request) })
    }
}