package com.tft.selfbest.ui.fragments.profile.companyProfile

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.CollabToolsRequest
import com.tft.selfbest.models.CompanyProfileDetail
import com.tft.selfbest.models.OrgAccountSettingResponse
import com.tft.selfbest.models.OrgAddSkillRequest
import com.tft.selfbest.models.ProfileResponse
import com.tft.selfbest.models.SaveOrgDetails
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.CompanyProfileRepository
import com.tft.selfbest.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CompanyProfileViewModel @Inject constructor(
    val preference: SelfBestPreference,
    val repository: CompanyProfileRepository,
): ViewModel(), LifecycleObserver {
    private val companyProfileLiveData = MutableLiveData<NetworkResponse<CompanyProfileDetail>>()
    val companyProfileObserver: LiveData<NetworkResponse<CompanyProfileDetail>> = companyProfileLiveData

    private val companyDomains = MutableLiveData<NetworkResponse<List<String>>>()
    val companyDomainsObserver: LiveData<NetworkResponse<List<String>>> = companyDomains

    private val companyOrgSkills = MutableLiveData<NetworkResponse<List<String>>>()
    val companyOrgSkillsObserver: LiveData<NetworkResponse<List<String>>> = companyOrgSkills

    private val saveCompanyProfileLiveData = MutableLiveData<NetworkResponse<Unit>>()
    val saveCompanyProfileObserver: LiveData<NetworkResponse<Unit>> = saveCompanyProfileLiveData


    private val profileImageLiveData = MutableLiveData<NetworkResponse<Unit>>()
    val profileImageObserver: LiveData<NetworkResponse<Unit>> = profileImageLiveData

    private val uploadSheetLiveData = MutableLiveData<NetworkResponse<Unit>>()
    val uploadSheetObserver: LiveData<NetworkResponse<Unit>> = uploadSheetLiveData

    private val downloadSheetLiveData = MutableLiveData<NetworkResponse<ResponseBody>>()
    val downloadSheetObserver: LiveData<NetworkResponse<ResponseBody>> = downloadSheetLiveData

    private val addSkillLiveData = MutableLiveData<NetworkResponse<Unit>>()
    val addSkillObserver: LiveData<NetworkResponse<Unit>> = addSkillLiveData

    private val collabToolsLiveData = MutableLiveData<NetworkResponse<Unit>>()
    val collabToolsObserver: LiveData<NetworkResponse<Unit>> = collabToolsLiveData

    private val deleteAccountLiveData = MutableLiveData<NetworkResponse<OrgAccountSettingResponse>>()
    val deleteAccountObserver: LiveData<NetworkResponse<OrgAccountSettingResponse>> = deleteAccountLiveData


    fun getCompanyDetails(){
        viewModelScope.launch {
            val userID = preference.getLoginData?.id ?: return@launch
            repository.getCompanyDetails(userID).collect{
                if(it is NetworkResponse.Success)
                    companyProfileLiveData.postValue(it)
            }
        }
    }

    fun getCompanyDomains(){
        viewModelScope.launch {
            val userID = preference.getLoginData?.id ?: return@launch
            repository.getCompanyDomains(userID).collect{
                if(it is NetworkResponse.Success)
                    companyDomains.postValue(it)
            }
        }
    }

    fun getOrgSkills(){
        viewModelScope.launch {
            val userID = preference.getLoginData?.id ?: return@launch
            repository.getOrgSkills(userID).collect{
                if(it is NetworkResponse.Success)
                    companyOrgSkills.postValue(it)
            }
        }
    }

    fun saveCompanyDetails(request: SaveOrgDetails){
        viewModelScope.launch {
            val userID = preference.getLoginData?.id ?: return@launch
            repository.saveCompanyDetails(userID, request).collect{
                if(it is NetworkResponse.Success)
                    saveCompanyProfileLiveData.postValue(it)
            }
        }
    }

    fun saveCollabTools(request: CollabToolsRequest){
        viewModelScope.launch {
            val userID = preference.getLoginData?.id ?: return@launch
            repository.saveCollabTools(userID, request).collect{
                if(it is NetworkResponse.Success)
                    collabToolsLiveData.postValue(it)
            }
        }
    }

    fun getOrgSheet(){
        viewModelScope.launch {
            val userID = preference.getLoginData?.id ?: return@launch
            repository.getOrgSheet(userID).collect{
                if(it is NetworkResponse.Success)
                    downloadSheetLiveData.postValue(it)
            }
        }
    }

    fun uploadOrgSheet(file: File?){
        viewModelScope.launch {
            val userID = preference.getLoginData?.id ?: return@launch
            repository.uploadOrgSheet(userID, file).collect{
                if(it is NetworkResponse.Success)
                    uploadSheetLiveData.postValue(it)
            }
        }
    }

    fun uploadOrgProfilePhoto(file: File?){
        viewModelScope.launch {
            val userID = preference.getLoginData?.id ?: return@launch
            repository.uploadOrgProfilePhoto(userID, file).collect{
                if(it is NetworkResponse.Success)
                    profileImageLiveData.postValue(it)
            }
        }
    }

    fun addOrgSkill(skill: String){
        viewModelScope.launch {
            val userID = preference.getLoginData?.id ?: return@launch
            repository.addOrgSkills(userID, OrgAddSkillRequest(userID, skill)).collect{
                if(it is NetworkResponse.Success)
                    addSkillLiveData.postValue(it)
            }
        }
    }

    fun deleteOrgAccount(type: String){
        viewModelScope.launch {
            val userID = preference.getLoginData?.id ?: return@launch
            repository.deleteOrgAccount(userID, type).collect{
                if(it is NetworkResponse.Success)
                    deleteAccountLiveData.postValue(it)
            }
        }
    }
}