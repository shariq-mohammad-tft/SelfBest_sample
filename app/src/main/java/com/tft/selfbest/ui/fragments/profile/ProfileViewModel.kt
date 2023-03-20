package com.tft.selfbest.ui.fragments.profile

import android.util.Log
import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.*
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val preference: SelfBestPreference,
    val repository: ProfileRepository,
) : ViewModel(),
    LifecycleObserver {
    private val profileLiveData = MutableLiveData<NetworkResponse<ProfileResponse>>()
    val profileObserver: LiveData<NetworkResponse<ProfileResponse>> = profileLiveData
    private val personalityTypeLiveData = MutableLiveData<NetworkResponse<List<String>>>()
    val personalityTypeObserver: LiveData<NetworkResponse<List<String>>> = personalityTypeLiveData
    private val profileImageLiveData = MutableLiveData<NetworkResponse<ProfileImageResponse>>()
    val profileImageObserver: LiveData<NetworkResponse<ProfileImageResponse>> = profileImageLiveData
    private val skillsLiveData = MutableLiveData<NetworkResponse<List<String>>>()
    val skillsObserver: LiveData<NetworkResponse<List<String>>> = skillsLiveData
    private val profileChangesDoneMessage = MutableLiveData<String>()
    val profileChangesDoneMessageObserver: LiveData<String> =
        profileChangesDoneMessage
    private val signup = MutableLiveData<NetworkResponse<Unit>>()
    val signupObserver: LiveData<NetworkResponse<Unit>> = signup
    private val pSkills = MutableLiveData<NetworkResponse<List<String>>>()
    val pSkillsObserver : LiveData<NetworkResponse<List<String>>> = pSkills

    private val rSkillsLiveData = MutableLiveData<NetworkResponse<List<String>>>()
    val rSkillsObserver: LiveData<NetworkResponse<List<String>>> = rSkillsLiveData

    private val resumeUploadLiveData = MutableLiveData<NetworkResponse<UploadResumeResponse>>()
    val resumeUploadLiveDataObserver: LiveData<NetworkResponse<UploadResumeResponse>> = resumeUploadLiveData

    private val accountSetting = MutableLiveData<NetworkResponse<Unit>>()
    val accountSettingObserver: LiveData<NetworkResponse<Unit>> = accountSetting

    private val notificationResponse = MutableLiveData<NetworkResponse<DeviceTokenResponse>>()
    val notificationResponseObserver: LiveData<NetworkResponse<DeviceTokenResponse>> = notificationResponse

    fun getProfileData(isNoNeedOfPersonalityList: Boolean) {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.getProfileData(userId).collect {
                profileLiveData.postValue(it)
                if (it is NetworkResponse.Success) {
                    preference.setProfileData(it.data?.profileData)
                    preference.setProfilePicture(it.data?.profileData?.image!!)
                    if (!isNoNeedOfPersonalityList) {
                        getPersonalityList()
                    }
                }
            }
        }
    }

    fun getSkills() {
        viewModelScope.launch {
            Log.e("Skills List", "Success")
//            val userId = preference.getLoginData?.id ?: return@launch
            repository.getAllSkills().collect {
                if(it is NetworkResponse.Success)
                    pSkills.postValue(it)
                else
                    Log.e("Skills List", "$it")
            }
        }
    }

    fun getJobs() {
        viewModelScope.launch {
            //val userId = preference.getLoginData?.id ?: return@launch
            repository.getJobs().collect {
//                profileLiveData.postValue()
            }
        }
    }

    fun getProfile(platform: String) {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.getProfile(userId, platform).collect {
                if(it is NetworkResponse.Success) {
                    profileLiveData.postValue(it)
                }
            }
        }
    }

    fun getPersonalityList() {
        viewModelScope.launch {
            repository.getPersonality().collect {
                if (it is NetworkResponse.Success) {
                    personalityTypeLiveData.postValue(it)
                }
            }
        }
    }


    fun updateProfilePhoto(fileUrl: File?) {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.updateProfilePhoto(fileUrl, userId).collect {
                if (it is NetworkResponse.Success) {
                    Log.e("Image VM", it.data?.imageUrl ?: "")
                    preference.setProfilePicture(it.data?.imageUrl ?: "")
                    profileImageLiveData.postValue(it)
                }
            }
        }
    }

    fun disconnectAllCalendar() {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.disconnectAllCalendar(userId).collect {
                if (it is NetworkResponse.Success) {
                    getProfileData(true)
                }
            }
        }
    }

    fun connectCalendar(code: String, redirectUrl: String, calendarType: String) {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.connectCalendar(userId, code, redirectUrl, calendarType).collect {
                if (it is NetworkResponse.Success)
                    getProfileData(true)
            }
        }
    }


    fun getAllSkills() {
        viewModelScope.launch {
            repository.getSkills().collect {
                if (it is NetworkResponse.Success) {
                    skillsLiveData.postValue(it)
                }
            }
        }
    }

    fun saveProfileChangesData(profileChangesData: ProfileChangesData) {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.saveProfileChanges(userId, profileChangesData).collect {
                //Log.e("profile"," save")
                if (it is NetworkResponse.Success) {
                    profileChangesDoneMessage.postValue("Profile data updated successfully")
                }
            }
        }
    }

    fun saveData(signUpData: SignUpDetail){
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.saveData(userId, signUpData).collect{
                if(it is NetworkResponse.Success || it is NetworkResponse.Error){
                    signup.postValue(it)
                }
            }
        }
    }

    fun getRecommendation(q: String) {
        viewModelScope.launch {
            val userId = preference.getLoginData?.id ?: return@launch
            repository.getRecommendation(userId, q).collect {
                if (it is NetworkResponse.Success) {
                    rSkillsLiveData.postValue(it)
                }
            }
        }
    }

    fun getResumeSkills(file: File?){
        viewModelScope.launch {
            val id=preference.getLoginData?.id?:return@launch
            repository.uploadResumeFile(file, id).collect {
                if(it is NetworkResponse.Success)
                   resumeUploadLiveData.postValue(it)
            }
        }

    }

    fun accountSetting(type: String){
        viewModelScope.launch {
            val id=preference.getLoginData?.id?:return@launch
            repository.accountSetting(id, type).collect {
                if(it is NetworkResponse.Success)
                    accountSetting.postValue(it)
            }
        }

    }

    fun sendRegistrationToken(token: String){
        viewModelScope.launch {
            val id = preference.getLoginData?.id?:return@launch
            repository.sendRegistrationToken(token, id).collect{
                if(it is NetworkResponse.Success)
                    notificationResponse.postValue(it)
            }
        }
    }
}