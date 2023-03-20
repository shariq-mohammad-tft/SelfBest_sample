package com.tft.selfbest.ui.fragments.userManagement

import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.*
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.UserManagementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserManagementViewModel @Inject constructor(
    private val preferences: SelfBestPreference,
    private val userManagementRepository: UserManagementRepository
) : ViewModel(), LifecycleObserver {
    private val userRequestsData =
        MutableLiveData<NetworkResponse<List<UserRequest>>>()
    val userRequestsDataObserver: LiveData<NetworkResponse<List<UserRequest>>> =
        userRequestsData
    private val deleteRequestsData =
        MutableLiveData<NetworkResponse<List<DeleteAccountResponse>>>()
    val deleteRequestsDataObserver: LiveData<NetworkResponse<List<DeleteAccountResponse>>> =
        deleteRequestsData
    private val skillRequestData =
        MutableLiveData<NetworkResponse<SkillRequest>>()
    val skillRequestDataObserver: LiveData<NetworkResponse<SkillRequest>> =
        skillRequestData

    private val skillRequestChangeData =
        MutableLiveData<NetworkResponse<Unit>>()
    val skillRequestChangeDataObserver: LiveData<NetworkResponse<Unit>> =
        skillRequestChangeData

    private val skillsRequestData =
        MutableLiveData<NetworkResponse<List<String>>>()
    val skillsRequestDataObserver: LiveData<NetworkResponse<List<String>>> =
        skillsRequestData

    fun getUserRequests(status: String) {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            userManagementRepository.getUserRequests(userId, status).collect {
                if (it is NetworkResponse.Success) {
                    userRequestsData.postValue(it)
                }
            }
        }
    }

    fun getDeleteAccounts() {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            userManagementRepository.getDeleteAccounts(userId).collect {
                if (it is NetworkResponse.Success)
                    deleteRequestsData.postValue(it)
            }
        }
    }

    fun getSkillRequests() {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            userManagementRepository.getSkillRequests(userId).collect {
                if (it is NetworkResponse.Success)
                    skillRequestData.postValue(it)
            }
        }
    }

    fun changeRequestStatus(request: ChangeRequestStatus) {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            userManagementRepository.changeRequestStatus(userId, request).collect {
                if (it is NetworkResponse.Success)
                    skillRequestChangeData.postValue(it)
            }
        }
    }


    fun changeSkillRequestStatus(request: ChangeSkillRequestStatus) {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            userManagementRepository.changeSkillRequestStatus(userId, request).collect {
                if (it is NetworkResponse.Success)
                    skillRequestChangeData.postValue(it)
            }
        }
    }

    fun skillRequestStatus(skill: String) {
        viewModelScope.launch {
            userManagementRepository.requestSkill(skill).collect {
                if (it is NetworkResponse.Success)
                    skillsRequestData.postValue(it)
            }
        }
    }

    fun getCertificateRequest(teamId: Int) {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            userManagementRepository.getCertificateRequest(userId, teamId).collect {
                if (it is NetworkResponse.Success)
                    skillRequestChangeData.postValue(it)
            }
        }
    }

    fun changeAccountRequest(request: ChangeAccountRequestBody) {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            userManagementRepository.changeAccountRequest(userId, request).collect {
                if (it is NetworkResponse.Success)
                    skillRequestChangeData.postValue(it)
            }
        }
    }
}